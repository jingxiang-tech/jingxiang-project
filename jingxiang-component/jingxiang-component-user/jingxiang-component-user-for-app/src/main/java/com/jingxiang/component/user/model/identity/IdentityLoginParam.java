package com.jingxiang.component.user.model.identity;

import com.jingxiang.component.user.dict.IdentityTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 第三方登录/注册入参
 *
 * @author chenjw
 */
@Getter
@Setter
public class IdentityLoginParam {

    /**
     * 身份类型
     */
    @NotNull(message = "身份类型不能为空")
    private IdentityTypeEnum identityType;

    /**
     * 第三方平台应用标识
     */
    @NotNull(message = "appKey不能为空")
    @Size(max = 128)
    private String appKey;

    /**
     * 第三方身份标识
     */
    @NotBlank(message = "identifier不能为空")
    @Size(max = 255)
    private String identifier;

    /**
     * 跨应用统一身份
     */
    @Size(max = 128)
    private String unionId;

    /**
     * 第三方昵称
     */
    @Size(max = 64)
    private String nickname;

    /**
     * 第三方头像
     */
    @Size(max = 512)
    private String avatar;
}
