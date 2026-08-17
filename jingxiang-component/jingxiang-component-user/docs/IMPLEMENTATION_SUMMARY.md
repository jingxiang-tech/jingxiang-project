# jingxiang-component-user 实现总结

## 模块

| 模块 | 职责 |
|------|------|
| `jingxiang-component-user` | 注册/登录校验/资料/改密/第三方登录绑手机 |
| `jingxiang-component-user-admin` | 用户列表删禁、租户、成员、Identity |

**仅 Service，无 HTTP Controller。**

## 接入方式

1. Maven 依赖对应模块
2. 业务系统自写 Controller + 自签 JWT
3. 共享用户四表；Session 使用 `tenantId` / `tenantCode`

## lynx / wanyu

| 系统 | 依赖 | 说明 |
|------|------|------|
| lynx-app | user | 登录编排 |
| lynx-ms | user-admin | 员工/成员走 Member；Session=`tenantCode` |
| lynx-platform | user-admin | 建商户 Tenant + Owner |
| wanyu-app/ms | user-admin | 去掉 `clip_user`，租户码 `WANYU_AI_COMMERCE` |

## 密码

仅 BCrypt。
