package com.jingxiang.component.user.admin.platform.service.impl;

import com.jingxiang.commons.model.dto.R;
import com.jingxiang.component.user.admin.core.dict.MemberTypeEnum;
import com.jingxiang.component.user.admin.core.dict.TenantTypeEnum;
import com.jingxiang.component.user.admin.core.model.member.UserMemberCreate;
import com.jingxiang.component.user.admin.core.model.member.UserMemberPo;
import com.jingxiang.component.user.admin.core.model.member.UserMemberUpdate;
import com.jingxiang.component.user.admin.core.service.UserAdminService;
import com.jingxiang.component.user.admin.core.service.UserMemberService;
import com.jingxiang.component.user.admin.core.service.UserTenantService;
import com.jingxiang.component.user.admin.platform.port.PlatformMerchantUserSpacePort;
import com.jingxiang.component.user.admin.platform.service.PlatformMerchantOnboardingService;
import com.jingxiang.component.user.model.user.UserBrief;
import com.jingxiang.component.user.model.user.UserCreate;
import com.jingxiang.component.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * 商户 OWNER 开户实现。
 * <p>
 * 顺序固定为用户库用户、租户、成员，再写业务库空间关系。两库不使用伪分布式事务；
 * 末步失败时仅补偿本次新建的成员和用户，租户作为按商户号幂等的数据保留。
 */
public class PlatformMerchantOnboardingServiceImpl implements PlatformMerchantOnboardingService {

    private static final Logger log = LoggerFactory.getLogger(PlatformMerchantOnboardingServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserAdminService userAdminService;

    @Autowired
    private UserTenantService userTenantService;

    @Autowired
    private UserMemberService userMemberService;

    @Autowired
    private PlatformMerchantUserSpacePort userSpacePort;

    @Override
    public R<Long> onboardOwner(OwnerOnboarding command) {
        boolean userCreated = false;
        Long createdMemberId = null;
        Long updatedMemberId = null;
        MemberTypeEnum previousMemberType = null;
        Long userId = null;
        try {
            UserBrief existingUser = userService.getByUserName(command.userName());
            if (existingUser == null) {
                UserCreate create = new UserCreate();
                create.setUserName(command.userName());
                create.setNickname(command.userName());
                create.setRealName("管理者");
                create.setPassword(command.initialPassword());
                R<Long> userResult = userService.create(create);
                if (userResult.failed()) {
                    return R.fail(userResult.getMsg());
                }
                userId = userResult.getData();
                userCreated = true;
            } else {
                userId = existingUser.getUserId();
            }

            Long tenantId = userTenantService.ensureByCode(
                    TenantTypeEnum.MERCHANT, command.merchantNo(), command.merchantName());
            UserMemberPo member = userMemberService.getByUserAndTenant(userId, tenantId);
            if (member == null) {
                UserMemberCreate memberCreate = new UserMemberCreate();
                memberCreate.setUserId(userId);
                memberCreate.setTenantId(tenantId);
                memberCreate.setMemberType(MemberTypeEnum.OWNER);
                R<Long> memberResult = userMemberService.add(memberCreate);
                if (memberResult.failed()) {
                    compensate(userId, userCreated, null);
                    return R.fail(memberResult.getMsg());
                }
                createdMemberId = memberResult.getData();
            } else if (member.getMemberType() != MemberTypeEnum.OWNER) {
                updatedMemberId = member.getMemberId();
                previousMemberType = member.getMemberType();
                UserMemberUpdate memberUpdate = new UserMemberUpdate();
                memberUpdate.setMemberId(member.getMemberId());
                memberUpdate.setMemberType(MemberTypeEnum.OWNER);
                R<?> updateResult = userMemberService.update(memberUpdate);
                if (updateResult.failed()) {
                    compensate(userId, userCreated, null);
                    return R.fail(updateResult.getMsg());
                }
            }

            String operator = StringUtils.hasText(command.operator()) ? command.operator() : "system";
            userSpacePort.ensure(
                    userId.intValue(),
                    new PlatformMerchantUserSpacePort.Merchant(command.merchantNo(), command.merchantName()),
                    new PlatformMerchantUserSpacePort.MerchantSpace(command.spaceId(), command.spaceName()),
                    operator,
                    true);
            return R.ok(userId);
        } catch (RuntimeException exception) {
            compensate(userId, userCreated, createdMemberId, updatedMemberId, previousMemberType);
            return R.fail("商户 OWNER 开户失败：" + exception.getMessage());
        }
    }

    @Override
    public R<Long> ensureMerchantTenant(String merchantNo, String merchantName) {
        try {
            return R.ok(userTenantService.ensureByCode(TenantTypeEnum.MERCHANT, merchantNo, merchantName));
        } catch (RuntimeException exception) {
            return R.fail("创建商户用户租户失败：" + exception.getMessage());
        }
    }

    private void compensate(Long userId, boolean userCreated, Long memberId) {
        compensate(userId, userCreated, memberId, null, null);
    }

    private void compensate(
            Long userId,
            boolean userCreated,
            Long memberId,
            Long updatedMemberId,
            MemberTypeEnum previousMemberType) {
        if (memberId != null) {
            try {
                userMemberService.remove(memberId);
            } catch (RuntimeException exception) {
                log.error("补偿开户成员关系失败，userId={}, memberId={}", userId, memberId, exception);
            }
        }
        if (updatedMemberId != null && previousMemberType != null) {
            try {
                UserMemberUpdate update = new UserMemberUpdate();
                update.setMemberId(updatedMemberId);
                update.setMemberType(previousMemberType);
                userMemberService.update(update);
            } catch (RuntimeException exception) {
                log.error("补偿开户成员角色失败，userId={}, memberId={}", userId, updatedMemberId, exception);
            }
        }
        if (userCreated && userId != null) {
            try {
                userAdminService.delete(userId);
            } catch (RuntimeException exception) {
                log.error("补偿开户统一用户失败，userId={}", userId, exception);
            }
        }
    }
}
