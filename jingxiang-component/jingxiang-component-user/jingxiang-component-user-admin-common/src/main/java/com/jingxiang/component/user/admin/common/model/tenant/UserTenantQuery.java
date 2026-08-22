package com.jingxiang.component.user.admin.common.model.tenant;

import com.jingxiang.commons.model.dto.BasePage;
import com.jingxiang.component.user.admin.common.dict.TenantTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 租户分页查询
 *
 * @author chenjw
 */
@Getter
@Setter
public class UserTenantQuery extends BasePage {

    /**
     * 组织类型
     */
    private TenantTypeEnum tenantType;

    /**
     * 组织编码
     */
    private String tenantCode;

    /**
     * 组织名称（模糊）
     */
    private String tenantName;

    /**
     * 上级组织ID
     */
    private Long parentId;

    /**
     * 是否禁用
     */
    private Integer forbidden;
}
