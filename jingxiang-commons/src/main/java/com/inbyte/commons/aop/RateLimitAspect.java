package com.inbyte.commons.aop;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.RateLimiter;
import com.inbyte.commons.exception.InbyteException;
import com.inbyte.commons.model.dto.R;
import com.inbyte.commons.util.IpUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class RateLimitAspect {

    private Cache<String, RateLimiter> ipRateLimiters  = CacheBuilder.newBuilder()
            .expireAfterAccess(2, TimeUnit.MINUTES) // 设置过期时间为2分钟
            .build();

    @Pointcut("@annotation(rateLimit)")
    public void rateLimitPointcut(RateLimit rateLimit) {}

    @Around("rateLimitPointcut(rateLimit)")
    public Object limitRequest(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        String key = className + "#" + methodName + "#" + IpUtil.getWanIpAddress();

        RateLimiter rateLimiter = ipRateLimiters.get(key, () -> RateLimiter.create(getPermitsPerSecond(rateLimit)));

        if (rateLimiter.tryAcquire()) {
            return joinPoint.proceed();
        } else {
            return R.fail("请求频率太高啦，稍等下再试");
        }

    }

    private double getPermitsPerSecond(RateLimit rateLimit) {
        switch (rateLimit.unit()) {
            case SECONDS:
                return rateLimit.rate();
            case MINUTES:
                return rateLimit.rate() / 60;
            case HOURS:
                return rateLimit.rate() / 60 / 60;
        }
        throw InbyteException.error("RateLimit 限流单位不支持");
    }
}