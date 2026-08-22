# jingxiang-component-user-admin-for-platform

平台端商户用户管理 Starter，提供以下能力：

- 保持 `/platform/merchant-user/**` 用户管理接口。
- 提供 `/platform/merchant-space/{spaceId}/users` 查询接口。
- 通过 `PlatformMerchantDirectoryPort`、`PlatformMerchantUserSpacePort` 访问宿主业务库。
- 通过 `PlatformOperatorPort` 获取当前平台操作人。
- 提供 `PlatformMerchantOnboardingService`，按“用户库开户 → 业务库授权”顺序完成 OWNER 开户。

组件不依赖 Lynx 业务模型、业务数据源、平台登录、JWT 或拦截器。宿主必须提供上述三个 Port，
自动配置才会生效。跨用户库与业务库不使用 `@Transactional` 模拟分布式事务，开户失败会补偿
本次新建的用户和成员；按商户号创建的用户租户具有幂等性并保留。用户删除先保存并移除业务库
空间关系，再删除统一用户；统一用户删除失败时通过幂等 `restore` 恢复原空间和管理员标记。
