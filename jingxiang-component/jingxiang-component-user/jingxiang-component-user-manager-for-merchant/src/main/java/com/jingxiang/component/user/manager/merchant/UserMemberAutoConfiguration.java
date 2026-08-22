package com.jingxiang.component.user.manager.merchant;

import com.jingxiang.component.user.jwt.JwtAutoConfiguration;
import com.jingxiang.component.user.jwt.JwtCodec;
import com.jingxiang.component.user.manager.common.UserManagerCoreAutoConfiguration;
import com.jingxiang.component.user.manager.common.service.UserManagerService;
import com.jingxiang.component.user.manager.common.service.UserMemberService;
import com.jingxiang.component.user.manager.merchant.controller.UserMemberController;
import com.jingxiang.component.user.manager.merchant.service.impl.UserMemberManageServiceImpl;
import com.jingxiang.component.user.service.UserService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Import;

/**
 * 组织成员 HTTP 自动配置。
 * <p>
 * 不依赖商户业务库 Port，万域等单租户宿主只需用户库与 JWT 即可装配 {@code /user-member}。
 *
 * @author chenjw
 */
@AutoConfiguration(after = {
        JwtAutoConfiguration.class,
        UserManagerCoreAutoConfiguration.class
})
@ConditionalOnBean({
        JwtCodec.class,
        UserService.class,
        UserManagerService.class,
        UserMemberService.class
})
@Import({
        UserMemberManageServiceImpl.class,
        UserMemberController.class
})
public class UserMemberAutoConfiguration {
}
