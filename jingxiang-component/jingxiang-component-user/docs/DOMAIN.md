# 统一用户领域模型

## 模块拆分

| 模块 | 职责 |
|------|------|
| `jingxiang-component-user` | 登录/注册/个人资料/第三方登录绑手机 |
| `jingxiang-component-user-admin` | 用户列表删禁、租户、成员、身份管理 |

## 关系

```
User（自然人）
├── UserIdentity（第三方身份，N:1）
└── UserMember（组织成员，N:1）── UserTenant
```

C 端通常只需 User + Identity；B 端管理依赖 admin 模块的 Tenant/Member。

## deleted vs forbidden

| 字段 | 语义 |
|------|------|
| deleted | 逻辑删除 |
| forbidden | 禁用（管理端接口维护） |
