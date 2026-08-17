package com.jingxiang.component.user.model.identity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 更新第三方身份资料
 *
 * @author chenjw
 */
@Getter
@Setter
public class UserIdentityUpdate {

    /**
     * 身份ID
     */
    @NotNull(message = "identityId不能为空")
    private Long identityId;

    /**
     * 跨应用统一身份
     */
    @Size(max = 128, message = "unionId最长128位")
    private String unionId;

    /**
     * 第三方昵称
     */
    @Size(max = 64, message = "昵称最长64位")
    private String nickname;

    /**
     * 第三方头像
     */
    @Size(max = 512, message = "头像URL过长")
    private String avatar;

    /**
     * 备注
     */
    @Size(max = 255, message = "备注最长255位")
    private String remark;
}
