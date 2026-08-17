package com.jingxiang.component.user.model.identity;

import com.jingxiang.component.user.dict.IdentityTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 创建/绑定第三方身份
 *
 * @author chenjw
 */
@Getter
@Setter
public class UserIdentityCreate {

    /**
     * 用户ID（绑定已有用户时必填；第三方自动注册可不传）
     */
    private Long userId;

    /**
     * 身份类型
     */
    @NotNull(message = "身份类型不能为空")
    private IdentityTypeEnum identityType;

    /**
     * 第三方平台应用标识
     */
    @NotNull(message = "appKey不能为空")
    @Size(max = 128, message = "appKey最长128位")
    private String appKey;

    /**
     * 第三方身份标识
     */
    @NotBlank(message = "identifier不能为空")
    @Size(max = 255, message = "identifier最长255位")
    private String identifier;

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
