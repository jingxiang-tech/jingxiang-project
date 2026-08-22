package com.jingxiang.component.user.admin.platform.service.impl;

import com.jingxiang.commons.model.dto.R;
import com.jingxiang.component.user.admin.core.dict.MemberTypeEnum;
import com.jingxiang.component.user.admin.core.model.member.UserMemberBrief;
import com.jingxiang.component.user.admin.core.service.UserAdminService;
import com.jingxiang.component.user.admin.core.service.UserMemberService;
import com.jingxiang.component.user.admin.core.service.UserTenantService;
import com.jingxiang.component.user.admin.platform.model.MerchantUserBrief;
import com.jingxiang.component.user.admin.platform.model.MerchantUserCreate;
import com.jingxiang.component.user.admin.platform.model.MerchantUserQuery;
import com.jingxiang.component.user.admin.platform.port.PlatformMerchantDirectoryPort;
import com.jingxiang.component.user.admin.platform.port.PlatformMerchantUserSpacePort;
import com.jingxiang.component.user.admin.platform.port.PlatformOperatorPort;
import com.jingxiang.component.user.model.user.UserPo;
import com.jingxiang.component.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 平台商户用户 CRUD、空间授权和映射组件测试。
 */
@ExtendWith(MockitoExtension.class)
class PlatformMerchantUserServiceImplTest {

    @Mock
    private PlatformMerchantDirectoryPort merchantDirectoryPort;

    @Mock
    private PlatformMerchantUserSpacePort userSpacePort;

    @Mock
    private PlatformOperatorPort operatorPort;

    @Mock
    private UserService userService;

    @Mock
    private UserAdminService userAdminService;

    @Mock
    private UserTenantService userTenantService;

    @Mock
    private UserMemberService userMemberService;

    @InjectMocks
    private PlatformMerchantUserServiceImpl service;

    @Test
    void shouldMapCoreMemberProjectionToPlatformJsonContract() {
        UserMemberBrief member = new UserMemberBrief();
        member.setUserId(10L);
        member.setUserName("employee");
        member.setRealName("测试员工");
        member.setTel("13800000000");
        member.setTenantCode("M1");
        member.setUserForbidden(1);
        member.setUserRemark("备注");
        when(userMemberService.listAll(any())).thenReturn(R.ok(List.of(member)));

        MerchantUserQuery query = new MerchantUserQuery();
        query.setMctNo("M1");
        query.setKeyword("测试");
        R<List<MerchantUserBrief>> result = service.list(query);

        assertTrue(result.succeeded());
        MerchantUserBrief brief = result.getData().get(0);
        assertEquals(10, brief.getUserId());
        assertEquals("employee", brief.getUserName());
        assertEquals("M1", brief.getMctNo());
        assertEquals(1, brief.getDisabled());
        assertEquals("备注", brief.getRemark());
    }

    @Test
    void shouldCompensateUserDatabaseWhenBusinessSpaceWriteFails() {
        MerchantUserCreate create = new MerchantUserCreate();
        create.setMctNo("M1");
        create.setUserName("employee");
        create.setSpaceIds(List.of(1));
        PlatformMerchantDirectoryPort.Merchant merchant =
                new PlatformMerchantDirectoryPort.Merchant("M1", "商户A");
        PlatformMerchantDirectoryPort.MerchantSpace space =
                new PlatformMerchantDirectoryPort.MerchantSpace(1, "M1", "空间A");
        when(merchantDirectoryPort.findMerchant("M1")).thenReturn(Optional.of(merchant));
        when(merchantDirectoryPort.listSpaces("M1")).thenReturn(List.of(space));
        when(userService.create(any())).thenReturn(R.ok(10L));
        when(userTenantService.ensureByCode(any(), any(), any())).thenReturn(20L);
        when(userMemberService.add(any())).thenReturn(R.ok(30L));
        when(operatorPort.currentOperator()).thenReturn("platform-admin");
        org.mockito.Mockito.doThrow(new IllegalStateException("业务库不可用"))
                .when(userSpacePort)
                .replace(any(), any(), any(), any(), any(Boolean.class));
        org.mockito.Mockito.doThrow(new IllegalStateException("成员补偿失败"))
                .when(userMemberService)
                .remove(30L);

        R<Void> result = service.create(create);

        assertTrue(result.failed());
        verify(userMemberService).remove(30L);
        verify(userAdminService).delete(10L);
    }

    @Test
    void shouldRestoreExactSpaceSnapshotWhenUserDeletionFails() {
        List<PlatformMerchantUserSpacePort.UserSpace> snapshot = List.of(
                new PlatformMerchantUserSpacePort.UserSpace(1, "空间A", "M1", "商户A", true));
        when(userService.getById(10L)).thenReturn(UserPo.builder().userId(10L).build());
        when(userSpacePort.listByUserId(10)).thenReturn(snapshot);
        when(userAdminService.delete(10L)).thenReturn(R.fail("统一用户删除失败"));
        when(operatorPort.currentOperator()).thenReturn("platform-admin");

        R<Void> result = service.delete(10);

        assertTrue(result.failed());
        verify(userSpacePort).remove(10);
        verify(userSpacePort).restore(10, snapshot, "platform-admin");
    }

    @Test
    void shouldDeduplicateAndValidateSpaceAuthorization() {
        PlatformMerchantDirectoryPort.Merchant merchant =
                new PlatformMerchantDirectoryPort.Merchant("M1", "商户A");
        PlatformMerchantDirectoryPort.MerchantSpace space =
                new PlatformMerchantDirectoryPort.MerchantSpace(1, "M1", "空间A");
        when(userService.getById(10L)).thenReturn(UserPo.builder().userId(10L).build());
        when(userSpacePort.findMerchantNo(10)).thenReturn(Optional.of("M1"));
        when(merchantDirectoryPort.findMerchant("M1")).thenReturn(Optional.of(merchant));
        when(merchantDirectoryPort.listSpaces("M1")).thenReturn(List.of(space));
        when(operatorPort.currentOperator()).thenReturn("platform-admin");

        R<Void> result = service.putSpaceIds(10, List.of(1, 1));

        assertTrue(result.succeeded());
        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<PlatformMerchantUserSpacePort.MerchantSpace>> spacesCaptor =
                ArgumentCaptor.forClass(List.class);
        verify(userSpacePort).replace(
                org.mockito.ArgumentMatchers.eq(10),
                org.mockito.ArgumentMatchers.eq(
                        new PlatformMerchantUserSpacePort.Merchant("M1", "商户A")),
                spacesCaptor.capture(),
                org.mockito.ArgumentMatchers.eq("platform-admin"),
                org.mockito.ArgumentMatchers.eq(false));
        assertEquals(List.of(new PlatformMerchantUserSpacePort.MerchantSpace(1, "空间A")),
                spacesCaptor.getValue());
    }
}
