package com.jingxiang.component.user.manager.merchant.model.member;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 组织成员列表项
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
     * 用户名
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 手机号
     */
    private String tel;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 角色编码列表
     */
    private List<String> roleCodeList;

    /**
     * 角色名称
     */
    private String roleNameDesc;

    /**
     * 是否禁用
     */
    private Integer disabled;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人用户ID
     */
    private Long creatorUserId;
}
