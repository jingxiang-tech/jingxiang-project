package com.jingxiang.component.user.admin.platform.service.impl;

import com.jingxiang.commons.model.dto.R;
import com.jingxiang.component.user.admin.common.dict.MemberTypeEnum;
import com.jingxiang.component.user.admin.common.dict.TenantTypeEnum;
import com.jingxiang.component.user.admin.common.model.member.UserMemberBrief;
import com.jingxiang.component.user.admin.common.model.member.UserMemberCreate;
import com.jingxiang.component.user.admin.common.model.member.UserMemberQuery;
import com.jingxiang.component.user.admin.common.service.UserAdminService;
import com.jingxiang.component.user.admin.common.service.UserMemberService;
import com.jingxiang.component.user.admin.common.service.UserTenantService;
import com.jingxiang.component.user.admin.platform.model.*;
import com.jingxiang.component.user.admin.platform.port.PlatformMerchantDirectoryPort;
import com.jingxiang.component.user.admin.platform.port.PlatformMerchantUserSpacePort;
import com.jingxiang.component.user.admin.platform.port.PlatformOperatorPort;
import com.jingxiang.component.user.admin.platform.service.PlatformMerchantUserService;
import com.jingxiang.component.user.model.user.*;
import com.jingxiang.component.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 平台端商户用户管理服务实现。
 */
public class PlatformMerchantUserServiceImpl implements PlatformMerchantUserService {

    private static final Logger log = LoggerFactory.getLogger(PlatformMerchantUserServiceImpl.class);
    private static final String DEFAULT_PASSWORD = "123456";

    @Autowired
    private PlatformMerchantDirectoryPort merchantDirectoryPort;

    @Autowired
    private PlatformMerchantUserSpacePort userSpacePort;

    @Autowired
    private PlatformOperatorPort operatorPort;

    @Autowired
    private UserService userService;

    @Autowired
    private UserAdminService userAdminService;

    @Autowired
    private UserTenantService userTenantService;

    @Autowired
    private UserMemberService userMemberService;

    @Override
    public R<List<MerchantUserBrief>> list(MerchantUserQuery query) {
        UserMemberQuery memberQuery = new UserMemberQuery();
        memberQuery.setTenantCode(query.getMctNo());
        memberQuery.setKeyword(query.getKeyword());
        R<List<UserMemberBrief>> result = userMemberService.listAll(memberQuery);
        if (result.failed()) {
            return R.fail(result.getMsg());
        }
        List<UserMemberBrief> members = result.getData() == null ? List.of() : result.getData();
        return R.ok(members.stream().map(this::toBrief).toList());
    }

    @Override
    public R<MerchantUserDetail> get(Integer userId) {
        R<UserDetail> result = userService.detail(userId.longValue());
        if (result.failed() || result.getData() == null) {
            return R.fail("用户不存在");
        }
        return R.ok(toDetail(result.getData(), resolveMerchantNo(userId)));
    }

    @Override
    public R<Void> create(MerchantUserCreate create) {
        Optional<PlatformMerchantDirectoryPort.Merchant> merchantOptional =
                merchantDirectoryPort.findMerchant(create.getMctNo());
        if (merchantOptional.isEmpty()) {
            return R.fail("商户不存在");
        }
        List<PlatformMerchantDirectoryPort.MerchantSpace> spaces =
                validateSpaces(create.getMctNo(), create.getSpaceIds());
        if (spaces == null) {
            return R.fail("存在不属于该商户的空间，无法赋权");
        }

        UserCreate userCreate = new UserCreate();
        userCreate.setUserName(create.getUserName());
        userCreate.setRealName(create.getRealName());
        userCreate.setTel(create.getTel());
        userCreate.setPassword(StringUtils.hasText(create.getPwd()) ? create.getPwd() : DEFAULT_PASSWORD);
        userCreate.setNickname(create.getUserName());
        userCreate.setRemark(create.getRemark());
        R<Long> userResult = userService.create(userCreate);
        if (userResult.failed()) {
            return R.fail(userResult.getMsg());
        }
        Long userId = userResult.getData();

        Long memberId = null;
        try {
            PlatformMerchantDirectoryPort.Merchant merchant = merchantOptional.get();
            Long tenantId = userTenantService.ensureByCode(
                    TenantTypeEnum.MERCHANT, merchant.merchantNo(), merchant.merchantName());
            UserMemberCreate memberCreate = new UserMemberCreate();
            memberCreate.setUserId(userId);
            memberCreate.setTenantId(tenantId);
            memberCreate.setMemberType(MemberTypeEnum.MEMBER);
            R<Long> memberResult = userMemberService.add(memberCreate);
            if (memberResult.failed()) {
                compensateCreatedUser(userId, null);
                return R.fail(memberResult.getMsg());
            }
            memberId = memberResult.getData();
            replaceSpaces(userId.intValue(), merchant, spaces, false);
            return R.ok();
        } catch (RuntimeException exception) {
            compensateCreatedUser(userId, memberId);
            return R.fail("创建用户失败：" + exception.getMessage());
        }
    }

