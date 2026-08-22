package com.jingxiang.component.user.jwt;

import com.jingxiang.commons.model.dict.WhetherDict;
import com.jingxiang.commons.util.SpringContextUtil;
import com.jingxiang.component.user.model.user.UserPo;
import com.jingxiang.component.user.service.UserService;

import java.util.function.Function;

/**
 * 会话辅助：取 Bean / 账号状态校验
 *
 * @author chenjw
 */
public final class SessionSupports {

    private SessionSupports() {
    }

    @SuppressWarnings("unchecked")
    public static <T extends BaseSessionUser> SessionSupport<T> get() {
        return SpringContextUtil.getBean(SessionSupport.class);
    }

    /**
     * 校验用户存在且未删除、未禁用
     *
     * @param exceptionFactory 消息 → 异常（各端自定义异常类型）
     */
    public static <T extends BaseSessionUser> SessionValidator<T> enabledAccount(
            UserService userService,
            Function<String, ? extends RuntimeException> exceptionFactory) {
        return session -> {
            if (session == null || session.getUserId() == null) {
                throw exceptionFactory.apply("会话不可用");
            }
            UserPo user = userService.getById(session.getUserId().longValue());
            if (user == null) {
                throw exceptionFactory.apply("账号不存在，请重新登录");
            }
            if (WhetherDict.Yes.code == user.getDeleted()) {
                throw exceptionFactory.apply("账号已被删除，请联系管理员");
            }
            if (WhetherDict.Yes.code == user.getForbidden()) {
                throw exceptionFactory.apply("账号已被禁用，请联系管理员");
            }
        };
    }
}
