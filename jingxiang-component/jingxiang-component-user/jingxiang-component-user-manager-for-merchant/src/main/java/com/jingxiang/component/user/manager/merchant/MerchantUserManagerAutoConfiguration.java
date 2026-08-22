package com.jingxiang.component.user.manager.merchant;

import com.jingxiang.component.user.manager.common.UserManagerCoreAutoConfiguration;
import com.jingxiang.component.user.manager.common.service.UserManagerService;
import com.jingxiang.component.user.manager.common.service.UserMemberService;
import com.jingxiang.component.user.manager.common.service.UserTenantService;
import com.jingxiang.component.user.manager.merchant.config.MerchantUserManagerProperties;
import com.jingxiang.component.user.manager.merchant.controller.MerchantSpaceUserController;
import com.jingxiang.component.user.manager.merchant.controller.SystemRoleController;
import com.jingxiang.component.user.manager.merchant.controller.SystemUserController;
import com.jingxiang.component.user.manager.merchant.exception.MerchantSessionUnavailableException;
import com.jingxiang.component.user.manager.merchant.port.MerchantDirectoryPort;
import com.jingxiang.component.user.manager.merchant.port.MerchantUserSpacePort;
import com.jingxiang.component.user.manager.merchant.service.impl.MerchantSpaceUserServiceImpl;
import com.jingxiang.component.user.manager.merchant.service.impl.SystemUserServiceImpl;
import com.jingxiang.component.user.manager.merchant.session.MerchantSessionUser;
import com.jingxiang.component.user.manager.merchant.web.MerchantAccessLogAspect;
import com.jingxiang.component.user.manager.merchant.web.MerchantSessionCheckAspect;
import com.jingxiang.component.user.manager.merchant.web.MerchantSessionExceptionResolver;
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
        UserManagerCoreAutoConfiguration.class
})
@ConditionalOnBean({
        MerchantDirectoryPort.class,
        MerchantUserSpacePort.class,
        JwtCodec.class,
        UserService.class,
        UserManagerService.class,
        UserMemberService.class,
        UserTenantService.class
})
@EnableConfigurationProperties(MerchantUserManagerProperties.class)
@Import({
        SystemUserServiceImpl.class,
        MerchantSpaceUserServiceImpl.class,
        SystemUserController.class,
        MerchantSpaceUserController.class,
        SystemRoleController.class,
        MerchantSessionExceptionResolver.class
})
public class MerchantUserManagerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(SessionSupport.class)
    public SessionSupport<MerchantSessionUser> merchantSessionSupport(JwtCodec jwtCodec) {
        return new SessionSupport<>(
                jwtCodec,
                MerchantSessionUser.class,
                MerchantSessionUnavailableException::new);
    }

    @Bean
    public MerchantSessionCheckAspect merchantSessionCheckAspect(MerchantUserManagerProperties properties) {
        return new MerchantSessionCheckAspect(properties);
    }

    @Bean
    public MerchantAccessLogAspect merchantAccessLogAspect(MerchantUserManagerProperties properties) {
        return new MerchantAccessLogAspect(properties);
    }
}
