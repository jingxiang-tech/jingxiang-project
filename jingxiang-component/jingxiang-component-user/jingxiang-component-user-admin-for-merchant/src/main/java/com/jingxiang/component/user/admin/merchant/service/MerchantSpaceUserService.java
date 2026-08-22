package com.jingxiang.component.user.admin.merchant.service;

import com.jingxiang.commons.model.dto.R;
import com.jingxiang.component.user.admin.merchant.model.SpaceUserBrief;
import com.jingxiang.component.user.admin.merchant.model.SpaceUserCreate;
import com.jingxiang.component.user.admin.merchant.model.SpaceUserPasswordUpdate;
import com.jingxiang.component.user.admin.merchant.model.SpaceUserStatusUpdate;
import com.jingxiang.component.user.admin.merchant.model.UsernameExistsBrief;

import java.util.List;

/**
 * 商户后台空间用户服务。
 *
 * @author chenjw
 */
public interface MerchantSpaceUserService {

    R<String> create(SpaceUserCreate request);

    R<UsernameExistsBrief> usernameExists(String username);

    R<List<SpaceUserBrief>> list();

    R<String> updateStatus(SpaceUserStatusUpdate request);

    R<String> resetPassword(SpaceUserPasswordUpdate request);
}
