package com.jingxiang.component.user.manager.merchant.web;

import com.jingxiang.commons.util.WebUtil;
import com.jingxiang.component.user.manager.merchant.config.MerchantUserManagerProperties;
import com.jingxiang.component.user.jwt.SessionUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * 商户后台全局会话校验。
 *
 * @author chenjw
 */
@Aspect
public class MerchantSessionCheckAspect {

    private final MerchantUserManagerProperties properties;

    public MerchantSessionCheckAspect(MerchantUserManagerProperties properties) {
        this.properties = properties;
    }

    @Around("@within(org.springframework.stereotype.Controller) ||"
            + "@within(org.springframework.web.bind.annotation.RestController)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        String requestUri = WebUtil.getCurrentRequest().getRequestURI();
        if (MerchantPathMatcher.matches(requestUri, properties.getAuthIgnorePaths())) {
            return joinPoint.proceed();
        }
        if (properties.getAllowNullSpacePathPrefixes().stream().anyMatch(requestUri::startsWith)) {
            SessionUtil.getSessionUser();
        } else {
            SessionUtil.requireSpaceId();
        }
        return joinPoint.proceed();
    }
}
