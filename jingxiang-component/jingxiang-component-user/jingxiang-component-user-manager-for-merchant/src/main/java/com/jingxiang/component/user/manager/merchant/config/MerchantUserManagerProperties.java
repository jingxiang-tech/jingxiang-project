package com.jingxiang.component.user.manager.merchant.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 商户后台用户管理配置。
 *
 * @author chenjw
 */
@ConfigurationProperties(prefix = "jingxiang.component.user.manager.merchant")
public class MerchantUserManagerProperties {

    /**
     * 重置密码时使用的初始密码。
     */
    private String initialPassword = "123456";

    /**
     * 已存在统一用户首次加入商户时使用的密码。
     */
    private String existingUserPassword = "Aa123456";

    /**
     * 无需登录校验的请求路径，支持 Ant 风格表达式。
     */
    private List<String> authIgnorePaths = new ArrayList<>(List.of(
            "/health",
            "/error",
            "/aliyun/oss/callback",
            "/api/user/merchant/weixin/mp/login",
            "/api/system/user/login/id-pwd",
            "/api/user/merchant/login/id-pwd",
            "/api/common/district/province",
            "/api/common/district/city",
            "/api/common/district/district",
            "/api/ai/customer/service/on-wechat",
            "/api/automation/task",
            "/**/dict/**",
            "/**/ai/**",
            "/**/openapi/**",
            "/**/aliyun/oss/callback"
    ));

    /**
     * 允许会话尚未选择业务空间的请求前缀。
     */
    private List<String> allowNullSpacePathPrefixes = new ArrayList<>(List.of(
            "/api/system/user/info",
            "/api/system/user/space"
    ));

    /**
     * 不记录访问日志的请求路径，支持 Ant 风格表达式。
     */
    private List<String> logIgnorePaths = new ArrayList<>(List.of(
            "/health",
            "/error",
            "/aliyun/oss/callback",
            "/**/dict/**",
            "/**/aliyun/oss/callback"
    ));

    public String getInitialPassword() {
        return initialPassword;
    }

    public void setInitialPassword(String initialPassword) {
        this.initialPassword = initialPassword;
    }

    public String getExistingUserPassword() {
        return existingUserPassword;
    }

    public void setExistingUserPassword(String existingUserPassword) {
        this.existingUserPassword = existingUserPassword;
    }

    public List<String> getAuthIgnorePaths() {
        return authIgnorePaths;
    }

    public void setAuthIgnorePaths(List<String> authIgnorePaths) {
        this.authIgnorePaths = authIgnorePaths;
    }

    public List<String> getAllowNullSpacePathPrefixes() {
        return allowNullSpacePathPrefixes;
    }

    public void setAllowNullSpacePathPrefixes(List<String> allowNullSpacePathPrefixes) {
        this.allowNullSpacePathPrefixes = allowNullSpacePathPrefixes;
    }

    public List<String> getLogIgnorePaths() {
        return logIgnorePaths;
    }

    public void setLogIgnorePaths(List<String> logIgnorePaths) {
        this.logIgnorePaths = logIgnorePaths;
    }
}
