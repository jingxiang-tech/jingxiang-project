package com.jingxiang.component.user.model.identity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jingxiang.component.user.dict.IdentityTypeEnum;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 用户第三方身份实体
 *
 * 表：user_identity
 *
 * @author chenjw
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@TableName("user_identity")
public class UserIdentityPo {

    /**
     * 第三方身份ID
     */
    @TableId(value = "identity_id", type = IdType.AUTO)
    private Long identityId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 身份类型
     */
    private IdentityTypeEnum identityType;

    /**
     * 第三方平台应用标识
     */
    private String appKey;

    /**
     * 第三方身份标识
     */
    private String identifier;

    /**
     * 跨应用统一身份
     */
    private String unionId;

    /**
     * 第三方昵称
     */
    private String nickname;

    /**
     * 第三方头像
     */
    private String avatar;

    /**
     * 是否禁用：0否 1是
     */
    private Integer forbidden;

    /**
     * 最近登录时间
     */
    private LocalDateTime latestLoginAt;

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
}
