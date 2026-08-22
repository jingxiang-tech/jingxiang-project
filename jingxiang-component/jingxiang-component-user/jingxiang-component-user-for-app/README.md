# jingxiang-component-user-for-app

客户端/小程序用户专属组件：第三方身份、登录注册与绑手机。

**只提供 Service，不提供 Controller。** 各业务系统自持 JWT/Session 与 HTTP 编排。

本模块依赖 `jingxiang-component-user-common`。用户主表、通用用户服务、数据源、密码和 JWT
均由 common 提供；本模块不重复包含这些 FQCN。

## 引入

```xml
<dependency>
    <groupId>com.jingxiang</groupId>
    <artifactId>jingxiang-component-user-for-app</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

自动配置：
- common 自动配置用户数据源、`UserServiceImpl` 与 JWT
- `UserConfiguration`：在 `UserCoreAutoConfiguration` 之后注册 `UserAuthServiceImpl` 和
  `UserIdentityServiceImpl`

### 用户库数据源（必配）

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

建表须先执行 [common/docs/DDL.sql](../jingxiang-component-user-common/docs/DDL.sql)
（`user` / `user_tenant` / `user_member`），再执行本模块 [docs/DDL.sql](docs/DDL.sql)
（仅 `user_identity`）。均在 **user_center** 执行，不要建到业务库。

## 核心 Service

- `UserAuthService`：loginOrRegisterByIdentity、bindMobile
- `UserIdentityService`：身份绑定与查询
- `MobileVerifyPort`：由宿主实现手机验证码校验

`UserService` 由 `jingxiang-component-user-common` 提供。

## JWT / Session（来自 common）

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

1. 共享同一套用户库（common 的 user/user_tenant/user_member + 本模块的 user_identity，或只读副本）
2. 各端自签 JWT，不共享 Session
3. 租户统一 `user_tenant.tenantCode` / `tenantId`，业务 Session 使用 `tenantCode`，不再使用 `mctNo` 作为租户语义
4. HTTP 由业务系统 Controller 编排后调用上述 Service

## 密码

仅 BCrypt。

## 管理端

组件结构如下：

- `jingxiang-component-user-common`：用户通用核心，供 for-app 与 manager-common 共同依赖
- `jingxiang-component-user-manager-common`：共享管理核心，仅依赖 common
- `jingxiang-component-user-manager-for-merchant`：商户后台用户接口、会话、访问日志与业务端口
- `jingxiang-component-user-manager-for-platform`：平台端商户用户 CRUD、空间授权和 OWNER 开户编排

商户端和平台端分别依赖对应 Starter；两个 Starter 都通过 Port 访问宿主业务库，不扫描宿主 Mapper。
跨用户库与业务库的流程不得由单个 `@Transactional` 包裹，使用本地事务、幂等写入和失败补偿。
