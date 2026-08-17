package com.jingxiang.component.user.admin.service;

import com.jingxiang.commons.model.dto.Page;
import com.jingxiang.commons.model.dto.R;
import com.jingxiang.component.user.admin.dict.TenantTypeEnum;
import com.jingxiang.component.user.admin.model.tenant.*;

import java.util.List;

/**
 * 租户/组织服务
 *
 * @author chenjw
 */
public interface UserTenantService {

    /**
     * 创建租户
     */
    R<Long> create(UserTenantCreate create);

    /**
     * 按编码获取，不存在则创建
     */
    Long ensureByCode(TenantTypeEnum tenantType, String tenantCode, String tenantName);

    /**
     * 修改租户
     */
    R<?> update(UserTenantUpdate update);

    /**
     * 详情
     */
    R<UserTenantBrief> detail(Long tenantId);

    /**
     * 分页
     */
    R<Page<UserTenantBrief>> list(UserTenantQuery query);

    /**
     * 禁用/启用
     */
    R<?> setForbidden(Long tenantId, boolean forbidden);

    /**
     * 逻辑删除
     */
    R<?> delete(Long tenantId);

    /**
     * 按编码查询
     */
    UserTenantBrief getByCode(String tenantCode);

    /**
     * 按 ID 查询可用租户
     */
    UserTenantPo getAvailableById(Long tenantId);

    /**
     * 查询子租户
     */
    R<List<UserTenantBrief>> listChildren(Long parentId);
}
