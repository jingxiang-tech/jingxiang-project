package com.jingxiang.component.user.model.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jingxiang.component.user.dict.GenderEnum;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 统一用户实体
 *
 * 表：user
 *
 * @author chenjw
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("user")
public class UserPo {

    /**
     * 用户ID
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * 手机号
     */
    private String tel;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * BCrypt密码Hash（禁止出现在日志/响应）
     */
    private String pwd;

    /**
     * 性别
     */
    private GenderEnum gender;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 是否禁用：0否 1是
     */
    private Integer forbidden;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 逻辑删除：0否 1是
     */
    private Integer deleted;

    /**
     * 备注
     */
    private String remark;

    @Override
    public String toString() {
        return "UserPo{" +
                "userId=" + userId +
                ", tel='" + tel + '\'' +
                ", userName='" + userName + '\'' +
                ", nickname='" + nickname + '\'' +
                ", realName='" + realName + '\'' +
                ", gender=" + gender +
                ", avatar='" + avatar + '\'' +
                ", email='" + email + '\'' +
                ", forbidden=" + forbidden +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deleted=" + deleted +
                ", remark='" + remark + '\'' +
                '}';
    }
}
