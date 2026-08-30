package com.jingxiang.component.user.manager.merchant.service.impl;

import com.jingxiang.commons.model.dict.WhetherDict;
import com.jingxiang.commons.model.dto.Dict;
import com.jingxiang.commons.model.dto.R;
import com.jingxiang.component.user.jwt.SessionUtil;
import com.jingxiang.component.user.manager.common.dict.MemberRoleEnum;
import com.jingxiang.component.user.manager.common.model.member.UserMemberPo;
import com.jingxiang.component.user.manager.common.model.member.UserMemberQuery;
import com.jingxiang.component.user.manager.common.model.member.UserMemberUpdate;
import com.jingxiang.component.user.manager.common.service.UserManagerService;
import com.jingxiang.component.user.manager.common.service.UserMemberService;
import com.jingxiang.component.user.manager.merchant.model.member.UserMemberBrief;
import com.jingxiang.component.user.manager.merchant.model.member.UserMemberCreate;
import com.jingxiang.component.user.manager.merchant.model.member.UserMemberDisabledUpdate;
import com.jingxiang.component.user.manager.merchant.model.member.UserMemberPasswordUpdate;
import com.jingxiang.component.user.manager.merchant.model.member.UserMemberRolesUpdate;
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

import java.util.Arrays;
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
        List<String> roleCodeList = resolveCreateRoleCodes(request.getRoleCodeList());
        R<String> roleCheck = validateAssignableRoles(roleCodeList, null);
        if (roleCheck != null) {
            return roleCheck;
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
        memberCreate.setRoleCodeList(roleCodeList);
        memberCreate.setRoleNameDesc(MemberRoleEnum.namesOf(roleCodeList));
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
        UserMemberPo member = requireTenantMember(request.getUserId());
        if (member == null) {
            return R.fail("更新失败，目标成员可能不存在");
        }
        R<String> ownerCheck = validateOwnerOperation(member, Objects.equals(request.getDisabled(), 1));
        if (ownerCheck != null) {
            return ownerCheck;
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
        UserMemberPo member = requireTenantMember(request.getUserId());
        if (member == null) {
            return R.fail("重置失败，目标成员可能不存在");
        }
        if (MemberRoleEnum.contains(member.getRoleCodeList(), MemberRoleEnum.OWNER)
                && !SessionUtil.hasRole(MemberRoleEnum.OWNER.code)) {
            return R.fail("仅所有者可重置所有者账号密码");
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

    @Override
    public R<String> updateRoles(UserMemberRolesUpdate request) {
        if (!isAdminUser()) {
            return R.fail("仅管理员可修改组织成员角色");
        }
        if (Objects.equals(request.getUserId(), SessionUtil.getUserId())) {
            return R.fail("不能修改当前登录账号角色");
        }
        UserMemberPo member = requireTenantMember(request.getUserId());
        if (member == null) {
            return R.fail("更新失败，目标成员可能不存在");
        }
        List<String> roleCodeList = MemberRoleEnum.normalizeCodes(request.getRoleCodeList());
        if (roleCodeList.isEmpty()) {
            return R.fail("角色编码无效");
        }
        R<String> roleCheck = validateAssignableRoles(roleCodeList, member);
        if (roleCheck != null) {
            return roleCheck;
        }
        UserMemberUpdate update = new UserMemberUpdate();
        update.setMemberId(member.getMemberId());
        update.setRoleCodeList(roleCodeList);
        update.setRoleNameDesc(MemberRoleEnum.namesOf(roleCodeList));
        R<?> result = userMemberService.update(update);
        if (result.failed()) {
            return R.fail(result.getMsg() != null ? result.getMsg() : "更新失败");
        }
        return R.ok("角色已更新");
    }

    @Override
    public R<List<Dict>> roleDict() {
        if (!isAdminUser()) {
            return R.fail("仅管理员可查看角色字典");
        }
        List<Dict> dicts = Arrays.stream(MemberRoleEnum.values())
                .map(item -> new Dict(item.code, item.name))
                .toList();
        return R.ok(dicts);
    }

    private boolean isAdminUser() {
        SessionUtil.getSessionUser();
        return SessionUtil.hasRole(MemberRoleEnum.ADMIN.code)
                || SessionUtil.hasRole(MemberRoleEnum.OWNER.code);
    }

    /**
     * 创建时解析角色：未传则默认运营
     */
    private List<String> resolveCreateRoleCodes(List<String> requested) {
        if (requested == null || requested.isEmpty()) {
            return List.of(MemberRoleEnum.OPERATOR.code);
        }
        return MemberRoleEnum.normalizeCodes(requested);
    }

    /**
     * 校验可分配角色、特权授予与最后一个所有者
     */
    private R<String> validateAssignableRoles(List<String> roleCodeList, UserMemberPo currentMember) {
        if (roleCodeList == null || roleCodeList.isEmpty()) {
            return R.fail("角色编码无效");
        }
        boolean grantingPrivileged = MemberRoleEnum.contains(roleCodeList, MemberRoleEnum.OWNER)
                || MemberRoleEnum.contains(roleCodeList, MemberRoleEnum.ADMIN);
        if (grantingPrivileged && !SessionUtil.hasRole(MemberRoleEnum.OWNER.code)) {
            return R.fail("仅所有者可授予管理员或所有者角色");
        }
        if (currentMember != null
                && MemberRoleEnum.contains(currentMember.getRoleCodeList(), MemberRoleEnum.OWNER)
                && !SessionUtil.hasRole(MemberRoleEnum.OWNER.code)) {
            return R.fail("仅所有者可修改所有者账号");
        }
        boolean removingOwner = currentMember != null
                && MemberRoleEnum.contains(currentMember.getRoleCodeList(), MemberRoleEnum.OWNER)
                && !MemberRoleEnum.contains(roleCodeList, MemberRoleEnum.OWNER);
        if (removingOwner && countEnabledOwners() <= 1) {
            return R.fail("不能移除最后一个所有者");
        }
        return null;
    }

    private R<String> validateOwnerOperation(UserMemberPo member, boolean disabling) {
        if (!MemberRoleEnum.contains(member.getRoleCodeList(), MemberRoleEnum.OWNER)) {
            return null;
        }
        if (!SessionUtil.hasRole(MemberRoleEnum.OWNER.code)) {
            return R.fail("仅所有者可修改所有者账号状态");
        }
        if (disabling && countEnabledOwners() <= 1) {
            return R.fail("不能禁用最后一个所有者");
        }
        return null;
    }

    private int countEnabledOwners() {
        UserMemberQuery query = new UserMemberQuery();
        query.setTenantId(SessionUtil.getTenantId());
        query.setRoleCode(MemberRoleEnum.OWNER.code);
        R<List<com.jingxiang.component.user.manager.common.model.member.UserMemberBrief>> result =
                userMemberService.listAll(query);
        if (result.failed() || result.getData() == null) {
            return 0;
        }
        int count = 0;
        for (com.jingxiang.component.user.manager.common.model.member.UserMemberBrief item : result.getData()) {
            if (!Objects.equals(item.getUserForbidden(), WhetherDict.Yes.code)
                    && !Objects.equals(item.getForbidden(), WhetherDict.Yes.code)) {
                count++;
            }
        }
        return count;
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
     * 校验目标成员属于当前租户
     */
    private UserMemberPo requireTenantMember(Integer userId) {
        if (userId == null) {
            return null;
        }
        UserMemberPo member = userMemberService.getByUserAndTenant(
                userId.longValue(), SessionUtil.getTenantId());
        if (member == null || WhetherDict.Yes.code == member.getForbidden()) {
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
