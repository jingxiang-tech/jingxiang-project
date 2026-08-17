# lynx 迁移说明

## 决策

1. 组件**只提供 Service**，无 Controller
2. 各端自签 JWT，共享用户四表
3. 租户统一 `tenantCode` / `tenantId`（Session 不再使用 `mctNo` 语义）
4. 不做历史兼容，直接替换

## 映射

| 旧 | 新 |
|----|----|
| `merchant_user` | `user` + `user_member` |
| `merchant`/`mctNo` 作租户 | `user_tenant.tenantCode` |
| `clip_user` | `user` + `user_member`（租户 `WANYU_AI_COMMERCE`） |
| `platform_admin` | 不纳入 |

## 切流

- lynx-app / lynx-ms / lynx-platform：Service 注入组件
- lynx-ms 员工 list/detail/update/delete：`UserMemberService` + `UserService`
- lynx-ms Session：`tenantId` / `tenantCode` / `tenantName`
- wanyu-app / wanyu-ms：去掉 `clip_user`，认证走组件

## 业务表 `mct_no` 列

业务数据表仍可保留 `mct_no` 字段名，写入时用 `SessionUtil.getTenantCode()` 的值（与租户编码一致）。新代码 Session API 一律用 `tenantCode`。
