package com.jingxiang.component.user.admin.merchant;

import com.jingxiang.component.user.admin.common.UserAdminCoreAutoConfiguration;
import com.jingxiang.component.user.admin.common.service.UserAdminService;
import com.jingxiang.component.user.admin.common.service.UserMemberService;
import com.jingxiang.component.user.admin.common.service.UserTenantService;
import com.jingxiang.component.user.admin.merchant.config.MerchantUserAdminProperties;
import com.jingxiang.component.user.admin.merchant.controller.MerchantSpaceUserController;
import com.jingxiang.component.user.admin.merchant.controller.SystemRoleController;
import com.jingxiang.component.user.admin.merchant.controller.SystemUserController;
import com.jingxiang.component.user.admin.merchant.exception.MerchantSessionUnavailableException;
import com.jingxiang.component.user.admin.merchant.port.MerchantDirectoryPort;
import com.jingxiang.component.user.admin.merchant.port.MerchantUserSpacePort;
import com.jingxiang.component.user.admin.merchant.service.impl.MerchantSpaceUserServiceImpl;
import com.jingxiang.component.user.admin.merchant.service.impl.SystemUserServiceImpl;
import com.jingxiang.component.user.admin.merchant.session.MerchantSessionUser;
import com.jingxiang.component.user.admin.merchant.web.MerchantAccessLogAspect;
import com.jingxiang.component.user.admin.merchant.web.MerchantSessionCheckAspect;
import com.jingxiang.component.user.admin.merchant.web.MerchantSessionExceptionResolver;
import com.jingxiang.component.user.jwt.JwtAutoConfiguration;
import com.jingxiang.component.user.jwt.JwtCodec;
import com.jingxiang.component.user.jwt.SessionSupport;
import com.jingxiang.component.user.service.UserService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * 商户后台用户管理自动配置。
 * <p>
 * 本模块不扫描宿主 Mapper；只有宿主同时提供两个业务端口时才装配 HTTP、会话与日志能力。
 *
 * @author chenjw
 */
@AutoConfiguration(after = {
        JwtAutoConfiguration.class,
        UserAdminCoreAutoConfiguration.class
})
@ConditionalOnBean({
        MerchantDirectoryPort.class,
        MerchantUserSpacePort.class,
        JwtCodec.class,
        UserService.class,
        UserAdminService.class,
        UserMemberService.class,
        UserTenantService.class
})
@EnableConfigurationProperties(MerchantUserAdminProperties.class)
@Import({
        SystemUserServiceImpl.class,
        MerchantSpaceUserServiceImpl.class,
        SystemUserController.class,
        MerchantSpaceUserController.class,
        SystemRoleController.class,
        MerchantSessionExceptionResolver.class
})
public class MerchantUserAdminAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(SessionSupport.class)
    public SessionSupport<MerchantSessionUser> merchantSessionSupport(JwtCodec jwtCodec) {
        return new SessionSupport<>(
                jwtCodec,
                MerchantSessionUser.class,
                MerchantSessionUnavailableException::new);
    }

    @Bean
    public MerchantSessionCheckAspect merchantSessionCheckAspect(MerchantUserAdminProperties properties) {
        return new MerchantSessionCheckAspect(properties);
    }

    @Bean
    public MerchantAccessLogAspect merchantAccessLogAspect(MerchantUserAdminProperties properties) {
        return new MerchantAccessLogAspect(properties);
    }
}
