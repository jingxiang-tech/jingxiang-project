package com.jingxiang.component.user.admin.platform;

import com.jingxiang.component.user.UserCoreAutoConfiguration;
import com.jingxiang.component.user.admin.common.UserAdminCoreAutoConfiguration;
import com.jingxiang.component.user.admin.common.service.UserAdminService;
import com.jingxiang.component.user.admin.common.service.UserMemberService;
import com.jingxiang.component.user.admin.common.service.UserTenantService;
import com.jingxiang.component.user.admin.platform.controller.PlatformMerchantSpaceUserController;
import com.jingxiang.component.user.admin.platform.controller.PlatformMerchantUserController;
import com.jingxiang.component.user.admin.platform.port.PlatformMerchantDirectoryPort;
import com.jingxiang.component.user.admin.platform.port.PlatformMerchantUserSpacePort;
import com.jingxiang.component.user.admin.platform.port.PlatformOperatorPort;
import com.jingxiang.component.user.admin.platform.service.impl.PlatformMerchantOnboardingServiceImpl;
import com.jingxiang.component.user.admin.platform.service.impl.PlatformMerchantUserServiceImpl;
import com.jingxiang.component.user.service.UserService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Import;

/**
 * 平台端商户用户管理自动配置。
 * <p>
 * 仅在宿主提供三个平台端口时装配，不扫描宿主 Mapper，也不装配登录、JWT 和拦截器。
 */
@AutoConfiguration(after = {
        UserCoreAutoConfiguration.class,
        UserAdminCoreAutoConfiguration.class
})
@ConditionalOnBean({
        PlatformMerchantDirectoryPort.class,
        PlatformMerchantUserSpacePort.class,
        PlatformOperatorPort.class,
        UserService.class,
        UserAdminService.class,
        UserMemberService.class,
        UserTenantService.class
})
@Import({
        PlatformMerchantUserServiceImpl.class,
        PlatformMerchantOnboardingServiceImpl.class,
        PlatformMerchantUserController.class,
        PlatformMerchantSpaceUserController.class
})
public class PlatformUserAdminAutoConfiguration {
}
