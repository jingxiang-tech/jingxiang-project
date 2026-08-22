package com.jingxiang.component.user.manager.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jingxiang.commons.util.convert.ListTypeHandler;
import com.jingxiang.component.user.manager.common.model.member.UserMemberBrief;
import com.jingxiang.component.user.manager.common.model.member.UserMemberPo;
import com.jingxiang.component.user.manager.common.model.member.UserMemberQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 成员 Mapper
 *
 * @author chenjw
 */
@Mapper
public interface UserMemberMapper extends BaseMapper<UserMemberPo> {

    @Results(id = "UserMemberBriefMap", value = {
            @Result(property = "roleCodeList", column = "role_code_list", typeHandler = ListTypeHandler.class)
    })
    @Select("""
            <script>
            SELECT
                m.member_id,
                m.user_id,
                m.tenant_id,
                m.role_code_list,
                m.role_name_desc,
                u.nickname,
                u.user_name,
                u.real_name,
                u.avatar,
                u.email,
                u.tel,
                t.tenant_name,
                t.tenant_code,
                u.forbidden AS user_forbidden,
                u.remark AS user_remark,
                u.created_at AS user_created_at,
                u.updated_at AS user_updated_at,
                m.forbidden,
                m.remark,
                m.created_at
            FROM user_member m
            INNER JOIN user u ON u.user_id = m.user_id AND u.deleted = 0
            INNER JOIN user_tenant t ON t.tenant_id = m.tenant_id AND t.deleted = 0
            WHERE m.deleted = 0
              AND m.member_id = #{memberId}
            LIMIT 1
            </script>
            """)
    UserMemberBrief detail(@Param("memberId") Long memberId);

    @Result(property = "roleCodeList", column = "role_code_list", typeHandler = ListTypeHandler.class)
    @Select("""
            <script>
            SELECT
                m.member_id,
                m.user_id,
                m.tenant_id,
                m.role_code_list,
                m.role_name_desc,
                u.nickname,
                u.user_name,
                u.real_name,
                u.avatar,
                u.email,
                u.tel,
                t.tenant_name,
                t.tenant_code,
                u.forbidden AS user_forbidden,
                u.remark AS user_remark,
                u.created_at AS user_created_at,
                u.updated_at AS user_updated_at,
                m.forbidden,
                m.remark,
                m.created_at
            FROM user_member m
            INNER JOIN user u ON u.user_id = m.user_id AND u.deleted = 0
            INNER JOIN user_tenant t ON t.tenant_id = m.tenant_id AND t.deleted = 0
            WHERE m.deleted = 0
            <if test="tenantId != null">
                AND m.tenant_id = #{tenantId}
            </if>
            <if test="tenantCode != null and tenantCode != ''">
                AND t.tenant_code = #{tenantCode}
            </if>
            <if test="memberUserId != null">
                AND m.user_id = #{memberUserId}
            </if>
            <if test="userIds != null and userIds.size() > 0">
                AND m.user_id IN
                <foreach collection="userIds" item="userId" open="(" separator="," close=")">
                    #{userId}
                </foreach>
            </if>
            <if test="roleCode != null and roleCode != ''">
                AND JSON_CONTAINS(m.role_code_list, JSON_QUOTE(#{roleCode}))
            </if>
            <if test="forbidden != null">
                AND m.forbidden = #{forbidden}
            </if>
            <if test="keyword != null and keyword != ''">
                AND (u.user_name LIKE CONCAT('%', #{keyword}, '%')
                  OR u.tel LIKE CONCAT('%', #{keyword}, '%')
                  OR u.nickname LIKE CONCAT('%', #{keyword}, '%')
                  OR m.role_name_desc LIKE CONCAT('%', #{keyword}, '%'))
            </if>
            ORDER BY m.member_id DESC
            </script>
            """)
    List<UserMemberBrief> list(UserMemberQuery query);

    @Results(id = "UserMemberPoMap", value = {
            @Result(property = "roleCodeList", column = "role_code_list", typeHandler = ListTypeHandler.class)
    })
    @Select("""
            <script>
            SELECT
                member_id,
                user_id,
                tenant_id,
                role_code_list,
                role_name_desc,
                forbidden,
                created_at,
                updated_at,
                deleted,
                remark
            FROM user_member
            WHERE deleted = 0
              AND user_id = #{userId}
              AND tenant_id = #{tenantId}
            LIMIT 1
            </script>
            """)
    UserMemberPo findByUserAndTenant(@Param("userId") Long userId, @Param("tenantId") Long tenantId);

    @Result(property = "roleCodeList", column = "role_code_list", typeHandler = ListTypeHandler.class)
    @Select("""
            <script>
            SELECT
                m.member_id,
                m.user_id,
                m.tenant_id,
                m.role_code_list,
                m.role_name_desc,
                u.nickname,
                u.user_name,
                u.real_name,
                u.avatar,
                u.email,
                u.tel,
                t.tenant_name,
                t.tenant_code,
                m.forbidden,
                m.created_at
            FROM user_member m
            INNER JOIN user u ON u.user_id = m.user_id AND u.deleted = 0
            INNER JOIN user_tenant t ON t.tenant_id = m.tenant_id AND t.deleted = 0
            WHERE m.deleted = 0
              AND m.user_id = #{userId}
              AND m.forbidden = 0
              AND t.forbidden = 0
            ORDER BY m.member_id DESC
            </script>
            """)
    List<UserMemberBrief> listTenantsByUserId(@Param("userId") Long userId);
}
