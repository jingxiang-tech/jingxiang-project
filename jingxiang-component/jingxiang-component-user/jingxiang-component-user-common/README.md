# jingxiang-component-user-common

用户通用核心组件，供客户端与管理端共同依赖，不提供 Controller。

## 职责

- 用户库独立数据源、事务注解及 `com.jingxiang.component.user.dao` Mapper 扫描
- 用户主表 Mapper、用户模型、`GenderEnum`
- `UserService` / `UserServiceImpl`
- `PasswordUtil`
- 通用 JWT / Session 抽象

## 自动配置

- `UserDataSourceConfiguration`：创建 `userDataSource`、`userSqlSessionFactory`、
  `userTransactionManager`，扫描 `com.jingxiang.component.user.dao`
- `UserCoreAutoConfiguration`：在用户库 SqlSessionFactory 就绪后注册 `UserServiceImpl`
- `JwtAutoConfiguration`：配置了 `jingxiang.component.user.jwt.secret` 时装配 JWT

Mapper 扫描按包名工作。客户端同时依赖本模块与 `jingxiang-component-user-for-app` 时，
能够扫描 common 中的 `UserMapper` 和 for-app 中的 `UserIdentityMapper`。

## 依赖关系

`jingxiang-component-user-for-app` 和 `jingxiang-component-user-admin-core` 均直接依赖本模块。
管理端 merchant/platform 通过 admin-core 传递依赖本模块。

写操作使用 `@UserTransactional`，确保绑定用户库事务管理器。

## 用户库数据源（必配）

用户相关表落在独立库（建议库名 `user_center`），与业务主库隔离。各端配置同一套连接：

```yaml
jingxiang:
  component:
    user:
      datasource:
        jdbc-url: jdbc:mysql://host:3306/user_center?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true
        username: ***
        password: ***
        driver-class-name: com.mysql.cj.jdbc.Driver
        type: com.zaxxer.hikari.HikariDataSource
```

组件 Bean 名：`userDataSource` / `userSqlSessionFactory` / `userTransactionManager`。
写操作请使用 `@UserTransactional`（已绑定用户库事务管理器），勿依赖业务库 `@Primary` 事务。

建表见 [docs/DDL.sql](docs/DDL.sql)（在 **user_center** 执行，不要建到业务库）。
common DDL 包含 `user`、`user_tenant`、`user_member`。客户端第三方身份表 `user_identity` 见
`jingxiang-component-user-for-app` 的 DDL，须在本文件之后执行。
