package com.jingxiang.component.user.datasource;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import com.github.pagehelper.PageInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * 统一用户库独立数据源（与业务主库隔离）
 * <p>
 * 配置前缀：{@code jingxiang.component.user.datasource}，必填 {@code jdbc-url}。
 *
 * @author chenjw
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = "jingxiang.component.user.datasource", name = "jdbc-url")
@MapperScan(
        basePackages = "com.jingxiang.component.user.dao",
        sqlSessionFactoryRef = UserDataSourceConfiguration.SQL_SESSION_FACTORY)
public class UserDataSourceConfiguration {

    public static final String DATA_SOURCE = "userDataSource";
    public static final String SQL_SESSION_FACTORY = "userSqlSessionFactory";
    public static final String TRANSACTION_MANAGER = "userTransactionManager";

    @Bean(name = DATA_SOURCE)
    @ConfigurationProperties(prefix = "jingxiang.component.user.datasource")
    public DataSource userDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = TRANSACTION_MANAGER)
    public DataSourceTransactionManager userTransactionManager(
            @Qualifier(DATA_SOURCE) DataSource userDataSource) {
        return new DataSourceTransactionManager(userDataSource);
    }

    @Bean(name = SQL_SESSION_FACTORY)
    public MybatisSqlSessionFactoryBean userSqlSessionFactory(
            @Qualifier(DATA_SOURCE) DataSource userDataSource) {
        MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean();
        sessionFactory.setDataSource(userDataSource);

        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig()
                .setLogicDeleteField("deleted")
                .setLogicDeleteValue("1")
                .setLogicNotDeleteValue("0");
        sessionFactory.setGlobalConfig(new GlobalConfig().setDbConfig(dbConfig));

        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("reasonable", "false");
        properties.setProperty("pageSizeZero", "true");
        properties.setProperty("param", "pageNum=start;pageSize=limit");
        pageInterceptor.setProperties(properties);
        sessionFactory.setPlugins(pageInterceptor);

        MybatisConfiguration mybatisConfiguration = new MybatisConfiguration();
        mybatisConfiguration.setMapUnderscoreToCamelCase(true);
        sessionFactory.setConfiguration(mybatisConfiguration);

        return sessionFactory;
    }
}
