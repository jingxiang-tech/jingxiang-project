package com.jingxiang.component.user.jwt;

import java.time.LocalDateTime;

/**
 * 通用会话载荷基类
 * <p>
 * 子系统扩展私有字段即可，例如 spaceId、roleCodes。
 *
 * @author chenjw
 */
public class BaseSessionUser {

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 手机号
     */
    private String tel;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 登录时间
     */
    private LocalDateTime loginTime;

    /**
     * token 版本
     */
    private Double tokenVersion;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 租户名称
     */
    private String tenantName;

    /**
     * 登录方式
     */
    private String loginWay;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public Double getTokenVersion() {
        return tokenVersion;
    }

    public void setTokenVersion(Double tokenVersion) {
        this.tokenVersion = tokenVersion;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getLoginWay() {
        return loginWay;
    }

    public void setLoginWay(String loginWay) {
        this.loginWay = loginWay;
    }
}
