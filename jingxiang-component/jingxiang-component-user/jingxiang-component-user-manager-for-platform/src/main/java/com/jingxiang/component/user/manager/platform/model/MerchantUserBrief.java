package com.jingxiang.component.user.manager.platform.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 商户用户列表项。
 */
@Getter
@Setter
public class MerchantUserBrief {

    /** 用户 ID */
    private Integer userId;

    /** 账户名 */
    private String userName;

    /** 真实姓名 */
    private String realName;

    /** 手机号 */
    private String tel;

    /** 商户号 */
    private String mctNo;

    /** 是否禁用 */
    private Integer disabled;

    /** 备注 */
    private String remark;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 最近登录时间 */
    private LocalDateTime latestLoginTime;
}
