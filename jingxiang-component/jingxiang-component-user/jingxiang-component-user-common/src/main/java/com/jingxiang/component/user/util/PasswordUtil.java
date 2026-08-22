package com.jingxiang.component.user.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;

/**
 * 密码工具（仅 BCrypt）
 *
 * @author chenjw
 */
public final class PasswordUtil {

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    private PasswordUtil() {
    }

    /**
     * 明文密码编码为 BCrypt Hash
     */
    public static String encode(String rawPassword) {
        if (!StringUtils.hasText(rawPassword)) {
            return null;
        }
        return ENCODER.encode(rawPassword);
    }

    /**
     * 校验明文密码与 BCrypt Hash
     *
     * @return true 匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        if (!StringUtils.hasText(rawPassword) || !StringUtils.hasText(encodedPassword)) {
            return false;
        }
        return ENCODER.matches(rawPassword, encodedPassword);
    }
}
