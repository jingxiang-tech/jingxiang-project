package com.jingxiang.component.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jingxiang.component.user.dict.IdentityTypeEnum;
import com.jingxiang.component.user.model.identity.UserIdentityBrief;
import com.jingxiang.component.user.model.identity.UserIdentityPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户第三方身份 Mapper
 *
 * @author chenjw
 */
@Mapper
public interface UserIdentityMapper extends BaseMapper<UserIdentityPo> {

    @Select("""
            <script>
            SELECT
                identity_id,
                user_id,
                identity_type,
                app_key,
                identifier,
                union_id,
                nickname,
                avatar,
                forbidden,
                latest_login_at,
                created_at,
                updated_at,
                deleted,
                remark
            FROM user_identity
            WHERE deleted = 0
              AND identity_type = #{identityType}
              AND app_key = #{appKey}
              AND identifier = #{identifier}
            LIMIT 1
            </script>
            """)
    UserIdentityPo findByIdentity(@Param("identityType") IdentityTypeEnum identityType,
                                  @Param("appKey") String appKey,
                                  @Param("identifier") String identifier);

    @Select("""
            <script>
            SELECT
                identity_id,
                user_id,
                identity_type,
                app_key,
                identifier,
                union_id,
                nickname,
                avatar,
                forbidden,
                latest_login_at,
                created_at
            FROM user_identity
            WHERE deleted = 0
              AND user_id = #{userId}
            ORDER BY identity_id DESC
            </script>
            """)
    List<UserIdentityBrief> listByUserId(@Param("userId") Long userId);

    @Select("""
            <script>
            SELECT
                identity_id,
                user_id,
                identity_type,
                app_key,
                identifier,
                union_id,
                nickname,
                avatar,
                forbidden,
                latest_login_at,
                created_at
            FROM user_identity
            WHERE deleted = 0
              AND identity_id = #{identityId}
            LIMIT 1
            </script>
            """)
    UserIdentityBrief detail(@Param("identityId") Long identityId);
}
