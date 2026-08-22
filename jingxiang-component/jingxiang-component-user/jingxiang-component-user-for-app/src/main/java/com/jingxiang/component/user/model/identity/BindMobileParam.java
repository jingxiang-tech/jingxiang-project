package com.jingxiang.component.user.model.identity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 绑定手机号
 *
 * @author chenjw
 */
@Getter
@Setter
public class BindMobileParam {

    /**
     * 当前用户ID
     */
    @NotNull(message = "userId不能为空")
    private Long userId;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @Size(max = 32)
    private String tel;

    /**
     * 短信验证码（由 MobileVerifyPort 校验；可为空表示调用方已校验）
     */
    private String verifyCode;
}
