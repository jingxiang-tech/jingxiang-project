# jingxiang-component-user

统一用户**基础**组件：登录校验 / 注册 / 个人信息 / 第三方登录与绑手机。

**只提供 Service，不提供 Controller。** 各业务系统自持 JWT/Session 与 HTTP 编排。

不含管理端列表、删除、禁用；管理能力见 `jingxiang-component-user-admin`。

## 引入

```xml
<dependency>
    <groupId>com.jingxiang</groupId>
    <artifactId>jingxiang-component-user</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

自动配置：
- `UserDataSourceConfiguration`：独立用户库 DataSource / SqlSessionFactory / TransactionManager，并扫描 `user.dao`
- `UserConfiguration`：Service 扫描
- `JwtAutoConfiguration`：有 `jingxiang.component.user.jwt.secret` 时装配 JWT

### 用户库数据源（必配）

用户四表落在独立库（建议库名 `user_center`），与业务主库隔离。各端配置同一套连接：

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

## 核心 Service

- `UserService`：create / update / detail / changePassword / getBy* / authenticate
- `UserAuthService`：loginOrRegisterByIdentity、bindMobile
- `UserIdentityService`：身份绑定与查询

## JWT / Session（通用抽象）

包：`com.jingxiang.component.user.jwt`

| 类 | 说明 |
|----|------|
| `JwtProperties` | secret / issuer / audience / expire / tokenVersion（由各端 `application.yml` 绑定） |
| `JwtCodec` | 签发 / 验签 / 反序列化 |
| `BaseSessionUser` | 通用会话字段（userId、tenantCode…） |
| `SessionSupport<T>` | 读 Header → 验签 → 版本校验 → 可选 `SessionValidator` |
| `SessionValidator<T>` | 子系统附加校验（如查库禁用） |

各端只需声明 `SessionSupport`（+ 可选校验）；`JwtProperties` / `JwtCodec` 由 `JwtAutoConfiguration` 自动装配。

示例（`application.yml`）：

```yaml
jingxiang:
  component:
    user:
      jwt:
        secret: your-secret
        issuer: your-issuer
        audience: audience
        expire-millis: 31536000000
        token-version: 1.5
```

应用侧配置示例：

```java
@Bean
public SessionSupport<SessionUser> sessionSupport(JwtCodec jwtCodec) {
    return new SessionSupport<>(jwtCodec, SessionUser.class, XxxSessionUnavailableException::new);
}
```

## 接入约定

1. 共享同一套用户四表（或只读副本）
2. 各端自签 JWT，不共享 Session
3. 租户统一 `user_tenant.tenantCode` / `tenantId`，业务 Session 使用 `tenantCode`，不再使用 `mctNo` 作为租户语义
4. HTTP 由业务系统 Controller 编排后调用上述 Service

## 密码

仅 BCrypt。

## 管理端

平台/后台请依赖：`jingxiang-component-user-admin`（已传递依赖本模块）。
