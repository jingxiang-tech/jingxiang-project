# jingxiang-component-user-admin

统一用户**管理端**组件：用户列表 / 删除 / 禁用，以及租户、成员、第三方身份管理。

**只提供 Service，不提供 Controller。**

依赖 `jingxiang-component-user`。

## 引入

```xml
<dependency>
    <groupId>com.jingxiang</groupId>
    <artifactId>jingxiang-component-user-admin</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

自动配置：`UserAdminConfiguration`（在用户库 `userSqlSessionFactory` 可用后扫描 admin.dao）。  
需同时配置 `jingxiang.component.user.datasource.jdbc-url`（见 `jingxiang-component-user` README）。

## 核心 Service

- `UserAdminService`：list / delete / setForbidden
- `UserTenantService`：create / ensureByCode / update / getByCode …
- `UserMemberService`：add / remove / list / belongsTo …
- `UserIdentity` 管理能力见 admin 包内 Identity 相关 Service

## 租户约定

统一使用 `tenantCode` / `tenantId`。业务系统 Session 应携带这两个字段，不要再把 `mctNo` 当作租户主键语义。
