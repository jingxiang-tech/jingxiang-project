package com.jingxiang.component.user.admin.platform.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 更新商户用户参数。
 */
@Getter
@Setter
public class MerchantUserUpdate {

    /** 用户 ID */
    @NotNull(message = "用户ID不能为空")
    private Integer userId;

    /** 账户名 */
    @Size(max = 64)
    private String userName;

    /** 真实姓名 */
    @Size(max = 64)
    private String realName;

    /** 手机号 */
    @Size(max = 32)
    private String tel;

    /** 备注 */
    @Size(max = 512)
    private String remark;

    /** 是否禁用 */
    private Integer disabled;
}
