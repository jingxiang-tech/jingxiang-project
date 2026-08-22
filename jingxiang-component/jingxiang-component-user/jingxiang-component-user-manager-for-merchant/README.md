# jingxiang-component-user-manager-for-merchant

商户后台用户管理 Starter，依赖 `jingxiang-component-user-manager-common`，提供：

- `/system/user/**` 登录、选空间、员工 CRUD、密码和用户字典接口。
- `/merchant/space-user/**` 当前空间用户的创建、列表、启停和重置密码接口。
- `/user-member/**` 组织成员创建、列表、启停和重置密码。
  `UserMemberAutoConfiguration` 只依赖 JWT 与用户库服务，不要求两个 Port。
- `/system/role/dict` 角色字典接口。
- 商户会话校验、访问日志和异常响应。
- `MerchantDirectoryPort`、`MerchantUserSpacePort` 两个宿主业务库端口。

`/system/**` 与 `/merchant/space-user/**` 需要宿主实现两个 Port，并配置
`jingxiang.component.user.jwt` 和 `jingxiang.component.user.manager.merchant`。
自动配置不会扫描宿主 Mapper。

统一用户库写操作使用 `userTransactionManager`；业务空间关系由 Port 在宿主业务库本地事务中完成。
新增、删除员工等跨库流程不使用单个 `@Transactional`，而是按固定顺序执行，并对本次新增或删除的
关系做精确补偿。Port 的绑定、恢复和删除实现必须保持幂等。
