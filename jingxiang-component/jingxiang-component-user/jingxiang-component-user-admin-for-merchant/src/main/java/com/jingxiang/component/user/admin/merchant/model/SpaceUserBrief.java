package com.jingxiang.component.user.admin.merchant.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 空间用户列表项。
 *
 * @author chenjw
 */
@Getter
@Setter
public class SpaceUserBrief {

    /** 用户ID。 */
    private Long id;

    /** 用户名。 */
    private String username;

    /** 昵称。 */
    private String nickname;

    /** 手机号。 */
    private String mobile;

    /** 邮箱。 */
    private String email;

    /** 状态：0禁用 1启用。 */
    private Integer status;

    /** 创建时间。 */
    private LocalDateTime createdAt;

    /** 创建人用户ID。 */
    private Long creatorUserId;
}
