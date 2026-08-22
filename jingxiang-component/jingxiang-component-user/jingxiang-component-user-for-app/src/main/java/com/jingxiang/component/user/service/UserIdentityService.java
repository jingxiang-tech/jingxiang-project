package com.jingxiang.component.user.service;

import com.jingxiang.commons.model.dto.R;
import com.jingxiang.component.user.dict.IdentityTypeEnum;
import com.jingxiang.component.user.model.identity.UserIdentityBrief;
import com.jingxiang.component.user.model.identity.UserIdentityCreate;
import com.jingxiang.component.user.model.identity.UserIdentityPo;
import com.jingxiang.component.user.model.identity.UserIdentityUpdate;

import java.util.List;

/**
 * 用户第三方身份服务
 *
 * @author chenjw
 */
public interface UserIdentityService {

    /**
     * 按 identityId 查询
     */
    R<UserIdentityBrief> detail(Long identityId);

    /**
     * 查询某用户全部第三方身份
     */
    R<List<UserIdentityBrief>> listByUserId(Long userId);

    /**
     * 根据第三方身份查询 Identity
     */
    UserIdentityPo findByIdentity(IdentityTypeEnum identityType, String appKey, String identifier);

    /**
     * 绑定 Identity 到已有 User
     */
    R<Long> bind(UserIdentityCreate create);

    /**
     * 解绑（逻辑删除）
     */
    R<?> unbind(Long identityId);

    /**
     * 禁用/启用
     */
    R<?> setForbidden(Long identityId, boolean forbidden);

    /**
     * 更新昵称、头像、unionId
     */
    R<?> updateProfile(UserIdentityUpdate update);

    /**
     * 更新最近登录时间
     */
    R<?> touchLatestLogin(Long identityId);
}
