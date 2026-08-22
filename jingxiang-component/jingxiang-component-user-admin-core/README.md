# jingxiang-component-user-admin-core

共享用户 **Admin Core** 组件：提供用户列表、删除、禁用，以及租户和成员管理等通用能力。

**只提供 Service、DAO、数据模型与自动配置，不提供 Controller。**

该模块依赖 `jingxiang-component-user`，由 merchant/platform 两个 Starter 共同复用。

## 引入

```xml
<dependency>
    <groupId>com.jingxiang</groupId>
    <artifactId>jingxiang-component-user-admin-core</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

自动配置：`UserAdminCoreAutoConfiguration`（在用户库 `userSqlSessionFactory` 可用后扫描
`com.jingxiang.component.user.admin.core.dao`）。

需同时配置 `jingxiang.component.user.datasource.jdbc-url`（见 `jingxiang-component-user` README）。

## 核心 Service

- `UserAdminService`：list / delete / setForbidden
- `UserTenantService`：create / ensureByCode / update / getByCode …
- `UserMemberService`：add / remove / list / belongsTo …

## 租户约定

统一使用 `tenantCode` / `tenantId`。业务系统 Session 应携带这两个字段，不要再把 `mctNo` 当作租户主键语义。
