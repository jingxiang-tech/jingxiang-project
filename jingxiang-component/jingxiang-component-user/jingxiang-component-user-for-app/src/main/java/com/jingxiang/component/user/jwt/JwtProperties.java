package com.jingxiang.component.user.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * JWT 配置（各子系统通过 application.yml 隔离 secret/issuer）
 * <p>
 * 前缀：{@code jingxiang.component.user.jwt}
 *
 * @author chenjw
 */
@ConfigurationProperties(prefix = "jingxiang.component.user.jwt")
public class JwtProperties {

    /**
     * HMAC 密钥
     */
    private String secret;

    /**
     * 签发方
     */
    private String issuer;

    /**
     * 接收方
     */
    private String audience = "audience";

    /**
     * 有效期（毫秒），默认一年
     */
    private long expireMillis = 31536000000L;

    /**
     * Session tokenVersion 下限
     */
    private double tokenVersion = 1.0;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public long getExpireMillis() {
        return expireMillis;
    }

    public void setExpireMillis(long expireMillis) {
        this.expireMillis = expireMillis;
    }

    public double getTokenVersion() {
        return tokenVersion;
    }

    public void setTokenVersion(double tokenVersion) {
        this.tokenVersion = tokenVersion;
    }
}
