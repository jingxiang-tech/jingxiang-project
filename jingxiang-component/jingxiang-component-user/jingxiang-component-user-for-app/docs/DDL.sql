-- 必须先执行 jingxiang-component-user-common/docs/DDL.sql
-- （创建 user / user_tenant / user_member）
-- 本文件仅包含 user_identity，在独立库 user_center 中执行（勿建到各业务库）
-- CREATE DATABASE IF NOT EXISTS user_center DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- USE user_center;

CREATE TABLE `user_identity` (
  `identity_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '第三方身份ID',

  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',

  `identity_type` VARCHAR(32) NOT NULL COMMENT
    '身份类型：WECHAT_MP/ALIPAY_MP/DOUYIN/APPLE/GOOGLE等',

  `app_key` VARCHAR(128) NOT NULL DEFAULT '' COMMENT
    '第三方平台应用标识，如微信AppId',

  `identifier` VARCHAR(255) NOT NULL COMMENT
    '第三方身份标识，如openid/userId/sub',

  `union_id` VARCHAR(128) DEFAULT NULL COMMENT
    '跨应用统一身份，如微信UnionId',

  `nickname` VARCHAR(64) DEFAULT NULL COMMENT '第三方昵称',
  `avatar` VARCHAR(512) DEFAULT NULL COMMENT '第三方头像',

  `forbidden` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否禁用：0否 1是',

  `latest_login_at` DATETIME DEFAULT NULL COMMENT '最近登录时间',

  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',

  `deleted` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除：0否 1是',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',

  PRIMARY KEY (`identity_id`),

  UNIQUE KEY `uk_identity`
    (`identity_type`, `app_key`, `identifier`),

  KEY `idx_identity_user_id` (`user_id`),
  KEY `idx_identity_union_id` (`union_id`),

  CONSTRAINT `fk_identity_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`user_id`)

) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci
COMMENT='用户第三方身份';
