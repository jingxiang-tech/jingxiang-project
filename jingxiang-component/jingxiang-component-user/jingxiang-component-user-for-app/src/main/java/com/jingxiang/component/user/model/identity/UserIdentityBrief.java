package com.jingxiang.component.user.model.identity;

import com.jingxiang.component.common.dict.convert.DictSerialize;
import com.jingxiang.component.user.dict.IdentityTypeEnum;
import com.jingxiang.commons.model.dict.WhetherDict;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 第三方身份详情/列表
 *
 * @author chenjw
 */
@Getter
@Setter
public class UserIdentityBrief {

    /**
     * 第三方身份ID
     */
    private Long identityId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 身份类型
     */
    @DictSerialize(IdentityTypeEnum.class)
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
     * 是否禁用
     */
    @DictSerialize(WhetherDict.class)
    private Integer forbidden;

    /**
     * 最近登录时间
     */
    private LocalDateTime latestLoginAt;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
