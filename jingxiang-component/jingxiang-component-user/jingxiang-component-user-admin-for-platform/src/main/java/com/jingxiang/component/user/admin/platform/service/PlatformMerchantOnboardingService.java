package com.jingxiang.component.user.admin.platform.service;

import com.jingxiang.commons.model.dto.R;

/**
 * 平台商户用户库开户服务。
 */
public interface PlatformMerchantOnboardingService {

    R<Long> onboardOwner(OwnerOnboarding command);

    R<Long> ensureMerchantTenant(String merchantNo, String merchantName);

    /**
     * OWNER 开户命令，只携带跨边界所需的中立数据。
     */
    record OwnerOnboarding(
            String merchantNo,
            String merchantName,
            Integer spaceId,
            String spaceName,
            String userName,
            String initialPassword,
            String operator) {
    }
}
