package com.jingxiang.component.user.manager.merchant.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 商户空间用户列表项。
 *
 * @author chenjw
 */
@Getter
@Setter
public class MerchantSpaceUserBrief {

    /** 用户ID。 */
    private Long userId;

    /** 用户名。 */
    private String userName;

    /** 昵称。 */
    private String nickname;

    /** 手机号。 */
    private String tel;

    /** 邮箱。 */
    private String email;

    /** 是否禁用。 */
    private Integer disabled;

    /** 创建时间。 */
    private LocalDateTime createTime;

    /** 创建人用户ID。 */
    private Long creatorUserId;
}
