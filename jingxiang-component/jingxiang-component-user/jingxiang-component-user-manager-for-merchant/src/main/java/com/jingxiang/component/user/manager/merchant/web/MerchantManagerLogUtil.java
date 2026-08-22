package com.jingxiang.component.user.manager.merchant.web;

import com.alibaba.fastjson2.JSON;
import com.jingxiang.commons.model.dto.R;
import com.jingxiang.commons.util.IpUtil;
import com.jingxiang.commons.util.WebUtil;
import com.jingxiang.component.user.manager.merchant.session.MerchantSessionUser;
import com.jingxiang.component.user.jwt.SessionUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 商户后台访问日志工具。
 *
 * @author chenjw
 */
final class MerchantManagerLogUtil {

    private static final Logger ACCESS_LOGGER = LoggerFactory.getLogger("后台访问日志");
    private static final Logger RETURN_LOGGER = LoggerFactory.getLogger("后台请求结果");
    private static final String EMPTY_JSON = "{}";
    private static final String DEFAULT_VALUE = "-";

    private MerchantManagerLogUtil() {
    }

    static void access(JoinPoint joinPoint) {
        ACCESS_LOGGER.info(requestInfo(joinPoint));
    }

    static void afterReturning(Object returnValue) {
        String result = returnValue instanceof R ? returnValue.toString() : JSON.toJSONString(returnValue);
        if (result.length() > 512) {
            result = result.substring(0, 512);
        }
        RETURN_LOGGER.info("url:{}, result:{}", WebUtil.getRequestUri(), result);
    }

    private static String requestInfo(JoinPoint joinPoint) {
        HttpServletRequest request = WebUtil.getCurrentRequest();
        StringBuilder builder = new StringBuilder();
        MerchantSessionUser sessionUser = SessionUtil.getSessionUserUnchecked();
        builder.append("userId:").append(sessionUser != null ? sessionUser.getUserId() : DEFAULT_VALUE);
        builder.append(" userName:").append(sessionUser != null ? sessionUser.getUserName() : DEFAULT_VALUE);
        builder.append(" wanIp:").append(IpUtil.getWanIpAddress());
        builder.append(" lanIp:").append(IpUtil.getLanIpAddress());
        String requestUri = request.getRequestURI();
        if (requestUri.contains("/error")) {
            Object forwardUri = request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
            requestUri = forwardUri != null ? forwardUri.toString() : requestUri;
        }
        builder.append(" url:").append(requestUri);
        builder.append(" requestMethod:").append(request.getMethod());
        builder.append(" queryStr:").append(defaultIfEmpty(request.getQueryString(), DEFAULT_VALUE));
        builder.append(" formParam:")
                .append(defaultIfEmpty(JSON.toJSONString(request.getParameterMap()), EMPTY_JSON));
        builder.append(" requestBody:").append(jsonBody(request, joinPoint));
        builder.append(" userAgent:").append(request.getHeader("user-agent"));
        return builder.toString();
    }

    private static String jsonBody(HttpServletRequest request, JoinPoint joinPoint) {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            return EMPTY_JSON;
        }
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Annotation[][] annotations = method.getParameterAnnotations();
        for (int index = 0; index < annotations.length; index++) {
            for (Annotation annotation : annotations[index]) {
                if (annotation instanceof RequestBody) {
                    return JSON.toJSONString(joinPoint.getArgs()[index]);
                }
            }
        }
        return EMPTY_JSON;
    }

    private static String defaultIfEmpty(String value, String defaultValue) {
        return value == null || value.isEmpty() ? defaultValue : value;
    }
}
