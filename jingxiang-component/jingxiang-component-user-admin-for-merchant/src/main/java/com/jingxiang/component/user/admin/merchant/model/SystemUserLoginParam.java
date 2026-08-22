package com.jingxiang.component.user.admin.merchant.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 商户后台账号密码登录参数。
 *
 * @author chenjw
 */
@Getter
@Setter
public class SystemUserLoginParam {

    /**
     * 手机号或用户账号。
     */
    @NotNull(message = "账号不能为空")
    private String id;

    /**
     * 密码。
     */
    @NotNull(message = "密码不能为空")
    private String pwd;
}
