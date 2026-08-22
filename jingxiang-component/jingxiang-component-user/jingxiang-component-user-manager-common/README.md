# jingxiang-component-user-manager-common

共享用户 **Manager Core** 组件：提供用户列表、删除、禁用，以及租户和成员管理等通用能力。

**只提供 Service、DAO、数据模型与自动配置，不提供 Controller。**

该模块仅依赖 `jingxiang-component-user-common` 获取用户主表、通用用户服务、数据源和 JWT
能力，不依赖客户端第三方身份与登录注册能力。merchant/platform 两个 Starter 共同复用本模块。

## 引入

```xml
<dependency>
    <groupId>com.jingxiang</groupId>
    <artifactId>jingxiang-component-user-manager-common</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

自动配置：`UserManagerCoreAutoConfiguration`（在用户库 `userSqlSessionFactory` 可用后扫描
`com.jingxiang.component.user.manager.common.dao`）。

需同时配置 `jingxiang.component.user.datasource.jdbc-url`（见
`jingxiang-component-user-common` README）。

## 依赖关系

- `jingxiang-component-user-manager-common -> jingxiang-component-user-common`
- `jingxiang-component-user-manager-for-merchant -> jingxiang-component-user-manager-common`
- `jingxiang-component-user-manager-for-platform -> jingxiang-component-user-manager-common`

merchant/platform 通过 manager-common 传递获得 common，不直接依赖 for-app。

## 核心 Service

- `UserManagerService`：list / delete / setForbidden
- `UserTenantService`：create / ensureByCode / update / getByCode …
- `UserMemberService`：add / remove / list / belongsTo …

## 租户约定

统一使用 `tenantCode` / `tenantId`。业务系统 Session 应携带这两个字段，不要再把 `mctNo` 当作租户主键语义。
