# jingxiang-component-user-for-app

客户端/小程序用户**基础**组件：登录校验 / 注册 / 个人信息 / 第三方登录与绑手机。

**只提供 Service，不提供 Controller。** 各业务系统自持 JWT/Session 与 HTTP 编排。

不含管理端列表、删除、禁用；共享管理能力见 `jingxiang-component-user-admin-core`。

## 引入

```xml
<dependency>
    <groupId>com.jingxiang</groupId>
    <artifactId>jingxiang-component-user-for-app</artifactId>
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

拆分后的正式结构如下：

- `jingxiang-component-user-admin-core`：共享管理核心，提供用户、租户和成员管理能力，并传递依赖本模块
- `jingxiang-component-user-admin-for-merchant`：商户后台用户接口、会话、访问日志与业务端口
- `jingxiang-component-user-admin-for-platform`：平台端商户用户 CRUD、空间授权和 OWNER 开户编排

商户端和平台端分别依赖对应 Starter；两个 Starter 都通过 Port 访问宿主业务库，不扫描宿主 Mapper。
跨用户库与业务库的流程不得由单个 `@Transactional` 包裹，使用本地事务、幂等写入和失败补偿。
