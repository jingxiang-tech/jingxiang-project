package com.jingxiang.component.user.manager.common.service;

import com.jingxiang.commons.model.dto.Page;
import com.jingxiang.commons.model.dto.R;
import com.jingxiang.component.user.manager.common.model.member.*;

import java.util.List;

/**
 * 用户组织成员服务
 *
 * @author chenjw
 */
public interface UserMemberService {

    /**
     * 添加成员
     */
    R<Long> add(UserMemberCreate create);

    /**
     * 移除成员（逻辑删除）
     */
    R<?> remove(Long memberId);

    /**
     * 禁用/启用
     */
    R<?> setForbidden(Long memberId, boolean forbidden);

    /**
     * 修改成员角色
     */
    R<?> update(UserMemberUpdate update);

    /**
     * 成员详情
     */
    R<UserMemberBrief> detail(Long memberId);

    /**
     * 租户成员分页
     */
    R<Page<UserMemberBrief>> list(UserMemberQuery query);

    /**
     * 租户成员列表（不分页）
     */
    R<List<UserMemberBrief>> listAll(UserMemberQuery query);

    /**
     * 查询用户所属租户成员关系
     */
    R<List<UserMemberBrief>> listTenantsByUserId(Long userId);

    /**
     * 查询用户在指定租户下的成员
     */
    UserMemberPo getByUserAndTenant(Long userId, Long tenantId);

    /**
     * 判断用户是否属于指定租户（未删除且未禁用）
     */
    boolean belongsTo(Long userId, Long tenantId);
}
