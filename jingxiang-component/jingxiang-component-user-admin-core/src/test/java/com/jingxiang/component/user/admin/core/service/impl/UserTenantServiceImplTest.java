package com.jingxiang.component.user.admin.core.service.impl;

import com.jingxiang.component.user.admin.core.dao.UserTenantMapper;
import com.jingxiang.component.user.admin.core.dict.TenantTypeEnum;
import com.jingxiang.component.user.admin.core.model.tenant.UserTenantBrief;
import com.jingxiang.component.user.admin.core.model.tenant.UserTenantPo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 用户租户幂等服务组件测试。
 */
@ExtendWith(MockitoExtension.class)
class UserTenantServiceImplTest {

    @Mock
    private UserTenantMapper userTenantMapper;

    @InjectMocks
    private UserTenantServiceImpl service;

    @Test
    void shouldReturnExistingTenantWithoutInsert() {
        UserTenantBrief existing = new UserTenantBrief();
        existing.setTenantId(100L);
        existing.setTenantCode("M100");
        when(userTenantMapper.findByCode("M100")).thenReturn(existing);

        Long tenantId = service.ensureByCode(TenantTypeEnum.MERCHANT, "M100", "测试商户");

        assertEquals(100L, tenantId);
    }

    @Test
    void shouldReadConcurrentWinnerAfterDuplicateKey() {
        UserTenantBrief concurrentWinner = new UserTenantBrief();
        concurrentWinner.setTenantId(101L);
        concurrentWinner.setTenantCode("M101");
        when(userTenantMapper.findByCode("M101"))
                .thenReturn(null)
                .thenReturn(null)
                .thenReturn(concurrentWinner);
        when(userTenantMapper.insert(any(UserTenantPo.class)))
                .thenThrow(new DuplicateKeyException("uk_tenant_code"));

        Long tenantId = service.ensureByCode(TenantTypeEnum.MERCHANT, "M101", "并发商户");

        assertEquals(101L, tenantId);
        verify(userTenantMapper).insert(any(UserTenantPo.class));
    }
}
