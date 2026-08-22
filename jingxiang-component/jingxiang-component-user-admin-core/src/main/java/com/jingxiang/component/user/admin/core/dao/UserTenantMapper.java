package com.jingxiang.component.user.admin.core.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jingxiang.component.user.admin.core.model.tenant.UserTenantBrief;
import com.jingxiang.component.user.admin.core.model.tenant.UserTenantPo;
import com.jingxiang.component.user.admin.core.model.tenant.UserTenantQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 租户 Mapper
 *
 * @author chenjw
 */
@Mapper
public interface UserTenantMapper extends BaseMapper<UserTenantPo> {

    @Select("""
            <script>
            SELECT
                tenant_id,
                tenant_type,
                tenant_code,
                tenant_name,
                parent_id,
                logo,
                forbidden,
                created_at,
                remark
            FROM user_tenant
            WHERE deleted = 0
              AND tenant_id = #{tenantId}
            LIMIT 1
            </script>
            """)
    UserTenantBrief detail(@Param("tenantId") Long tenantId);

    @Select("""
            <script>
            SELECT
                tenant_id,
                tenant_type,
                tenant_code,
                tenant_name,
                parent_id,
                logo,
                forbidden,
                created_at,
                remark
            FROM user_tenant
            WHERE deleted = 0
              AND tenant_code = #{tenantCode}
            LIMIT 1
            </script>
            """)
    UserTenantBrief findByCode(@Param("tenantCode") String tenantCode);

    @Select("""
            <script>
            SELECT
                tenant_id,
                tenant_type,
                tenant_code,
                tenant_name,
                parent_id,
                logo,
                forbidden,
                created_at,
                remark
            FROM user_tenant
            WHERE deleted = 0
            <if test="tenantType != null">
                AND tenant_type = #{tenantType}
            </if>
            <if test="tenantCode != null and tenantCode != ''">
                AND tenant_code = #{tenantCode}
            </if>
            <if test="tenantName != null and tenantName != ''">
                AND tenant_name LIKE CONCAT('%', #{tenantName}, '%')
            </if>
            <if test="parentId != null">
                AND parent_id = #{parentId}
            </if>
            <if test="forbidden != null">
                AND forbidden = #{forbidden}
            </if>
            ORDER BY tenant_id DESC
            </script>
            """)
    List<UserTenantBrief> list(UserTenantQuery query);

    @Select("""
            <script>
            SELECT
                tenant_id,
                tenant_type,
                tenant_code,
                tenant_name,
                parent_id,
                logo,
                forbidden,
                created_at,
                remark
            FROM user_tenant
            WHERE deleted = 0
              AND parent_id = #{parentId}
            ORDER BY tenant_id ASC
            </script>
            """)
    List<UserTenantBrief> listByParentId(@Param("parentId") Long parentId);
}
