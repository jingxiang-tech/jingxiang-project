package com.jingxiang.component.user.admin.core.service.impl;

import com.jingxiang.commons.model.dict.WhetherDict;
import com.jingxiang.commons.model.dto.R;
import com.jingxiang.component.user.admin.core.dao.UserMemberMapper;
import com.jingxiang.component.user.admin.core.dict.MemberTypeEnum;
import com.jingxiang.component.user.admin.core.model.member.UserMemberCreate;
import com.jingxiang.component.user.admin.core.model.member.UserMemberPo;
import com.jingxiang.component.user.admin.core.model.tenant.UserTenantPo;
import com.jingxiang.component.user.admin.core.service.UserTenantService;
import com.jingxiang.component.user.model.user.UserPo;
import com.jingxiang.component.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 用户成员服务组件测试。
 */
@ExtendWith(MockitoExtension.class)
class UserMemberServiceImplTest {

    @Mock
    private UserMemberMapper userMemberMapper;

    @Mock
    private UserService userService;

    @Mock
    private UserTenantService userTenantService;

    @InjectMocks
    private UserMemberServiceImpl service;

    @Test
    void shouldMapCreateCommandAndReturnGeneratedMemberId() {
        UserMemberCreate create = new UserMemberCreate();
        create.setUserId(10L);
        create.setTenantId(20L);
        create.setMemberType(MemberTypeEnum.MEMBER);
        create.setMemberName("测试员工");
        create.setRemark("来源于组件测试");

        UserPo user = UserPo.builder().userId(10L).forbidden(WhetherDict.No.code).build();
        UserTenantPo tenant = UserTenantPo.builder()
                .tenantId(20L)
                .forbidden(WhetherDict.No.code)
                .build();
        when(userService.getById(10L)).thenReturn(user);
        when(userTenantService.getAvailableById(20L)).thenReturn(tenant);
        when(userMemberMapper.insert(any(UserMemberPo.class))).thenAnswer(invocation -> {
            UserMemberPo po = invocation.getArgument(0);
            po.setMemberId(30L);
            return 1;
        });

        R<Long> result = service.add(create);

        assertTrue(result.succeeded());
        assertEquals(30L, result.getData());
        ArgumentCaptor<UserMemberPo> captor = ArgumentCaptor.forClass(UserMemberPo.class);
        verify(userMemberMapper).insert(captor.capture());
        UserMemberPo saved = captor.getValue();
        assertEquals(10L, saved.getUserId());
        assertEquals(20L, saved.getTenantId());
        assertEquals(MemberTypeEnum.MEMBER, saved.getMemberType());
        assertEquals(WhetherDict.No.code, saved.getForbidden());
        assertEquals(WhetherDict.No.code, saved.getDeleted());
    }

    @Test
    void shouldRejectDisabledUserBeforeWritingMemberMapper() {
        UserMemberCreate create = new UserMemberCreate();
        create.setUserId(10L);
        create.setTenantId(20L);
        when(userService.getById(10L))
                .thenReturn(UserPo.builder().userId(10L).forbidden(WhetherDict.Yes.code).build());

        R<Long> result = service.add(create);

        assertTrue(result.failed());
        assertEquals("用户已禁用", result.getMsg());
        verify(userMemberMapper, never()).insert(any(UserMemberPo.class));
    }

    @Test
    void shouldConvertConcurrentDuplicateInsertToStableBusinessFailure() {
        UserMemberCreate create = new UserMemberCreate();
        create.setUserId(10L);
        create.setTenantId(20L);
        create.setMemberType(MemberTypeEnum.MEMBER);
        when(userService.getById(10L))
                .thenReturn(UserPo.builder().userId(10L).forbidden(WhetherDict.No.code).build());
        when(userTenantService.getAvailableById(20L))
                .thenReturn(UserTenantPo.builder().tenantId(20L).forbidden(WhetherDict.No.code).build());
        when(userMemberMapper.insert(any(UserMemberPo.class)))
                .thenThrow(new DuplicateKeyException("uk_user_tenant"));

        R<Long> result = service.add(create);

        assertTrue(result.failed());
        assertEquals("该用户已是组织成员", result.getMsg());
    }
}
