# jingxiang-component-user-admin-for-merchant

商户后台用户管理 Starter，依赖 `jingxiang-component-user-admin-core`，提供：

- `/system/user/**` 登录、选空间、员工 CRUD、密码和用户字典接口。
- `/system/role/dict` 角色字典接口。
- 商户会话校验、访问日志和异常响应。
- `MerchantDirectoryPort`、`MerchantUserSpacePort` 两个宿主业务库端口。

宿主必须实现两个 Port，并配置 `jingxiang.component.user.jwt` 和
`jingxiang.component.user.admin.merchant`。自动配置不会扫描宿主 Mapper。

统一用户库写操作使用 `userTransactionManager`；业务空间关系由 Port 在宿主业务库本地事务中完成。
新增、删除员工等跨库流程不使用单个 `@Transactional`，而是按固定顺序执行，并对本次新增或删除的
关系做精确补偿。Port 的绑定、恢复和删除实现必须保持幂等。
