package me.zhengjie.config;

import me.zhengjie.modules.system.interceptor.LimiterInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 郑杰
 * @date 2025-06-25
 * 限流拦截器配置
 */
@Configuration
public class LimiterInterceptorConfig implements WebMvcConfigurer {

    private final LimiterInterceptor limiterInterceptor;

    public LimiterInterceptorConfig(LimiterInterceptor limiterInterceptor) {
        this.limiterInterceptor = limiterInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册限流拦截器，拦截所有请求
        registry.addInterceptor(limiterInterceptor).addPathPatterns("/**");
    }
}
