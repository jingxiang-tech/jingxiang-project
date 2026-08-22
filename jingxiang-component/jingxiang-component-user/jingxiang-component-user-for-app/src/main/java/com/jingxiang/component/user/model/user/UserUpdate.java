package com.jingxiang.component.user.model.user;

import com.jingxiang.component.user.dict.GenderEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 修改用户（不含密码/禁用/删除）
 *
 * @author chenjw
 */
@Getter
@Setter
public class UserUpdate {

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 手机号
     */
    @Size(max = 32, message = "手机号最长32位")
    private String tel;

    /**
     * 用户名
     */
    @Size(max = 32, message = "用户名最长32位")
    private String userName;

    /**
     * 昵称
     */
    @Size(max = 32, message = "昵称最长32位")
    private String nickname;

    /**
     * 真实姓名
     */
    @Size(max = 32, message = "真实姓名最长32位")
    private String realName;

    /**
     * 性别
     */
    private GenderEnum gender;

    /**
     * 头像
     */
    @Size(max = 512, message = "头像URL过长")
    private String avatar;

    /**
     * 邮箱
     */
    @Size(max = 128, message = "邮箱最长128位")
    private String email;

    /**
     * 备注
     */
    @Size(max = 255, message = "备注最长255位")
    private String remark;
}
