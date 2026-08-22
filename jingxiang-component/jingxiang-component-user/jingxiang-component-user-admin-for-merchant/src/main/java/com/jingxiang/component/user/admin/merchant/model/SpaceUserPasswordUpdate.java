package com.jingxiang.component.user.admin.merchant.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 重置空间用户密码。
 *
 * @author chenjw
 */
@Getter
@Setter
public class SpaceUserPasswordUpdate {

    /** 空间用户ID。 */
    @NotNull(message = "userId不能为空")
    private Integer userId;

    /** 新密码。 */
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 64, message = "新密码长度应为6-64位")
    private String newPassword;
}
