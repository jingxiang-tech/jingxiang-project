package com.jingxiang.component.user.admin.merchant.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * 商户后台用户创建参数。
 *
 * @author chenjw
 */
@Getter
@Setter
public class SystemUserInsert {

    /** 账户名。 */
    @NotNull(message = "账户名不能为空")
    @Length(max = 32, message = "账户名长度不能超过32位")
    private String userName;

    /** 真实姓名。 */
    @Length(max = 32, message = "真实姓名长度不能超过32位")
    private String realName;

    /** 手机号。 */
    @NotNull(message = "手机号不能为空")
    @Length(max = 11, message = "手机号长度不能超过11位")
    private String tel;

    /** 密码。 */
    @NotNull(message = "密码不能为空")
    @Length(max = 128, message = "密码长度不能超过128位")
    private String pwd;

    /** 邮箱。 */
    @Length(max = 255, message = "邮箱长度不能超过255位")
    private String email;

    /** 角色列表。 */
    @NotNull(message = "角色列表不能为空")
    private List<String> role;

    /** 关联空间 ID，不传时使用当前空间。 */
    private List<Integer> spaceIds;
}
