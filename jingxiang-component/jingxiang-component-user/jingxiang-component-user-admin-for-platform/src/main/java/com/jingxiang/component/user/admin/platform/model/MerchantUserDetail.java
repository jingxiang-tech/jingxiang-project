package com.jingxiang.component.user.admin.platform.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 商户用户详情。
 */
@Getter
@Setter
public class MerchantUserDetail {

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
    /** 是否删除 */
    private Integer deleted;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 创建人 */
    private String creator;
    /** 更新时间 */
    private LocalDateTime updateTime;
    /** 修改人 */
    private String modifier;
    /** 最近登录时间 */
    private LocalDateTime latestLoginTime;
}
