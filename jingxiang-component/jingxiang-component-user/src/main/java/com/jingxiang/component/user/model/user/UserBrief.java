package com.jingxiang.component.user.model.user;

import com.jingxiang.component.common.dict.convert.DictSerialize;
import com.jingxiang.component.user.dict.GenderEnum;
import com.jingxiang.commons.model.dict.WhetherDict;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 用户列表
 *
 * @author chenjw
 */
@Getter
@Setter
public class UserBrief {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 手机号
     */
    private String tel;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 性别
     */
    @DictSerialize(GenderEnum.class)
    private GenderEnum gender;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 是否禁用
     */
    @DictSerialize(WhetherDict.class)
    private Integer forbidden;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
