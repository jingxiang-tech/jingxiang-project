package com.jingxiang.component.user.admin;

import com.jingxiang.component.user.datasource.UserDataSourceConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.ComponentScan;

/**
 * 统一用户管理端组件自动配置（仅 Service，无 HTTP）
 * <p>
 * Mapper 绑定用户库 {@link UserDataSourceConfiguration#SQL_SESSION_FACTORY}。
 *
 * @author chenjw
 */
@AutoConfiguration(after = UserDataSourceConfiguration.class)
@ConditionalOnBean(name = UserDataSourceConfiguration.SQL_SESSION_FACTORY)
@ComponentScan(basePackages = "com.jingxiang.component.user.admin.service")
@MapperScan(
        basePackages = "com.jingxiang.component.user.admin.dao",
        sqlSessionFactoryRef = UserDataSourceConfiguration.SQL_SESSION_FACTORY)
public class UserAdminConfiguration {
}
