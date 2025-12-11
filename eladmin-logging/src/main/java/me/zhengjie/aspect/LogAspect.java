/*
 *  Copyright 2019-2025 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.aspect;

import lombok.extern.slf4j.Slf4j;
import me.zhengjie.domain.SysLog;
import me.zhengjie.service.SysLogService;
import me.zhengjie.utils.RequestHolder;
import me.zhengjie.utils.SecurityUtils;
import me.zhengjie.utils.StringUtils;
import me.zhengjie.utils.ThrowableUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Zheng Jie
 * @date 2018-11-24
 */
@Component
@Aspect
@Slf4j
public class LogAspect {

    private final SysLogService sysLogService;

    ThreadLocal<Long> currentTime = new ThreadLocal<>();

    public LogAspect(SysLogService sysLogService) {
        this.sysLogService = sysLogService;
    }

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(me.zhengjie.annotation.Log)")
    public void logPointcut() {
        // 该方法无方法体,主要为了让同类中其他方法使用此切入点
    }

    /**
     * 配置环绕通知,使用在方法logPointcut()上注册的切入点
     *
     * @param joinPoint join point for advice
     */
    @Around("logPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        currentTime.set(System.currentTimeMillis());
        result = joinPoint.proceed();
        SysLog sysLog = new SysLog("INFO",System.currentTimeMillis() - currentTime.get());
        currentTime.remove();
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        // 获取Log注解中的module和action
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log log = method.getAnnotation(Log.class);
        if (log != null) {
            sysLog.setModule(log.module());
            sysLog.setAction(log.action());
        }
        // 获取目标ID
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            // 假设第一个参数是ID或者包含ID的对象
            Object arg = args[0];
            if (arg != null) {
                if (arg instanceof Long || arg instanceof String) {
                    sysLog.setTargetId(arg.toString());
                } else {
                    // 尝试从对象中获取ID属性
                    try {
                        Method getIdMethod = arg.getClass().getMethod("getId");
                        Object id = getIdMethod.invoke(arg);
                        if (id != null) {
                            sysLog.setTargetId(id.toString());
                        }
                    } catch (Exception e) {
                        // 忽略异常
                    }
                }
            }
        }
        sysLogService.save(getUsername(), StringUtils.getBrowser(request), StringUtils.getIp(request),joinPoint, sysLog);
        return result;
    }

    /**
     * 配置异常通知
     *
     * @param joinPoint join point for advice
     * @param e exception
     */
    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        SysLog sysLog = new SysLog("ERROR",System.currentTimeMillis() - currentTime.get());
        currentTime.remove();
        sysLog.setExceptionDetail(ThrowableUtil.getStackTrace(e).getBytes());
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        // 获取Log注解中的module和action
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log log = method.getAnnotation(Log.class);
        if (log != null) {
            sysLog.setModule(log.module());
            sysLog.setAction(log.action());
        }
        // 获取目标ID
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            // 假设第一个参数是ID或者包含ID的对象
            Object arg = args[0];
            if (arg != null) {
                if (arg instanceof Long || arg instanceof String) {
                    sysLog.setTargetId(arg.toString());
                } else {
                    // 尝试从对象中获取ID属性
                    try {
                        Method getIdMethod = arg.getClass().getMethod("getId");
                        Object id = getIdMethod.invoke(arg);
                        if (id != null) {
                            sysLog.setTargetId(id.toString());
                        }
                    } catch (Exception ex) {
                        // 忽略异常
                    }
                }
            }
        }
        sysLogService.save(getUsername(), StringUtils.getBrowser(request), StringUtils.getIp(request), (ProceedingJoinPoint)joinPoint, sysLog);
    }

    /**
     * 获取用户名
     * @return /
     */
    public String getUsername() {
        try {
            return SecurityUtils.getCurrentUsername();
        }catch (Exception e){
            return "";
        }
    }
}
