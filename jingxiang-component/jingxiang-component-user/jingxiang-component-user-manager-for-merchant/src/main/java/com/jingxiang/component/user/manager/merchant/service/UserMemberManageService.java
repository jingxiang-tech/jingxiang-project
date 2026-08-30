package com.jingxiang.component.user.manager.merchant.service;

import com.jingxiang.commons.model.dto.Dict;
import com.jingxiang.commons.model.dto.R;
import com.jingxiang.component.user.manager.merchant.model.member.UserMemberBrief;
import com.jingxiang.component.user.manager.merchant.model.member.UserMemberCreate;
import com.jingxiang.component.user.manager.merchant.model.member.UserMemberDisabledUpdate;
import com.jingxiang.component.user.manager.merchant.model.member.UserMemberPasswordUpdate;
import com.jingxiang.component.user.manager.merchant.model.member.UserMemberRolesUpdate;
import com.jingxiang.component.user.manager.merchant.model.member.UserMemberUsernameExistsBrief;

import java.util.List;

/**
 * 组织成员编排服务（创建 user + user_member）
 *
 * @author chenjw
 */
public interface UserMemberManageService {

    R<String> create(UserMemberCreate request);

    R<UserMemberUsernameExistsBrief> usernameExists(String userName);

    R<List<UserMemberBrief>> list();

    R<String> updateDisabled(UserMemberDisabledUpdate request);

    R<String> resetPassword(UserMemberPasswordUpdate request);

    R<String> updateRoles(UserMemberRolesUpdate request);

    R<List<Dict>> roleDict();
}
