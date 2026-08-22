package com.jingxiang.component.user.manager.common.model.tenant;

import com.jingxiang.component.user.manager.common.dict.TenantTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 创建租户
 *
 * @author chenjw
 */
@Getter
@Setter
public class UserTenantCreate {

    /**
     * 组织类型
     */
    @NotNull(message = "组织类型不能为空")
    private TenantTypeEnum tenantType;

    /**
     * 组织编码
     */
    @NotBlank(message = "组织编码不能为空")
    @Size(max = 64)
    private String tenantCode;

    /**
     * 组织名称
     */
    @NotBlank(message = "组织名称不能为空")
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
