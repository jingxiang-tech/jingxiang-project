package com.jingxiang.component.user.admin.merchant.service.impl;

import com.jingxiang.commons.model.dto.R;
import com.jingxiang.component.user.admin.core.dict.MemberTypeEnum;
import com.jingxiang.component.user.admin.core.model.member.UserMemberPo;
import com.jingxiang.component.user.admin.core.model.tenant.UserTenantBrief;
import com.jingxiang.component.user.admin.core.service.UserAdminService;
import com.jingxiang.component.user.admin.core.service.UserMemberService;
import com.jingxiang.component.user.admin.core.service.UserTenantService;
import com.jingxiang.component.user.admin.merchant.config.MerchantUserAdminProperties;
import com.jingxiang.component.user.admin.merchant.model.SystemUserInsert;
import com.jingxiang.component.user.admin.merchant.model.SystemUserLoginDto;
import com.jingxiang.component.user.admin.merchant.model.SystemUserLoginParam;
import com.jingxiang.component.user.admin.merchant.port.MerchantDirectoryPort;
import com.jingxiang.component.user.admin.merchant.port.MerchantUserSpacePort;
import com.jingxiang.component.user.admin.merchant.port.MerchantUserSpacePort.MerchantSpace;
import com.jingxiang.component.user.admin.merchant.port.MerchantUserSpacePort.UserSpace;
import com.jingxiang.component.user.admin.merchant.session.MerchantSessionUser;
import com.jingxiang.component.user.jwt.SessionUtil;
import com.jingxiang.component.user.model.user.UserBrief;
import com.jingxiang.component.user.model.user.UserPasswordUpdate;
import com.jingxiang.component.user.model.user.UserPo;
import com.jingxiang.component.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 商户用户登录、选空间与跨库补偿组件测试。
 */
@ExtendWith(MockitoExtension.class)
class SystemUserServiceImplTest {

    @Mock
    private MerchantUserSpacePort merchantUserSpacePort;

    @Mock
    private MerchantDirectoryPort merchantDirectoryPort;

    @Mock
    private UserService userService;

    @Mock
    private UserAdminService userAdminService;

    @Mock
    private UserTenantService userTenantService;

    @Mock
    private UserMemberService userMemberService;

    @Mock
    private MerchantUserAdminProperties properties;

    @InjectMocks
    private SystemUserServiceImpl service;

    @Test
    void shouldRequireSpaceSelectionAndPreserveLoginJsonContract() {
        SystemUserLoginParam param = new SystemUserLoginParam();
        param.setId("employee");
        param.setPwd("secret");
        UserPo user = UserPo.builder().userId(10L).userName("employee").build();
        List<UserSpace> spaces = List.of(
                new UserSpace(1, "空间A", "M1", "商户A", 1),
                new UserSpace(2, "空间B", "M1", "商户A", 0));
        when(userService.authenticate("employee", "secret")).thenReturn(user);
        when(merchantUserSpacePort.listByUserId(10)).thenReturn(spaces);

        try (MockedStatic<SessionUtil> session = mockStatic(SessionUtil.class)) {
            session.when(() -> SessionUtil.getJwtToken(any(MerchantSessionUser.class))).thenReturn("pre-token");

            R<SystemUserLoginDto> result = service.idPwdLogin(param);

            assertTrue(result.succeeded());
            assertEquals("pre-token", result.getData().userToken());
            assertEquals(1, result.getData().needChooseSpace());
            assertEquals(List.of("空间A", "空间B"),
                    result.getData().spaceList().stream().map(option -> option.name()).toList());
        }
    }

    @Test
    void shouldIssueSpaceScopedTokenWhenSwitchingSpace() {
        MerchantSessionUser sessionUser = new MerchantSessionUser();
        sessionUser.setUserId(10);
        UserSpace chosen = new UserSpace(2, "空间B", "M1", "商户A", 1);
        UserPo user = UserPo.builder().userId(10L).userName("employee").build();
        UserTenantBrief tenant = new UserTenantBrief();
        tenant.setTenantId(20L);
        tenant.setTenantCode("M1");
        tenant.setTenantName("商户A");
        when(merchantUserSpacePort.findByUserAndSpace(10, 2)).thenReturn(chosen);
        when(userService.getById(10L)).thenReturn(user);
        when(userTenantService.ensureByCode(any(), eq("M1"), eq("商户A"))).thenReturn(20L);
        when(userTenantService.getByCode("M1")).thenReturn(tenant);

        try (MockedStatic<SessionUtil> session = mockStatic(SessionUtil.class)) {
            session.when(SessionUtil::<MerchantSessionUser>getSessionUser).thenReturn(sessionUser);
            session.when(() -> SessionUtil.getJwtToken(any(MerchantSessionUser.class))).thenReturn("space-token");

            R<String> result = service.switchSpace(2);

            assertTrue(result.succeeded());
            assertEquals("space-token", result.getData());
        }
    }

