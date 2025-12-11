package me.zhengjie.modules.system.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.zhengjie.modules.system.domain.LimiterLog;
import me.zhengjie.modules.system.domain.Limiter;
import me.zhengjie.modules.system.service.LimiterLogService;
import me.zhengjie.modules.system.service.LimiterService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 郑杰
 * @date 2025-06-25
 * 限流拦截器
 */
@Component
public class LimiterInterceptor implements HandlerInterceptor {

    private final LimiterService limiterService;
    private final LimiterLogService limiterLogService;
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    public LimiterInterceptor(LimiterService limiterService, LimiterLogService limiterLogService, StringRedisTemplate stringRedisTemplate, ObjectMapper objectMapper) {
        this.limiterService = limiterService;
        this.limiterLogService = limiterLogService;
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        String ip = getClientIp(request);
        String method = request.getMethod();

        // 查询限流规则
        Limiter limiter = limiterService.findByLimiterKey(uri + ":" + method);
        if (limiter == null || !limiter.getIsEnabled()) {
            // 没有限流规则或未启用，允许访问
            return true;
        }

        // 构建Redis键
        String redisKey = "limiter:" + limiter.getLimiterKey() + ":" + ip;

        // 原子递增请求次数
        Long requestCount = stringRedisTemplate.opsForValue().increment(redisKey);
        if (requestCount == 1) {
            // 设置过期时间
            stringRedisTemplate.expire(redisKey, limiter.getLimiterTimeWindow(), TimeUnit.SECONDS);
        }

        // 检查是否超过限制
        if (requestCount > limiter.getLimiterCount()) {
            // 记录限流日志
            LimiterLog log = new LimiterLog();
            log.setRequestIp(ip);
            log.setRequestPath(uri);
            log.setRequestMethod(method);
            log.setLimiterKey(limiter.getLimiterKey());
            log.setLimiterRule(limiter.toString());
            log.setLimiterTime(new Date());
            log.setHandleResult("限流拦截");
            limiterLogService.create(log);

            // 返回限流提示
            returnLimitExceeded(response, limiter);
            return false;
        }

        return true;
    }

    /**
     * 获取客户端IP
     * @param request 请求对象
     * @return 客户端IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 返回限流提示
     * @param response 响应对象
     * @param limiter 限流规则
     * @throws IOException IO异常
     */
    private void returnLimitExceeded(HttpServletResponse response, Limiter limiter) throws IOException {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> result = new HashMap<>();
        result.put("code", HttpStatus.TOO_MANY_REQUESTS.value());
        result.put("message", limiter.getLimiterMessage() != null ? limiter.getLimiterMessage() : "请求过于频繁，请在" + limiter.getLimiterTimeWindow() + "秒后重试");

        PrintWriter writer = response.getWriter();
        writer.write(objectMapper.writeValueAsString(result));
        writer.flush();
        writer.close();
    }
}
