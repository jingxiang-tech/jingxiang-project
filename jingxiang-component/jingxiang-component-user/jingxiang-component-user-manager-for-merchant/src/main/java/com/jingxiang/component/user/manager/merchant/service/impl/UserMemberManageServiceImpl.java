package com.jingxiang.component.user.manager.merchant.service.impl;

import com.jingxiang.commons.model.dict.WhetherDict;
import com.jingxiang.commons.model.dto.R;
import com.jingxiang.component.user.jwt.SessionUtil;
import com.jingxiang.component.user.manager.common.dict.MemberTypeEnum;
import com.jingxiang.component.user.manager.common.model.member.UserMemberPo;
import com.jingxiang.component.user.manager.common.model.member.UserMemberQuery;
import com.jingxiang.component.user.manager.common.service.UserManagerService;
import com.jingxiang.component.user.manager.common.service.UserMemberService;
import com.jingxiang.component.user.manager.merchant.model.member.UserMemberBrief;
import com.jingxiang.component.user.manager.merchant.model.member.UserMemberCreate;
import com.jingxiang.component.user.manager.merchant.model.member.UserMemberDisabledUpdate;
import com.jingxiang.component.user.manager.merchant.model.member.UserMemberPasswordUpdate;
import com.jingxiang.component.user.manager.merchant.model.member.UserMemberUsernameExistsBrief;
import com.jingxiang.component.user.manager.merchant.service.UserMemberManageService;
import com.jingxiang.component.user.model.user.UserCreate;
import com.jingxiang.component.user.model.user.UserPasswordUpdate;
import com.jingxiang.component.user.model.user.UserPo;
import com.jingxiang.component.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 组织成员编排服务实现
 *
 * @author chenjw
 */
@Service
public class UserMemberManageServiceImpl implements UserMemberManageService {

