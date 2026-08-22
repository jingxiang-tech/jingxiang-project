package com.jingxiang.component.user.admin.merchant.service.impl;

import com.jingxiang.commons.model.dict.WhetherDict;
import com.jingxiang.commons.model.dto.R;
import com.jingxiang.component.user.admin.core.dict.MemberTypeEnum;
import com.jingxiang.component.user.admin.core.model.member.UserMemberBrief;
import com.jingxiang.component.user.admin.core.model.member.UserMemberCreate;
import com.jingxiang.component.user.admin.core.model.member.UserMemberPo;
import com.jingxiang.component.user.admin.core.model.member.UserMemberQuery;
import com.jingxiang.component.user.admin.core.service.UserAdminService;
import com.jingxiang.component.user.admin.core.service.UserMemberService;
import com.jingxiang.component.user.admin.merchant.model.SpaceUserBrief;
import com.jingxiang.component.user.admin.merchant.model.SpaceUserCreate;
import com.jingxiang.component.user.admin.merchant.model.SpaceUserPasswordUpdate;
import com.jingxiang.component.user.admin.merchant.model.SpaceUserStatusUpdate;
import com.jingxiang.component.user.admin.merchant.model.UsernameExistsBrief;
import com.jingxiang.component.user.admin.merchant.service.MerchantSpaceUserService;
import com.jingxiang.component.user.jwt.SessionUtil;
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
 * 商户后台空间用户服务实现。
 *
 * @author chenjw
 */
@Service
public class MerchantSpaceUserServiceImpl implements MerchantSpaceUserService {

