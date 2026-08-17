package com.jingxiang.component.user.jwt;

import com.alibaba.fastjson2.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Objects;

/**
 * JWT 编解码（模板流程：签发 / 验签 / 反序列化）
 * <p>
 * 各子系统用不同 {@link JwtProperties} 创建独立实例，实现端隔离。
 *
 * @author chenjw
 */
public class JwtCodec {

    private static final Logger log = LoggerFactory.getLogger(JwtCodec.class);
    private static final String BEARER = "Bearer ";

    private final JwtProperties properties;
    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    public JwtCodec(JwtProperties properties) {
        Objects.requireNonNull(properties, "jwt properties");
        Objects.requireNonNull(properties.getSecret(), "jwt secret");
        Objects.requireNonNull(properties.getIssuer(), "jwt issuer");
        this.properties = properties;
        this.algorithm = Algorithm.HMAC256(properties.getSecret());
        this.verifier = JWT.require(this.algorithm)
                .withIssuer(properties.getIssuer())
                .build();
    }

    public JwtProperties getProperties() {
        return properties;
    }

    /**
     * 签发 JWT，subject 序列化为 JSON
     */
    public String create(Object subject) {
        return JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + properties.getExpireMillis()))
                .withAudience(properties.getAudience())
                .withIssuer(properties.getIssuer())
                .withSubject(JSON.toJSONString(subject))
                .sign(algorithm);
    }

    /**
     * 验签，失败返回 null
     */
    public DecodedJWT verify(String jwtToken) {
        try {
            String token = stripBearer(jwtToken);
            if (!StringUtils.hasText(token)) {
                return null;
            }
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            log.warn("JWT 验签失败, issuer={}", properties.getIssuer());
            return null;
        }
    }

    /**
     * 验签并反序列化为指定类型，失败返回 null
     */
    public <T> T parseQuietly(String jwtToken, Class<T> type) {
        DecodedJWT jwt = verify(jwtToken);
        if (jwt == null) {
            return null;
        }
        return JSON.parseObject(jwt.getSubject(), type);
    }

    public static String stripBearer(String jwtToken) {
        if (jwtToken == null) {
            return null;
        }
        if (jwtToken.startsWith(BEARER)) {
            return jwtToken.substring(BEARER.length());
        }
        return jwtToken;
    }
}
