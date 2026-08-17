package com.jingxiang.component.user.admin.model.tenant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jingxiang.component.user.admin.dict.TenantTypeEnum;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 用户体系租户/组织实体
 *
 * 表：user_tenant
 *
 * @author chenjw
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@TableName("user_tenant")
public class UserTenantPo {

    /**
     * 租户/组织ID
     */
    @TableId(value = "tenant_id", type = IdType.AUTO)
    private Long tenantId;

    /**
     * 组织类型
     */
    private TenantTypeEnum tenantType;

    /**
     * 组织编码
     */
    private String tenantCode;

    /**
     * 组织名称
     */
    private String tenantName;

    /**
     * 上级组织ID
     */
    private Long parentId;

    /**
     * 组织Logo
     */
    private String logo;

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