    @Override
    public R<Void> update(MerchantUserUpdate update) {
        if (userService.getById(update.getUserId().longValue()) == null) {
            return R.fail("用户不存在");
        }
        UserUpdate userUpdate = new UserUpdate();
        userUpdate.setUserId(update.getUserId().longValue());
        userUpdate.setUserName(update.getUserName());
        userUpdate.setRealName(update.getRealName());
        userUpdate.setTel(update.getTel());
        userUpdate.setRemark(update.getRemark());
        R<?> result = userService.update(userUpdate);
        if (result.failed()) {
            return R.fail(result.getMsg());
        }
        if (update.getDisabled() != null) {
            return setDisabled(update.getUserId(), update.getDisabled());
        }
        return R.ok();
    }

    @Override
    public R<Void> delete(Integer userId) {
        if (userService.getById(userId.longValue()) == null) {
            return R.fail("用户不存在");
        }
        List<PlatformMerchantUserSpacePort.UserSpace> removedSpaces = userSpacePort.listByUserId(userId);
        userSpacePort.remove(userId);
        try {
            R<?> result = userAdminService.delete(userId.longValue());
            if (result.failed()) {
                restoreSpaces(userId, removedSpaces);
                return R.fail("删除失败");
            }
            return R.ok();
        } catch (RuntimeException exception) {
            restoreSpaces(userId, removedSpaces);
            throw exception;
        }
    }

    @Override
    public R<Void> resetPassword(Integer userId) {
        if (userService.getById(userId.longValue()) == null) {
            return R.fail("用户不存在");
        }
        UserPasswordUpdate update = new UserPasswordUpdate();
        update.setUserId(userId.longValue());
        update.setNewPassword(DEFAULT_PASSWORD);
        return userService.changePassword(update).failed() ? R.fail("重置失败") : R.ok();
    }

    @Override
    public R<Void> setDisabled(Integer userId, Integer disabled) {
        if (userService.getById(userId.longValue()) == null) {
            return R.fail("用户不存在");
        }
        return userAdminService.setForbidden(userId.longValue(), Objects.equals(disabled, 1)).failed()
                ? R.fail("操作失败") : R.ok();
    }

    @Override
    public R<List<Integer>> getSpaceIds(Integer userId) {
        return R.ok(userSpacePort.listSpaceIds(userId));
    }

    @Override
    public R<Void> putSpaceIds(Integer userId, List<Integer> spaceIds) {
        if (userService.getById(userId.longValue()) == null) {
            return R.fail("用户不存在");
        }
        String merchantNo = resolveMerchantNo(userId);
        if (!StringUtils.hasText(merchantNo)) {
            return R.fail("用户未归属商户");
        }
        Optional<PlatformMerchantDirectoryPort.Merchant> merchantOptional =
                merchantDirectoryPort.findMerchant(merchantNo);
        if (merchantOptional.isEmpty()) {
            return R.fail("商户不存在");
        }
        List<PlatformMerchantDirectoryPort.MerchantSpace> spaces = validateSpaces(merchantNo, spaceIds);
        if (spaces == null) {
            return R.fail("存在不属于该商户的空间，无法赋权");
        }
        replaceSpaces(userId, merchantOptional.get(), spaces, false);
        return R.ok();
    }

    @Override
    public R<List<MerchantUserBrief>> listBySpaceId(Integer spaceId) {
        Optional<PlatformMerchantDirectoryPort.MerchantSpace> spaceOptional =
                merchantDirectoryPort.findSpace(spaceId);
        if (spaceOptional.isEmpty()) {
            return R.fail("业务空间不存在");
        }
        List<Integer> userIds = userSpacePort.listUserIds(spaceId);
        if (userIds.isEmpty()) {
            return R.ok(List.of());
        }
        List<UserBrief> users = userService.listByIds(userIds.stream().map(Integer::longValue).toList());
        Map<Long, UserBrief> userMap = users.stream()
                .collect(Collectors.toMap(UserBrief::getUserId, Function.identity()));
        String merchantNo = spaceOptional.get().merchantNo();
        return R.ok(userIds.stream()
                .map(Integer::longValue)
                .distinct()
                .map(userMap::get)
                .filter(Objects::nonNull)
                .map(user -> toBrief(user, merchantNo))
                .toList());
    }

