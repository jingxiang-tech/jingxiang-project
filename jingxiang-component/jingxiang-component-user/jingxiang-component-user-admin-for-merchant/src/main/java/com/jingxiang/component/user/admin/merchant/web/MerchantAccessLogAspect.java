package com.jingxiang.component.user.admin.merchant.web;

import com.jingxiang.commons.util.WebUtil;
import com.jingxiang.component.user.admin.merchant.config.MerchantUserAdminProperties;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;

/**
 * 商户后台访问日志切面。
 *
 * @author chenjw
 */
@Aspect
@Order(1)
public class MerchantAccessLogAspect {

    private final MerchantUserAdminProperties properties;

    public MerchantAccessLogAspect(MerchantUserAdminProperties properties) {
        this.properties = properties;
    }

    @Before("@within(org.springframework.stereotype.Controller) ||"
            + "@within(org.springframework.web.bind.annotation.RestController)")
    public void before(JoinPoint joinPoint) {
        if (!ignore()) {
            MerchantAdminLogUtil.access(joinPoint);
        }
    }

    @AfterReturning(
            pointcut = "@within(org.springframework.stereotype.Controller) ||"
                    + "@within(org.springframework.web.bind.annotation.RestController)",
            returning = "returnValue")
    public void afterReturning(Object returnValue) {
        if (!ignore()) {
            MerchantAdminLogUtil.afterReturning(returnValue);
        }
    }

    private boolean ignore() {
        String requestUri = WebUtil.getCurrentRequest().getRequestURI();
        return MerchantPathMatcher.matches(requestUri, properties.getLogIgnorePaths());
    }
}
