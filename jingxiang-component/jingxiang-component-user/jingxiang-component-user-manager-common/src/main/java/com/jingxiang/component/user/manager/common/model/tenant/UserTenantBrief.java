package com.jingxiang.component.user.manager.common.model.tenant;

import com.jingxiang.component.common.dict.convert.DictSerialize;
import com.jingxiang.component.user.manager.common.dict.TenantTypeEnum;
import com.jingxiang.commons.model.dict.WhetherDict;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 租户列表/详情
 *
 * @author chenjw
 */
@Getter
@Setter
public class UserTenantBrief {

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 组织类型
     */
    @DictSerialize(TenantTypeEnum.class)
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
     * 是否禁用
     */
    @DictSerialize(WhetherDict.class)
    private Integer forbidden;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 备注
     */
    private String remark;
}