    private List<PlatformMerchantDirectoryPort.MerchantSpace> validateSpaces(
            String merchantNo, List<Integer> spaceIds) {
        if (spaceIds == null || spaceIds.isEmpty()) {
            return List.of();
        }
        Map<Integer, PlatformMerchantDirectoryPort.MerchantSpace> allowed =
                merchantDirectoryPort.listSpaces(merchantNo).stream()
                        .collect(Collectors.toMap(
                                PlatformMerchantDirectoryPort.MerchantSpace::spaceId,
                                Function.identity()));
        if (!allowed.keySet().containsAll(spaceIds)) {
            return null;
        }
        return spaceIds.stream().distinct().map(allowed::get).toList();
    }

    private void replaceSpaces(
            Integer userId,
            PlatformMerchantDirectoryPort.Merchant merchant,
            List<PlatformMerchantDirectoryPort.MerchantSpace> spaces,
            boolean administrator) {
        List<PlatformMerchantUserSpacePort.MerchantSpace> assignments = spaces.stream()
                .map(space -> new PlatformMerchantUserSpacePort.MerchantSpace(space.spaceId(), space.spaceName()))
                .toList();
        userSpacePort.replace(
                userId,
                new PlatformMerchantUserSpacePort.Merchant(merchant.merchantNo(), merchant.merchantName()),
                assignments,
                currentOperator(),
                administrator);
    }

    private String resolveMerchantNo(Integer userId) {
        Optional<String> relationMerchantNo = userSpacePort.findMerchantNo(userId);
        if (relationMerchantNo.isPresent()) {
            return relationMerchantNo.get();
        }
        R<List<UserMemberBrief>> result = userMemberService.listTenantsByUserId(userId.longValue());
        if (result.failed() || result.getData() == null || result.getData().isEmpty()) {
            return null;
        }
        return result.getData().get(0).getTenantCode();
    }

    private String currentOperator() {
        String operator = operatorPort.currentOperator();
        return StringUtils.hasText(operator) ? operator : "system";
    }

    private void restoreSpaces(Integer userId, List<PlatformMerchantUserSpacePort.UserSpace> spaces) {
        try {
            userSpacePort.restore(userId, spaces, currentOperator());
        } catch (RuntimeException exception) {
            log.error("补偿平台用户空间关系失败，userId={}", userId, exception);
        }
    }

    private void compensateCreatedUser(Long userId, Long memberId) {
        if (memberId != null) {
            try {
                userMemberService.remove(memberId);
            } catch (RuntimeException exception) {
                log.error("补偿平台用户成员关系失败，userId={}, memberId={}", userId, memberId, exception);
            }
        }
        try {
            userAdminService.delete(userId);
        } catch (RuntimeException exception) {
            log.error("补偿平台统一用户失败，userId={}", userId, exception);
        }
    }

    private MerchantUserBrief toBrief(UserMemberBrief member) {
        MerchantUserBrief brief = new MerchantUserBrief();
        brief.setUserId(member.getUserId().intValue());
        brief.setUserName(member.getUserName());
        brief.setRealName(member.getRealName());
        brief.setTel(member.getTel());
        brief.setMctNo(member.getTenantCode());
        brief.setDisabled(member.getUserForbidden());
        brief.setRemark(member.getUserRemark());
        brief.setCreateTime(member.getUserCreatedAt());
        return brief;
    }

    private MerchantUserBrief toBrief(UserBrief user, String merchantNo) {
        MerchantUserBrief brief = new MerchantUserBrief();
        brief.setUserId(user.getUserId().intValue());
        brief.setUserName(user.getUserName());
        brief.setRealName(user.getRealName());
        brief.setTel(user.getTel());
        brief.setMctNo(merchantNo);
        brief.setDisabled(user.getForbidden());
        brief.setCreateTime(user.getCreatedAt());
        return brief;
    }

    private MerchantUserDetail toDetail(UserDetail user, String merchantNo) {
        MerchantUserDetail detail = new MerchantUserDetail();
        detail.setUserId(user.getUserId().intValue());
        detail.setUserName(user.getUserName());
        detail.setRealName(user.getRealName());
        detail.setTel(user.getTel());
        detail.setMctNo(merchantNo);
        detail.setDisabled(user.getForbidden());
        detail.setRemark(user.getRemark());
        detail.setDeleted(0);
        detail.setCreateTime(user.getCreatedAt());
        detail.setUpdateTime(user.getUpdatedAt());
        return detail;
    }
}
