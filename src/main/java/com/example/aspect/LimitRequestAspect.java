package com.example.aspect;


import com.example.annotation.LimitRequest;
import com.example.exception.ApiRuntimeException;
import com.example.exception.asserts.Assert;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author 18237
 */

@Aspect
@Component
public class LimitRequestAspect {

    private static ConcurrentHashMap<String, ExpiringMap<String, Integer>> limitMap = new ConcurrentHashMap<>();

    /**
     * @param limitRequest
     * @return
     * @decription: 定义切点让所有被@LimitRequest注解标记的方法都执行切面方法
     */
    @Pointcut("@annotation(limitRequest)")
    public void executeLimitService(LimitRequest limitRequest) {
    }

    @Around("executeLimitService(limitRequest)")
    public Object doAround(ProceedingJoinPoint joinPoint, LimitRequest limitRequest) throws Throwable {

        // 获得request对象
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) attributes;
        HttpServletRequest request = requestAttributes.getRequest();

        // 获取Map对象,如果没有则返回默认值, 第一个参数是key,第二个参数是默认值
        ExpiringMap<String, Integer> expiringMap = limitMap.getOrDefault(request.getRequestURI(), ExpiringMap.builder().variableExpiration().build());
        Integer requestCount = expiringMap.getOrDefault(request.getRemoteAddr(), 0);

        // 超过次数，不执行目标方法
        if (requestCount >= limitRequest.count()) {
            throw new ApiRuntimeException(Assert.OPERATE_FREQUENCY, "用户操作频繁,请稍后再试");
            // 第一次请求时，设置有效时间
        } else if (requestCount == 0) {
            expiringMap.put(request.getRemoteAddr(), requestCount + 1, ExpirationPolicy.CREATED, limitRequest.time(), TimeUnit.MILLISECONDS);
            // 未超过次数,记录加一
        } else {
            expiringMap.put(request.getRemoteAddr(), requestCount + 1);
        }
        limitMap.put(request.getRequestURI(), expiringMap);

        // result的值就是被拦截方法的返回值
        Object result = joinPoint.proceed();
        return result;
    }
}

