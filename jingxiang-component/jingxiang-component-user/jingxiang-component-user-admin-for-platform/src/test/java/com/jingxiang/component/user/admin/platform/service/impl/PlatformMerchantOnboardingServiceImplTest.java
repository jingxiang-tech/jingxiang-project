package com.jingxiang.component.user.admin.platform.service.impl;

import com.jingxiang.commons.model.dto.R;
import com.jingxiang.component.user.admin.core.dict.MemberTypeEnum;
import com.jingxiang.component.user.admin.core.model.member.UserMemberPo;
import com.jingxiang.component.user.admin.core.model.member.UserMemberUpdate;
import com.jingxiang.component.user.admin.core.service.UserAdminService;
import com.jingxiang.component.user.admin.core.service.UserMemberService;
import com.jingxiang.component.user.admin.core.service.UserTenantService;
import com.jingxiang.component.user.admin.platform.port.PlatformMerchantUserSpacePort;
import com.jingxiang.component.user.admin.platform.service.PlatformMerchantOnboardingService.OwnerOnboarding;
import com.jingxiang.component.user.model.user.UserBrief;
import com.jingxiang.component.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 平台商户开户跨库补偿组件测试。
 */
@ExtendWith(MockitoExtension.class)
class PlatformMerchantOnboardingServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private UserAdminService userAdminService;

    @Mock
    private UserTenantService userTenantService;

    @Mock
    private UserMemberService userMemberService;

    @Mock
    private PlatformMerchantUserSpacePort userSpacePort;

    @InjectMocks
    private PlatformMerchantOnboardingServiceImpl service;

    @Test
    void shouldRestorePreviousMemberRoleWhenBusinessBindingFails() {
        UserBrief user = new UserBrief();
        user.setUserId(10L);
        UserMemberPo member = UserMemberPo.builder()
                .memberId(30L)
                .userId(10L)
                .tenantId(20L)
                .memberType(MemberTypeEnum.MEMBER)
                .build();
        when(userService.getByUserName("owner")).thenReturn(user);
        when(userTenantService.ensureByCode(any(), any(), any())).thenReturn(20L);
        when(userMemberService.getByUserAndTenant(10L, 20L)).thenReturn(member);
        when(userMemberService.update(any())).thenReturn(R.ok());
        doThrow(new IllegalStateException("业务库不可用"))
                .when(userSpacePort)
                .ensure(any(), any(), any(), any(), org.mockito.ArgumentMatchers.eq(true));

        R<Long> result = service.onboardOwner(command());

        assertTrue(result.failed());
        ArgumentCaptor<UserMemberUpdate> updateCaptor = ArgumentCaptor.forClass(UserMemberUpdate.class);
        verify(userMemberService, org.mockito.Mockito.times(2)).update(updateCaptor.capture());
        assertEquals(MemberTypeEnum.OWNER, updateCaptor.getAllValues().get(0).getMemberType());
        assertEquals(MemberTypeEnum.MEMBER, updateCaptor.getAllValues().get(1).getMemberType());
    }

    @Test
    void shouldContinueUserCompensationWhenMemberCompensationThrows() {
        when(userService.getByUserName("owner")).thenReturn(null);
        when(userService.create(any())).thenReturn(R.ok(10L));
        when(userTenantService.ensureByCode(any(), any(), any())).thenReturn(20L);
        when(userMemberService.getByUserAndTenant(10L, 20L)).thenReturn(null);
        when(userMemberService.add(any())).thenReturn(R.ok(30L));
        doThrow(new IllegalStateException("业务库不可用"))
                .when(userSpacePort)
                .ensure(any(), any(), any(), any(), org.mockito.ArgumentMatchers.eq(true));
        doThrow(new IllegalStateException("成员补偿失败")).when(userMemberService).remove(30L);

        R<Long> result = service.onboardOwner(command());

        assertTrue(result.failed());
        verify(userAdminService).delete(10L);
    }

    private OwnerOnboarding command() {
        return new OwnerOnboarding(
                "M1",
                "商户A",
                1,
                "空间A",
                "owner",
                "Aa123456",
                "platform-admin");
    }
}