    private static final Logger log = LoggerFactory.getLogger(MerchantSpaceUserServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserAdminService userAdminService;

    @Autowired
    private UserMemberService userMemberService;

    @Override
    public R<String> create(SpaceUserCreate request) {
        if (!SessionUtil.isSpaceAdmin()) {
            return R.fail("仅管理员可创建空间用户");
        }
        String username = trimToNull(request.getUsername());
        String password = trimToNull(request.getPassword());
        if (username == null || password == null) {
            return R.fail("用户名或密码不能为空");
        }
        if (userService.getByUserName(username) != null) {
            return R.fail("创建失败，用户名可能已存在");
        }

        UserCreate create = new UserCreate();
        create.setUserName(username);
        create.setPassword(password);
        create.setNickname(trimToNull(request.getNickname()));
        create.setTel(trimToNull(request.getMobile()));
        create.setEmail(trimToNull(request.getEmail()));
        create.setRemark(trimToNull(request.getRemark()));
        R<Long> createResult = userService.create(create);
        if (createResult.failed() || createResult.getData() == null) {
            return R.fail(createResult.getMsg() != null ? createResult.getMsg() : "创建失败，用户名可能已存在");
        }

        UserMemberCreate memberCreate = new UserMemberCreate();
        memberCreate.setUserId(createResult.getData());
        memberCreate.setTenantId(SessionUtil.getTenantId());
        memberCreate.setMemberType(MemberTypeEnum.OPERATOR);
        memberCreate.setMemberName(trimToNull(request.getNickname()));
        memberCreate.setRemark(String.valueOf(SessionUtil.getUserId()));
        R<Long> memberResult = userMemberService.add(memberCreate);
        if (memberResult.failed()) {
            compensateNewUser(createResult.getData());
            return R.fail(memberResult.getMsg() != null ? memberResult.getMsg() : "创建失败");
        }
        return R.ok("创建成功");
    }

    @Override
    public R<UsernameExistsBrief> usernameExists(String username) {
        if (!SessionUtil.isSpaceAdmin()) {
            return R.fail("仅管理员可校验账号");
        }
        UsernameExistsBrief brief = new UsernameExistsBrief();
        String validUsername = trimToNull(username);
        brief.setExists(validUsername != null && userService.getByUserName(validUsername) != null);
        return R.ok(brief);
    }

    @Override
    public R<List<SpaceUserBrief>> list() {
        if (!SessionUtil.isSpaceAdmin()) {
            return R.fail("仅管理员可查看空间用户");
        }
        Integer creatorUserId = SessionUtil.getUserId();
        UserMemberQuery query = new UserMemberQuery();
        query.setTenantId(SessionUtil.getTenantId());
        query.setMemberType(MemberTypeEnum.OPERATOR);
        R<List<UserMemberBrief>> result = userMemberService.listAll(query);
        if (result.failed() || result.getData() == null) {
            return R.ok(Collections.emptyList());
        }
        String creatorKey = String.valueOf(creatorUserId);
        List<SpaceUserBrief> list = result.getData().stream()
                .filter(item -> creatorKey.equals(resolveCreatorRemark(item)))
                .map(this::toBrief)
                .toList();
        return R.ok(list);
    }

    @Override
    public R<String> updateStatus(SpaceUserStatusUpdate request) {
        if (!SessionUtil.isSpaceAdmin()) {
            return R.fail("仅管理员可修改空间用户状态");
        }
        if (request.getStatus() != 0 && request.getStatus() != 1) {
            return R.fail("状态仅支持0或1");
        }
        UserMemberPo spaceUser = requireOwnedSpaceUser(SessionUtil.getUserId(), request.getUserId());
        if (spaceUser == null) {
            return R.fail("更新失败，目标用户可能不存在");
        }
        R<?> result = userAdminService.setForbidden(request.getUserId().longValue(), request.getStatus() == 0);
        if (result.failed()) {
            return R.fail(result.getMsg() != null ? result.getMsg() : "更新失败，目标用户可能不存在");
        }
        return R.ok("更新成功");
    }

    @Override
    public R<String> resetPassword(SpaceUserPasswordUpdate request) {
        if (!SessionUtil.isSpaceAdmin()) {
            return R.fail("仅管理员可重置空间用户密码");
        }
        String newPassword = trimToNull(request.getNewPassword());
        if (newPassword == null) {
            return R.fail("新密码不能为空");
        }
        UserMemberPo spaceUser = requireOwnedSpaceUser(SessionUtil.getUserId(), request.getUserId());
        if (spaceUser == null) {
            return R.fail("重置失败，目标用户可能不存在");
        }
        UserPasswordUpdate update = new UserPasswordUpdate();
        update.setUserId(request.getUserId().longValue());
        update.setNewPassword(newPassword);
        R<?> result = userService.changePassword(update);
        if (result.failed()) {
            return R.fail(result.getMsg() != null ? result.getMsg() : "重置失败，目标用户可能不存在");
        }
        return R.ok("重置成功");
    }

    private SpaceUserBrief toBrief(UserMemberBrief member) {
        SpaceUserBrief row = new SpaceUserBrief();
        row.setId(member.getUserId());
        row.setUsername(member.getUserName());
        row.setNickname(member.getNickname());
        row.setMobile(member.getTel());
        row.setEmail(member.getEmail());
        row.setStatus(Integer.valueOf(WhetherDict.Yes.code).equals(member.getUserForbidden()) ? 0 : 1);
        row.setCreatedAt(member.getCreatedAt());
        String remark = resolveCreatorRemark(member);
        if (StringUtils.hasText(remark)) {
            try {
                row.setCreatorUserId(Long.parseLong(remark));
            } catch (NumberFormatException ignored) {
                row.setCreatorUserId(null);
            }
        }
        return row;
    }

    private String resolveCreatorRemark(UserMemberBrief member) {
        if (StringUtils.hasText(member.getRemark())) {
            return member.getRemark();
        }
        UserMemberPo po = userMemberService.getByUserAndTenant(member.getUserId(), member.getTenantId());
        return po == null ? null : po.getRemark();
    }

    /**
     * 校验空间用户归属：成员类型为 OPERATOR，且 remark 为创建人 userId。
     */
    private UserMemberPo requireOwnedSpaceUser(Integer adminUserId, Integer userId) {
        if (adminUserId == null || userId == null) {
            return null;
        }
        UserMemberPo member = userMemberService.getByUserAndTenant(
                userId.longValue(), SessionUtil.getTenantId());
        if (member == null || WhetherDict.Yes.code == member.getForbidden()) {
            return null;
        }
        if (member.getMemberType() != MemberTypeEnum.OPERATOR) {
            return null;
        }
        if (!Objects.equals(String.valueOf(adminUserId), member.getRemark())) {
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
            userAdminService.delete(userId);
        } catch (RuntimeException e) {
            log.error("补偿新建空间用户失败，userId={}", userId, e);
        }
    }

    private String trimToNull(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }
}
