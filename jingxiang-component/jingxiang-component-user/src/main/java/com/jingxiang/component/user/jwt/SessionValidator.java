package com.jingxiang.component.user.jwt;

/**
 * 会话附加校验（可选），例如查库确认用户未禁用
 *
 * @param <T> 会话类型
 * @author chenjw
 */
@FunctionalInterface
public interface SessionValidator<T extends BaseSessionUser> {

    /**
     * 校验失败应抛出业务异常或 {@link SessionUnavailableException}
     */
    void validate(T session);
}
