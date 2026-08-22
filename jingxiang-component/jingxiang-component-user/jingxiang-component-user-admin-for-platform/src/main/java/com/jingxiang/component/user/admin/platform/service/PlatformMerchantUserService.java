package com.jingxiang.component.user.admin.platform.service;

import com.jingxiang.commons.model.dto.R;
import com.jingxiang.component.user.admin.platform.model.*;

import java.util.List;

/**
 * 平台端商户用户管理服务。
 */
public interface PlatformMerchantUserService {

    R<List<MerchantUserBrief>> list(MerchantUserQuery query);

    R<MerchantUserDetail> get(Integer userId);

    R<Void> create(MerchantUserCreate create);

    R<Void> update(MerchantUserUpdate update);

    R<Void> delete(Integer userId);

    R<Void> resetPassword(Integer userId);

    R<Void> setDisabled(Integer userId, Integer disabled);

    R<List<Integer>> getSpaceIds(Integer userId);

    R<Void> putSpaceIds(Integer userId, List<Integer> spaceIds);

    R<List<MerchantUserBrief>> listBySpaceId(Integer spaceId);
}
