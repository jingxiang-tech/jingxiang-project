package com.jingxiang.component.user.manager.merchant.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 商户空间用户密码更新参数。
 *
 * @author chenjw
 */
@Getter
@Setter
public class MerchantSpaceUserPasswordUpdate {

    /** 用户ID。 */
    @NotNull(message = "用户ID不能为空")
    private Integer userId;

    /** 新密码。 */
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 64, message = "新密码长度应为6-64位")
    private String newPassword;
}
