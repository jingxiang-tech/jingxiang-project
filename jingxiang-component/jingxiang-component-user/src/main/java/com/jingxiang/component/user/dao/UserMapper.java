package com.jingxiang.component.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jingxiang.component.user.model.user.UserBrief;
import com.jingxiang.component.user.model.user.UserDetail;
import com.jingxiang.component.user.model.user.UserPo;
import com.jingxiang.component.user.model.user.UserQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户 Mapper
 *
 * @author chenjw
 */
@Mapper
public interface UserMapper extends BaseMapper<UserPo> {

    @Select("""
            <script>
            SELECT
                user_id,
                tel,
                user_name,
                nickname,
                real_name,
                gender,
                avatar,
                email,
                forbidden,
                created_at,
                updated_at,
                remark
            FROM user
            WHERE user_id = #{userId}
              AND deleted = 0
            </script>
            """)
    UserDetail detail(@Param("userId") Long userId);

    @Select("""
            <script>
            SELECT
                user_id,
                tel,
                user_name,
                nickname,
                real_name,
                gender,
                avatar,
                email,
                forbidden,
                created_at
            FROM user
            WHERE deleted = 0
            <if test="tel != null and tel != ''">
                AND tel = #{tel}
            </if>
            <if test="userName != null and userName != ''">
                AND user_name = #{userName}
            </if>
            <if test="nickname != null and nickname != ''">
                AND nickname LIKE CONCAT('%', #{nickname}, '%')
            </if>
            <if test="forbidden != null">
                AND forbidden = #{forbidden}
            </if>
            <if test="keyword != null and keyword != ''">
                AND (
                    tel LIKE CONCAT('%', #{keyword}, '%')
                    OR user_name LIKE CONCAT('%', #{keyword}, '%')
                    OR nickname LIKE CONCAT('%', #{keyword}, '%')
                )
            </if>
            ORDER BY user_id DESC
            </script>
            """)
    List<UserBrief> list(UserQuery query);

    @Select("""
            <script>
            SELECT
                user_id,
                tel,
                user_name,
                nickname,
                real_name,
                gender,
                avatar,
                email,
                forbidden,
                created_at
            FROM user
            WHERE deleted = 0
              AND tel = #{tel}
            LIMIT 1
            </script>
            """)
    UserBrief findBriefByTel(@Param("tel") String tel);

    @Select("""
            <script>
            SELECT
                user_id,
                tel,
                user_name,
                nickname,
                real_name,
                gender,
                avatar,
                email,
                forbidden,
                created_at
            FROM user
            WHERE deleted = 0
              AND user_name = #{userName}
            LIMIT 1
            </script>
            """)
    UserBrief findBriefByUserName(@Param("userName") String userName);

    @Select("""
            <script>
            SELECT
                user_id,
                tel,
                user_name,
                nickname,
                real_name,
                gender,
                avatar,
                email,
                forbidden,
                created_at
            FROM user
            WHERE deleted = 0
              AND email = #{email}
            LIMIT 1
            </script>
            """)
    UserBrief findBriefByEmail(@Param("email") String email);

    @Select("""
            <script>
            SELECT
                user_id,
                tel,
                user_name,
                nickname,
                real_name,
                pwd,
                gender,
                avatar,
                email,
                forbidden,
                created_at,
                updated_at,
                deleted,
                remark
            FROM user
            WHERE deleted = 0
              AND (
                user_name = #{account}
                OR tel = #{account}
              )
            LIMIT 1
            </script>
            """)
    UserPo findByAccount(@Param("account") String account);
}
