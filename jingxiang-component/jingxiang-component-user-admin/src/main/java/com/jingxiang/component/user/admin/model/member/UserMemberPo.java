package com.jingxiang.component.user.admin.model.member;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jingxiang.component.user.admin.dict.MemberTypeEnum;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 用户组织成员关系实体
 *
 * 表：user_member
 *
 * @author chenjw
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@TableName("user_member")
public class UserMemberPo {

    /**
     * 成员ID
     */
    @TableId(value = "member_id", type = IdType.AUTO)
    private Long memberId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 租户/组织ID
     */
    private Long tenantId;

    /**
     * 成员类型
     */
    private MemberTypeEnum memberType;

    /**
     * 组织内显示名称
     */
    private String memberName;

    /**
     * 是否禁用：0否 1是
     */
    private Integer forbidden;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 逻辑删除：0否 1是
     */
    private Integer deleted;

    /**
     * 备注
     */
    private String remark;
}
