-- lynx merchant_user → 统一用户表迁移（请先备份）
-- 要求：保持 user_id 与 merchant_user.user_id 一致

-- 1. 商户 → 租户
INSERT INTO user_tenant (
    tenant_type, tenant_code, tenant_name, forbidden, created_at, updated_at, deleted, remark
)
SELECT
    'MERCHANT',
    m.mct_no,
    m.mct_name,
    0,
    IFNULL(m.create_time, NOW()),
    IFNULL(m.update_time, NOW()),
    IFNULL(m.deleted, 0),
    m.remark
FROM merchant m
WHERE NOT EXISTS (
    SELECT 1 FROM user_tenant t WHERE t.tenant_code = m.mct_no
);

-- 2. 商户用户 → 统一用户
-- 注意：pwd 不要拷贝 MD5。先置空或写入已知明文的 BCrypt，再通知用户改密/重置。
INSERT INTO user (
    user_id, tel, user_name, nickname, real_name, pwd, gender, avatar, email,
    forbidden, created_at, updated_at, deleted, remark
)
SELECT
    mu.user_id,
    NULLIF(mu.tel, ''),
    NULLIF(mu.user_name, ''),
    mu.nickname,
    mu.real_name,
    NULL,
    0,
    mu.avatar,
    NULLIF(mu.email, ''),
    IFNULL(mu.disabled, 0),
    IFNULL(mu.create_time, NOW()),
    IFNULL(mu.update_time, NOW()),
    IFNULL(mu.deleted, 0),
    mu.remark
FROM merchant_user mu
WHERE NOT EXISTS (
    SELECT 1 FROM user u WHERE u.user_id = mu.user_id
);

-- 修正自增起点，避免后续插入冲突
SELECT @max_uid := IFNULL(MAX(user_id), 0) FROM user;
SET @sql = CONCAT('ALTER TABLE user AUTO_INCREMENT = ', @max_uid + 1);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 3. 用户-商户成员（按 mct_no 去重；member_type 默认 MEMBER，OWNER 可后续人工调整）
INSERT INTO user_member (
    user_id, tenant_id, member_type, member_name, forbidden, created_at, updated_at, deleted
)
SELECT DISTINCT
    mus.user_id,
    t.tenant_id,
    CASE
        WHEN mus.admin = 1 THEN 'ADMIN'
        ELSE 'MEMBER'
    END,
    NULL,
    0,
    IFNULL(mus.create_time, NOW()),
    NOW(),
    0
FROM merchant_user_space mus
INNER JOIN user_tenant t ON t.tenant_code = mus.mct_no AND t.deleted = 0
WHERE NOT EXISTS (
    SELECT 1 FROM user_member m
    WHERE m.user_id = mus.user_id AND m.tenant_id = t.tenant_id AND m.deleted = 0
);
