package com.jingxiang.component.user.manager.merchant.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 当前用户密码修改参数。
 *
 * @author chenjw
 */
@Getter
@Setter
public class SystemUserPwdUpdate {

    /** 新密码。 */
    @NotNull
    private String pwd;
}
