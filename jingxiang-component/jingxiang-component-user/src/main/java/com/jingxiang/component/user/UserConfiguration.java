package com.jingxiang.component.user;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 统一用户基础组件自动配置（仅 Service，无 HTTP）
 * <p>
 * Mapper 与用户库数据源见 {@link com.jingxiang.component.user.datasource.UserDataSourceConfiguration}。
 *
 * @author chenjw
 */
@Configuration
@ComponentScan(basePackages = "com.jingxiang.component.user.service")
public class UserConfiguration {
}