    @Test
    void shouldCompensateOnlyNewRelationsWhenExistingUserPasswordResetFails() {
        SystemUserInsert insert = new SystemUserInsert();
        insert.setUserName("employee");
        insert.setTel("13800000000");
        insert.setPwd("secret");
        insert.setRole(List.of("MEMBER"));
        insert.setSpaceIds(List.of(1));
        UserBrief brief = new UserBrief();
        brief.setUserId(10L);
        UserPo user = UserPo.builder().userId(10L).userName("employee").build();
        UserMemberPo member = UserMemberPo.builder()
                .memberId(30L)
                .userId(10L)
                .tenantId(20L)
                .memberType(MemberTypeEnum.MEMBER)
                .build();
        List<MerchantSpace> spaces = List.of(new MerchantSpace(1, "空间A"));
        List<UserSpace> added = List.of(new UserSpace(1, "空间A", "M1", "商户A", 0));
        when(userService.getByUserName("employee")).thenReturn(brief);
        when(userService.getById(10L)).thenReturn(user);
        when(userMemberService.belongsTo(10L, 20L)).thenReturn(false);
        when(userMemberService.add(any())).thenReturn(R.ok(30L));
        when(merchantUserSpacePort.listValidSpaces("M1", List.of(1))).thenReturn(spaces);
        when(merchantDirectoryPort.findByMerchantNo("M1"))
                .thenReturn(new MerchantDirectoryPort.MerchantDirectory("M1", "商户A", 0, 10));
        when(merchantUserSpacePort.bindSpaces(10, "M1", "商户A", spaces, "admin")).thenReturn(added);
        when(properties.getExistingUserPassword()).thenReturn("reset-password");
        when(userService.changePassword(any(UserPasswordUpdate.class))).thenReturn(R.fail("密码策略拒绝"));

        try (MockedStatic<SessionUtil> session = mockStatic(SessionUtil.class)) {
            session.when(SessionUtil::isSpaceAdmin).thenReturn(true);
            session.when(SessionUtil::getTenantCode).thenReturn("M1");
            session.when(SessionUtil::getTenantId).thenReturn(20L);
            session.when(SessionUtil::getUserName).thenReturn("admin");

            R<?> result = service.insert(insert);

            assertTrue(result.failed());
            verify(merchantUserSpacePort).removeSpaces(10, "M1", List.of(1));
            verify(userMemberService).remove(30L);
            verify(userAdminService, never()).delete(10L);
        }
    }

    @Test
    void shouldRestoreBusinessRelationsWhenMemberRemovalFails() {
        UserMemberPo member = UserMemberPo.builder().memberId(30L).userId(10L).tenantId(20L).build();
        List<UserSpace> removed = List.of(new UserSpace(1, "空间A", "M1", "商户A", 1));
        when(userMemberService.getByUserAndTenant(10L, 20L)).thenReturn(member);
        when(merchantUserSpacePort.removeByUserAndMerchant(10, "M1")).thenReturn(removed);
        when(userMemberService.remove(30L)).thenReturn(R.fail("移除失败"));

        try (MockedStatic<SessionUtil> session = mockStatic(SessionUtil.class)) {
            session.when(SessionUtil::isSpaceAdmin).thenReturn(true);
            session.when(SessionUtil::getTenantCode).thenReturn("M1");
            session.when(SessionUtil::getTenantId).thenReturn(20L);
            session.when(SessionUtil::getUserName).thenReturn("admin");

            R<?> result = service.delete(10);

            assertTrue(result.failed());
            verify(merchantUserSpacePort).restoreSpaces(10, removed, "admin");
            assertNull(result.getData());
        }
    }
}