    private static final Logger log = LoggerFactory.getLogger(UserMemberManageServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserManagerService userManagerService;

    @Autowired
    private UserMemberService userMemberService;

    @Override
    public R<String> create(UserMemberCreate request) {
        if (!isAdminUser()) {
            return R.fail("仅管理员可创建组织成员");
        }
        String userName = trimToNull(request.getUserName());
        String password = trimToNull(request.getPassword());
        if (userName == null || password == null) {
            return R.fail("用户名或密码不能为空");
        }
        if (userService.getByUserName(userName) != null) {
            return R.fail("创建失败，用户名可能已存在");
        }

        UserCreate create = new UserCreate();
        create.setUserName(userName);
        create.setPassword(password);
        create.setNickname(trimToNull(request.getNickname()));
        create.setTel(trimToNull(request.getTel()));
        create.setEmail(trimToNull(request.getEmail()));
        create.setRemark(trimToNull(request.getRemark()));
        R<Long> createResult = userService.create(create);
        if (createResult.failed() || createResult.getData() == null) {
            return R.fail(createResult.getMsg() != null ? createResult.getMsg() : "创建失败，用户名可能已存在");
        }

        com.jingxiang.component.user.manager.common.model.member.UserMemberCreate memberCreate =
                new com.jingxiang.component.user.manager.common.model.member.UserMemberCreate();
        memberCreate.setUserId(createResult.getData());
        memberCreate.setTenantId(SessionUtil.getTenantId());
        memberCreate.setRoleCodeList(List.of(MemberTypeEnum.OPERATOR.code));
        memberCreate.setRoleNameDesc(MemberTypeEnum.OPERATOR.name);
        memberCreate.setRemark(String.valueOf(SessionUtil.getUserId()));
        R<Long> memberResult = userMemberService.add(memberCreate);
        if (memberResult.failed()) {
            compensateNewUser(createResult.getData());
            return R.fail(memberResult.getMsg() != null ? memberResult.getMsg() : "创建失败");
        }
        return R.ok("创建成功");
    }

    @Override
    public R<UserMemberUsernameExistsBrief> usernameExists(String userName) {
        if (!isAdminUser()) {
            return R.fail("仅管理员可校验账号");
        }
        UserMemberUsernameExistsBrief brief = new UserMemberUsernameExistsBrief();
        String validUsername = trimToNull(userName);
        brief.setExists(validUsername != null && userService.getByUserName(validUsername) != null);
        return R.ok(brief);
    }

    @Override
    public R<List<UserMemberBrief>> list() {
        if (!isAdminUser()) {
            return R.fail("仅管理员可查看组织成员列表");
        }
        UserMemberQuery query = new UserMemberQuery();
        query.setTenantId(SessionUtil.getTenantId());
        query.setRoleCode(MemberTypeEnum.OPERATOR.code);
        R<List<com.jingxiang.component.user.manager.common.model.member.UserMemberBrief>> result =
                userMemberService.listAll(query);
        if (result.failed() || result.getData() == null) {
            return R.ok(Collections.emptyList());
        }
        return R.ok(result.getData().stream().map(this::toBrief).toList());
    }

    @Override
    public R<String> updateDisabled(UserMemberDisabledUpdate request) {
        if (!isAdminUser()) {
            return R.fail("仅管理员可修改组织成员状态");
        }
        if (Objects.equals(request.getUserId(), SessionUtil.getUserId())) {
            return R.fail("不能修改当前登录账号状态");
        }
        UserMemberPo member = requireTenantOperator(request.getUserId());
        if (member == null) {
            return R.fail("更新失败，目标成员可能不存在");
        }
        R<?> result = userManagerService.setForbidden(
                request.getUserId().longValue(), Objects.equals(request.getDisabled(), 1));
        if (result.failed()) {
            return R.fail(result.getMsg() != null ? result.getMsg() : "更新失败，目标成员可能不存在");
        }
        return R.ok("更新成功");
    }

    @Override
    public R<String> resetPassword(UserMemberPasswordUpdate request) {
        if (!isAdminUser()) {
            return R.fail("仅管理员可重置组织成员密码");
        }
        String newPassword = trimToNull(request.getNewPassword());
        if (newPassword == null) {
            return R.fail("新密码不能为空");
        }
        if (Objects.equals(request.getUserId(), SessionUtil.getUserId())) {
            return R.fail("请使用账户设置修改当前登录账号密码");
        }
        UserMemberPo member = requireTenantOperator(request.getUserId());
        if (member == null) {
            return R.fail("重置失败，目标成员可能不存在");
        }
        UserPasswordUpdate update = new UserPasswordUpdate();
        update.setUserId(request.getUserId().longValue());
        update.setNewPassword(newPassword);
        R<?> result = userService.changePassword(update);
        if (result.failed()) {
            return R.fail(result.getMsg() != null ? result.getMsg() : "重置失败，目标成员可能不存在");
        }
        return R.ok("重置成功");
    }

    private boolean isAdminUser() {
        SessionUtil.getSessionUser();
        return SessionUtil.hasRole(MemberTypeEnum.ADMIN.code)
                || SessionUtil.hasRole(MemberTypeEnum.OWNER.code);
    }

    private UserMemberBrief toBrief(
            com.jingxiang.component.user.manager.common.model.member.UserMemberBrief member) {
        UserMemberBrief brief = new UserMemberBrief();
        brief.setMemberId(member.getMemberId());
        brief.setUserId(member.getUserId());
        brief.setUserName(member.getUserName());
        brief.setNickname(member.getNickname());
        brief.setTel(member.getTel());
        brief.setEmail(member.getEmail());
        brief.setRoleCodeList(member.getRoleCodeList());
        brief.setRoleNameDesc(member.getRoleNameDesc());
        brief.setDisabled(member.getUserForbidden());
        brief.setCreateTime(member.getCreatedAt());
        String remark = resolveCreatorRemark(member);
        if (StringUtils.hasText(remark)) {
            try {
                brief.setCreatorUserId(Long.parseLong(remark));
            } catch (NumberFormatException ignored) {
                brief.setCreatorUserId(null);
            }
        }
        return brief;
    }

    private String resolveCreatorRemark(
            com.jingxiang.component.user.manager.common.model.member.UserMemberBrief member) {
        if (StringUtils.hasText(member.getRemark())) {
            return member.getRemark();
        }
        UserMemberPo po = userMemberService.getByUserAndTenant(member.getUserId(), member.getTenantId());
        return po == null ? null : po.getRemark();
    }

    /**
     * 校验目标成员属于当前租户，且角色为 OPERATOR
     */
    private UserMemberPo requireTenantOperator(Integer userId) {
        if (userId == null) {
            return null;
        }
        UserMemberPo member = userMemberService.getByUserAndTenant(
                userId.longValue(), SessionUtil.getTenantId());
        if (member == null || WhetherDict.Yes.code == member.getForbidden()) {
            return null;
        }
        if (!MemberTypeEnum.contains(member.getRoleCodeList(), MemberTypeEnum.OPERATOR)) {
            return null;
        }
        UserPo user = userService.getById(userId.longValue());
        if (user == null || WhetherDict.Yes.code == user.getDeleted()) {
            return null;
        }
        return member;
    }

    private void compensateNewUser(Long userId) {
        try {
            userManagerService.delete(userId);
        } catch (RuntimeException e) {
            log.error("补偿新建组织成员失败，userId={}", userId, e);
        }
    }

    private String trimToNull(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }
}
