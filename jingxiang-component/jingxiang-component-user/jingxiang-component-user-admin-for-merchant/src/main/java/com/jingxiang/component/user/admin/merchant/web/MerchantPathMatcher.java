package com.jingxiang.component.user.admin.merchant.web;

import org.springframework.util.AntPathMatcher;

import java.util.List;

/**
 * 商户后台配置路径匹配器。
 */
final class MerchantPathMatcher {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private MerchantPathMatcher() {
    }

    static boolean matches(String requestUri, List<String> patterns) {
        return patterns != null && patterns.stream()
                .anyMatch(pattern -> PATH_MATCHER.match(pattern, requestUri));
    }
}
