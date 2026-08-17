package com.jingxiang.component.user.jwt;

/**
 * 会话不可用（未登录 / token 无效 / 版本过低）
 *
 * @author chenjw
 */
public class SessionUnavailableException extends RuntimeException {

    public SessionUnavailableException() {
        super("会话不可用");
    }

    public SessionUnavailableException(String message) {
        super(message);
    }
}
