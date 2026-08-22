package com.jingxiang.component.user.admin.merchant.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 创建空间用户。
 *
 * @author chenjw
 */
@Getter
@Setter
public class SpaceUserCreate {

    /** 用户名。 */
    @NotBlank(message = "用户名不能为空")
    @Size(max = 64, message = "用户名长度不能超过64")
    private String username;

    /** 密码。 */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 64, message = "密码长度应为6-64位")
    private String password;

    /** 昵称。 */
    @Size(max = 64, message = "昵称长度不能超过64")
    private String nickname;

    /** 手机号。 */
    @Size(max = 20, message = "手机号长度不能超过20")
    private String mobile;

    /** 邮箱。 */
    @Size(max = 128, message = "邮箱长度不能超过128")
    private String email;

    /** 备注。 */
    @Size(max = 500, message = "备注长度不能超过500")
    private String remark;
}
