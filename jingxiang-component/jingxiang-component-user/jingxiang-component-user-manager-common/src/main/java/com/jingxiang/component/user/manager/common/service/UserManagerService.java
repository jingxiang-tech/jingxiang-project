package com.jingxiang.component.user.manager.common.service;

import com.jingxiang.commons.model.dto.Page;
import com.jingxiang.commons.model.dto.R;
import com.jingxiang.component.user.model.user.UserBrief;
import com.jingxiang.component.user.model.user.UserQuery;

/**
 * 用户管理端服务（列表/删除/禁用）
 *
 * @author chenjw
 */
public interface UserManagerService {

    /**
     * 用户分页
     */
    R<Page<UserBrief>> list(UserQuery query);

    /**
     * 逻辑删除
     */
    R<?> delete(Long userId);

    /**
     * 禁用/启用
     */
    R<?> setForbidden(Long userId, boolean forbidden);
}
