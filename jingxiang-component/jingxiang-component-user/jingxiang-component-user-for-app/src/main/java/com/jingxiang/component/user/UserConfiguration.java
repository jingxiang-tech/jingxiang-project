package com.jingxiang.component.user;

import com.jingxiang.component.user.datasource.UserDataSourceConfiguration;
import com.jingxiang.component.user.service.impl.UserAuthServiceImpl;
import com.jingxiang.component.user.service.impl.UserIdentityServiceImpl;
import com.jingxiang.component.user.service.impl.UserServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Import;

/**
 * 统一用户基础组件自动配置（仅 Service，无 HTTP）
 * <p>
 * Mapper 与用户库数据源见 {@link UserDataSourceConfiguration}。
 * 使用 {@link Import} 注册 Service，避免与 {@link ConditionalOnBean}（REGISTER_BEAN 阶段）冲突。
 *
 * @author chenjw
 */
@AutoConfiguration(after = UserDataSourceConfiguration.class)
@ConditionalOnBean(name = UserDataSourceConfiguration.SQL_SESSION_FACTORY)
@Import({
        UserServiceImpl.class,
        UserAuthServiceImpl.class,
        UserIdentityServiceImpl.class
})
public class UserConfiguration {
}
