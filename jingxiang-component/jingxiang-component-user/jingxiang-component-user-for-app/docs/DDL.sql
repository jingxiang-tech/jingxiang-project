-- 在独立库 user_center 中执行（勿建到各业务库）
-- CREATE DATABASE IF NOT EXISTS user_center DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- USE user_center;

CREATE TABLE `user` (
  `user_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户ID',

  `tel` VARCHAR(32) DEFAULT NULL COMMENT '手机号',
  `user_name` VARCHAR(32) DEFAULT NULL COMMENT '用户名',
  `nickname` VARCHAR(32) DEFAULT NULL COMMENT '昵称',
  `real_name` VARCHAR(32) DEFAULT NULL COMMENT '真实姓名',
  `pwd` VARCHAR(128) DEFAULT NULL COMMENT 'BCrypt密码Hash',
  `gender` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '性别：0未知 1男 2女',
  `avatar` VARCHAR(512) DEFAULT NULL COMMENT '头像',
  `email` VARCHAR(128) DEFAULT NULL COMMENT '邮箱',

  `forbidden` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否禁用：0否 1是',

  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',

  `deleted` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除：0否 1是',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',

  PRIMARY KEY (`user_id`),

  UNIQUE KEY `uk_user_tel` (`tel`),
  UNIQUE KEY `uk_user_email` (`email`),
  UNIQUE KEY `uk_user_name` (`user_name`),

  KEY `idx_user_created_at` (`created_at`)

) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci
COMMENT='统一用户';

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

CREATE TABLE `user_tenant` (
  `tenant_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '租户/组织ID',

  `tenant_type` VARCHAR(32) NOT NULL COMMENT
    '组织类型：PLATFORM/MERCHANT/ENTERPRISE/AGENCY/SERVICE_PROVIDER',

  `tenant_code` VARCHAR(64) NOT NULL COMMENT '组织编码',

  `tenant_name` VARCHAR(128) NOT NULL COMMENT '组织名称',

  `parent_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '上级组织ID',

  `logo` VARCHAR(512) DEFAULT NULL COMMENT '组织Logo',

  `forbidden` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否禁用：0否 1是',

  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',

  `deleted` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除：0否 1是',

  `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',

  PRIMARY KEY (`tenant_id`),

  UNIQUE KEY `uk_user_tenant_code` (`tenant_code`),

  KEY `idx_user_tenant_type` (`tenant_type`),
  KEY `idx_user_tenant_parent_id` (`parent_id`)

) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci
COMMENT='用户体系租户/组织';

CREATE TABLE `user_member` (
  `member_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '成员ID',

  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `tenant_id` BIGINT UNSIGNED NOT NULL COMMENT '租户/组织ID',
  `member_type` VARCHAR(32) NOT NULL DEFAULT 'MEMBER' COMMENT
    '成员类型：OWNER/ADMIN/OPERATOR/FINANCE/MEMBER等，由程序定义',

  `member_name` VARCHAR(64) DEFAULT NULL COMMENT '组织内显示名称',

  `forbidden` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否禁用：0否 1是',

  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',

  `deleted` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除：0否 1是',

  `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',

  PRIMARY KEY (`member_id`),

  UNIQUE KEY `uk_user_member_user_tenant`
    (`user_id`, `tenant_id`),

  KEY `idx_user_member_tenant_id`
    (`tenant_id`),

  CONSTRAINT `fk_user_member_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`user_id`),

  CONSTRAINT `fk_user_member_tenant`
    FOREIGN KEY (`tenant_id`)
    REFERENCES `user_tenant` (`tenant_id`)

) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci
COMMENT='用户组织成员关系';
