package com.jingxiang.component.user.jwt;

import com.jingxiang.commons.util.WebUtil;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * 通用会话支持：读 Authorization → 验签 → 反序列化 → tokenVersion 校验 → 可选业务校验
 *
 * @param <T> 子系统会话类型
 * @author chenjw
 */
public class SessionSupport<T extends BaseSessionUser> {

    public static final String DEFAULT_HEADER = "Authorization";

    private final JwtCodec jwtCodec;
    private final Class<T> sessionType;
    private final double minTokenVersion;
    private final SessionValidator<T> validator;
    private final String headerName;
    /**
     * 未登录 / token 无效
     */
    private final Supplier<? extends RuntimeException> unavailableException;
    /**
     * 已登录但关键字段缺失（如 app 未注册：userId/tenantCode 为空）
     */
    private final Supplier<? extends RuntimeException> incompleteException;

    /**
     * 最简构造：默认异常 {@link SessionUnavailableException}
     */
    public SessionSupport(JwtCodec jwtCodec, Class<T> sessionType) {
        this(jwtCodec, sessionType, null, SessionUnavailableException::new);
    }

    /**
     * 指定未登录异常类型（多数子系统使用；字段缺失异常与之相同）
     */
    public SessionSupport(JwtCodec jwtCodec,
                          Class<T> sessionType,
                          Supplier<? extends RuntimeException> unavailableException) {
        this(jwtCodec, sessionType, null, unavailableException);
    }

    /**
     * 带业务校验 + 自定义异常（字段缺失异常与未登录异常相同）
     */
    public SessionSupport(JwtCodec jwtCodec,
                          Class<T> sessionType,
                          SessionValidator<T> validator,
                          Supplier<? extends RuntimeException> unavailableException) {
        this(jwtCodec, sessionType, validator, unavailableException, unavailableException);
    }

    /**
     * 未登录异常与字段缺失异常分开配置（如 app：401 vs 4010）
     */
    public SessionSupport(JwtCodec jwtCodec,
                          Class<T> sessionType,
                          SessionValidator<T> validator,
                          Supplier<? extends RuntimeException> unavailableException,
                          Supplier<? extends RuntimeException> incompleteException) {
        this(jwtCodec,
                sessionType,
                jwtCodec.getProperties().getTokenVersion(),
                validator,
                DEFAULT_HEADER,
                unavailableException,
                incompleteException);
    }

    public SessionSupport(JwtCodec jwtCodec,
                          Class<T> sessionType,
                          double minTokenVersion,
                          SessionValidator<T> validator,
                          String headerName,
                          Supplier<? extends RuntimeException> unavailableException,
                          Supplier<? extends RuntimeException> incompleteException) {
        this.jwtCodec = Objects.requireNonNull(jwtCodec);
        this.sessionType = Objects.requireNonNull(sessionType);
        this.minTokenVersion = minTokenVersion;
        this.validator = validator;
        this.headerName = headerName != null ? headerName : DEFAULT_HEADER;
        this.unavailableException = unavailableException != null
                ? unavailableException
                : SessionUnavailableException::new;
        this.incompleteException = incompleteException != null
                ? incompleteException
                : this.unavailableException;
    }

    public JwtCodec getJwtCodec() {
        return jwtCodec;
    }

    /**
     * 签发 token，并写入 tokenVersion
     */
    public String issue(T session) {
        session.setTokenVersion(minTokenVersion);
        return jwtCodec.create(session);
    }

    /**
     * 必须登录，否则抛异常
     */
    public T require() {
        String token = WebUtil.getHeader(headerName);
        if (token == null) {
            throw newUnavailable();
        }
        T session = jwtCodec.parseQuietly(token, sessionType);
        if (session == null) {
            throw newUnavailable();
        }
        if (session.getTokenVersion() == null || session.getTokenVersion() < minTokenVersion) {
            throw newUnavailable();
        }
        if (validator != null) {
            validator.validate(session);
        }
        return session;
    }

    /**
     * 未登录或无效返回 null
     */
    public T unchecked() {
        String token = WebUtil.getHeader(headerName);
        if (token == null) {
            return null;
        }
        T session = jwtCodec.parseQuietly(token, sessionType);
        if (session == null) {
            return null;
        }
        if (session.getTokenVersion() != null && session.getTokenVersion() < minTokenVersion) {
            return null;
        }
        if (validator != null) {
            try {
                validator.validate(session);
            } catch (RuntimeException e) {
                return null;
            }
        }
        return session;
    }

    /**
     * 未登录 / token 无效
     */
    public RuntimeException newUnavailable() {
        return unavailableException.get();
    }

    /**
     * 已登录但关键字段缺失
     */
    public RuntimeException newIncomplete() {
        return incompleteException.get();
    }
}
