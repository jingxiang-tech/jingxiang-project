package com.jingxiang.component.user;

import com.jingxiang.component.user.datasource.UserDataSourceConfiguration;
import com.jingxiang.component.user.service.impl.UserServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Import;

/**
 * 用户通用核心自动配置（仅 Service，无 HTTP）
 * <p>
 * Mapper 与用户库数据源见 {@link UserDataSourceConfiguration}。
 *
 * @author chenjw
 */
@AutoConfiguration(after = UserDataSourceConfiguration.class)
@ConditionalOnBean(name = UserDataSourceConfiguration.SQL_SESSION_FACTORY)
@Import(UserServiceImpl.class)
public class UserCoreAutoConfiguration {
}
