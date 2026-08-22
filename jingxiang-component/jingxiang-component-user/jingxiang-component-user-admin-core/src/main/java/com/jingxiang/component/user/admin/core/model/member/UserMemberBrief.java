package com.jingxiang.component.user.admin.core.model.member;

import com.jingxiang.component.common.dict.convert.DictSerialize;
import com.jingxiang.component.user.admin.core.dict.MemberTypeEnum;
import com.jingxiang.commons.model.dict.WhetherDict;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 成员列表
 *
 * @author chenjw
 */
@Getter
@Setter
public class UserMemberBrief {

    /**
     * 成员ID
     */
    private Long memberId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 成员类型
     */
    @DictSerialize(MemberTypeEnum.class)
    private MemberTypeEnum memberType;

    /**
     * 组织内显示名称
     */
    private String memberName;

    /**
     * 用户昵称（联表）
     */
    private String nickname;

    /**
     * 用户名（联表）
     */
    private String userName;

    /**
     * 真实姓名（联表）
     */
    private String realName;

    /**
     * 头像（联表）
     */
    private String avatar;

    /**
     * 邮箱（联表）
     */
    private String email;

    /**
     * 用户手机号（联表）
     */
    private String tel;

    /**
     * 组织名称（联表）
     */
    private String tenantName;

    /**
     * 组织编码（联表）
     */
    private String tenantCode;

    /**
     * 用户是否禁用（联表）
     */
    @DictSerialize(WhetherDict.class)
    private Integer userForbidden;

    /**
     * 用户备注（联表）
     */
    private String userRemark;

    /**
     * 用户创建时间（联表）
     */
    private LocalDateTime userCreatedAt;

    /**
     * 用户更新时间（联表）
     */
    private LocalDateTime userUpdatedAt;

    /**
     * 成员是否禁用
     */
    @DictSerialize(WhetherDict.class)
    private Integer forbidden;

    /**
     * 成员备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
