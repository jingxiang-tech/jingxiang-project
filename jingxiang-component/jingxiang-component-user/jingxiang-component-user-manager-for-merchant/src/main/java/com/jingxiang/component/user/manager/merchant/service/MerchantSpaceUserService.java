package com.jingxiang.component.user.manager.merchant.service;

import com.jingxiang.commons.model.dto.R;
import com.jingxiang.component.user.manager.merchant.model.MerchantSpaceUserBrief;
import com.jingxiang.component.user.manager.merchant.model.MerchantSpaceUserCreate;
import com.jingxiang.component.user.manager.merchant.model.MerchantSpaceUserDisabledUpdate;
import com.jingxiang.component.user.manager.merchant.model.MerchantSpaceUserPasswordUpdate;
import com.jingxiang.component.user.manager.merchant.model.MerchantSpaceUserUsernameExistsBrief;

import java.util.List;

/**
 * 商户后台空间用户服务。
 *
 * @author chenjw
 */
public interface MerchantSpaceUserService {

    R<String> create(MerchantSpaceUserCreate request);

    R<MerchantSpaceUserUsernameExistsBrief> usernameExists(String username);

    R<List<MerchantSpaceUserBrief>> list();

    R<String> updateDisabled(MerchantSpaceUserDisabledUpdate request);

    R<String> resetPassword(MerchantSpaceUserPasswordUpdate request);
}
