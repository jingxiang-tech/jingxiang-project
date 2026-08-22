package com.jingxiang.component.user.admin.merchant.service;

import com.jingxiang.commons.model.dto.Dict;
import com.jingxiang.commons.model.dto.Page;
import com.jingxiang.commons.model.dto.R;
import com.jingxiang.component.user.admin.core.model.member.UserMemberBrief;
import com.jingxiang.component.user.admin.merchant.model.*;

import java.util.List;

/**
 * 商户后台用户服务。
 *
 * @author chenjw
 */
public interface SystemUserService {

    R<SystemUserLoginDto> idPwdLogin(SystemUserLoginParam param);

    R<SystemUserInfo> info();

    R<?> markInitGuideDone();

    R<List<Dict>> dict(String keyword);

    R<?> insert(SystemUserInsert insert);

    R<?> delete(Integer userId);

    R<?> update(SystemUserUpdate update);

    R<UserMemberBrief> detail(Integer userId);

    R<Page<UserMemberBrief>> list(MerchantUserQuery query);

    R<?> updatePwd(SystemUserPwdUpdate update);

    R<?> resetPwd(Integer userId);

    R<String> switchSpace(Integer spaceId);

    R<List<SpaceOption>> spaceList();
}
