package com.jingxiang.component.user.model.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 修改密码
 *
 * @author chenjw
 */
@Getter
@Setter
public class UserPasswordUpdate {

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 新明文密码
     */
    @NotBlank(message = "新密码不能为空")
    private String newPassword;

    /**
     * 旧明文密码（可选，有则校验）
     */
    private String oldPassword;
}
