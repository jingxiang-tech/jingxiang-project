package com.inbyte.commons.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 限流注解
 * <p>
 * 默认限流一分钟一次请求
 *
 * rate = 1, unit = TimeUnit.MINUTES; 一分钟通行一次
 * rate = 5, unit = TimeUnit.MINUTES; 一分钟通行五次，第一次放行，后面每12秒放行一次
 * rate = 1, unit = TimeUnit.SECONDS; 一秒钟通行一次
 * rate = 5, unit = TimeUnit.SECONDS; 一秒钟通行五次，第一次放行，后面每1/5(200毫秒)秒放行一次
 *
 * rate = 30, 每两秒一次放行
 * @author chenjw
 * @date 2024/3/14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RateLimit {

    /**
     * 单位内允许请求次数
     */
    double rate() default 1;

    /**
     * 单位
     */
    TimeUnit unit() default TimeUnit.MINUTES;
}