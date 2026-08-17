package com.jingxiang.component.user.jwt;

import java.util.Collections;
import java.util.List;

/**
 * 统一 Session 工具
 * <p>
 * 未登录异常、字段缺失异常由各端 {@link SessionSupport} 注入；
 * 扩展字段通过 {@link SpaceSession} / {@link RoleSession} 按需使用。
 *
 * @author chenjw
 */
public final class SessionUtil {

    private SessionUtil() {
    }

    @SuppressWarnings("unchecked")
    private static <T extends BaseSessionUser> SessionSupport<T> session() {
        return SessionSupports.get();
    }

    /**
     * 必须登录
     */
    @SuppressWarnings("unchecked")
    public static <T extends BaseSessionUser> T getSessionUser() {
        return (T) session().require();
    }

    /**
     * 未登录返回 null
     */
    @SuppressWarnings("unchecked")
    public static <T extends BaseSessionUser> T getSessionUserUnchecked() {
        return (T) session().unchecked();
    }

    /**
     * 签发 JWT
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static String getJwtToken(BaseSessionUser sessionUser) {
        SessionSupport support = SessionSupports.get();
        return support.issue(sessionUser);
    }

    public static boolean logged() {
        return getSessionUserUnchecked() != null;
    }

    public static Integer getUserId() {
        return requireField(getSessionUser().getUserId());
    }

    public static String getTenantCode() {
        return requireField(getSessionUser().getTenantCode());
    }

    public static Long getTenantId() {
        return requireField(getSessionUser().getTenantId());
    }

    public static String getUserName() {
        return getSessionUser().getUserName();
    }

    /**
     * 业务空间 ID（需会话实现 {@link SpaceSession}）；允许为 null
     */
    public static Integer getSpaceId() {
        return asSpace(getSessionUser()).getSpaceId();
    }

    /**
     * 未登录或无空间时返回 null
     */
    public static Integer getSpaceIdUnchecked() {
        BaseSessionUser su = getSessionUserUnchecked();
        if (!(su instanceof SpaceSession space)) {
            return null;
        }
        return space.getSpaceId();
    }

    /**
     * 必须有空间 ID，否则抛未登录类异常（非字段缺失异常）
     */
    public static Integer requireSpaceId() {
        Integer spaceId = getSpaceId();
        if (spaceId == null) {
            throw session().newUnavailable();
        }
        return spaceId;
    }

    /**
     * 角色编码列表（需会话实现 {@link RoleSession}）
     */
    public static List<String> getRoleCodes() {
        List<String> roleCodes = asRole(getSessionUser()).getRoleCodes();
        if (roleCodes == null) {
            return Collections.emptyList();
        }
        return roleCodes;
    }

    public static boolean hasRole(String roleCode) {
        if (roleCode == null || roleCode.isBlank()) {
            return false;
        }
        return getRoleCodes().stream().anyMatch(roleCode::equalsIgnoreCase);
    }

    private static <T> T requireField(T value) {
        if (value == null) {
            throw session().newIncomplete();
        }
        return value;
    }

    private static SpaceSession asSpace(BaseSessionUser user) {
        if (user instanceof SpaceSession space) {
            return space;
        }
        throw session().newUnavailable();
    }

    private static RoleSession asRole(BaseSessionUser user) {
        if (user instanceof RoleSession role) {
            return role;
        }
        throw session().newUnavailable();
    }
}
