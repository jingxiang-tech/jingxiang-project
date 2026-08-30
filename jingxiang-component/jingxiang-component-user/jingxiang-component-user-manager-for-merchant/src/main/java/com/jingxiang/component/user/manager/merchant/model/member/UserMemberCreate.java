package com.jingxiang.component.user.manager.merchant.model.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 创建组织成员（同时创建用户与 user_member 关系）
 *
 * @author chenjw
 */
@Getter
@Setter
public class UserMemberCreate {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Size(max = 64, message = "用户名长度不能超过64位")
    private String userName;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 64, message = "密码长度应为6-64位")
    private String password;

    /**
     * 昵称
     */
    @Size(max = 64, message = "昵称长度不能超过64位")
    private String nickname;

    /**
     * 手机号
     */
    @Size(max = 20, message = "手机号长度不能超过20位")
    private String tel;

    /**
     * 邮箱
     */
    @Size(max = 128, message = "邮箱长度不能超过128位")
    private String email;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过500位")
    private String remark;

    /**
     * 角色编码列表，不传则默认为运营
     */
    private List<String> roleCodeList;
}
