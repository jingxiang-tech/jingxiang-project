package com.jingxiang.component.user.admin.model.tenant;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 修改租户
 *
 * @author chenjw
 */
@Getter
@Setter
public class UserTenantUpdate {

    /**
     * 租户ID
     */
    @NotNull(message = "tenantId不能为空")
    private Long tenantId;

    /**
     * 组织名称
     */
    @Size(max = 128)
    private String tenantName;

    /**
     * 上级组织ID
     */
    private Long parentId;

    /**
     * 组织Logo
     */
    @Size(max = 512)
    private String logo;

    /**
     * 备注
     */
    @Size(max = 255)
    private String remark;
}
