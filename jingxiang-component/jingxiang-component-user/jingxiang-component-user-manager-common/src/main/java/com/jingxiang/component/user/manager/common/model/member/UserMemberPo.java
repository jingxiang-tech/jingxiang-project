package com.jingxiang.component.user.manager.common.model.member;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jingxiang.commons.util.convert.ListTypeHandler;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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
@TableName(value = "user_member", autoResultMap = true)
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
     * 角色编码列表
     */
    @TableField(typeHandler = ListTypeHandler.class)
    private List<String> roleCodeList;

    /**
     * 角色名称
     */
    private String roleNameDesc;

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
