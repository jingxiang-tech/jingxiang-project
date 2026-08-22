package com.jingxiang.component.user.manager.common;

import com.jingxiang.component.user.manager.common.service.impl.UserManagerServiceImpl;
import com.jingxiang.component.user.manager.common.service.impl.UserMemberServiceImpl;
import com.jingxiang.component.user.manager.common.service.impl.UserTenantServiceImpl;
import com.jingxiang.component.user.datasource.UserDataSourceConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Import;

/**
 * 共享用户管理核心组件自动配置（仅 Service，无 HTTP）
 * <p>
 * Mapper 绑定用户库 {@link UserDataSourceConfiguration#SQL_SESSION_FACTORY}。
 * 使用 {@link Import} 注册 Service，避免与 {@link ConditionalOnBean}（REGISTER_BEAN 阶段）冲突。
 *
 * @author chenjw
 */
@AutoConfiguration(after = UserDataSourceConfiguration.class)
@ConditionalOnBean(name = UserDataSourceConfiguration.SQL_SESSION_FACTORY)
@Import({
        UserManagerServiceImpl.class,
        UserMemberServiceImpl.class,
        UserTenantServiceImpl.class
})
@MapperScan(
        basePackages = "com.jingxiang.component.user.manager.common.dao",
        sqlSessionFactoryRef = UserDataSourceConfiguration.SQL_SESSION_FACTORY)
public class UserManagerCoreAutoConfiguration {
}
