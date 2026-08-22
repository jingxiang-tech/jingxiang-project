package com.jingxiang.component.user.manager.merchant.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 商户后台用户修改参数。
 *
 * @author chenjw
 */
@Getter
@Setter
public class SystemUserUpdate {

    /** 用户 ID。 */
    @NotNull(message = "用户ID不能为空")
    private Integer userId;

    /** 账户名。 */
    private String userName;

    /** 昵称。 */
    private String nickname;

    /** 真实姓名。 */
    private String realName;

    /** 手机号。 */
    private String tel;

    /** 头像。 */
    private String avatar;

    /** 邮箱。 */
    private String email;

    /** 备注。 */
    private String remark;

    /** 角色列表。 */
    private List<String> role;
}
