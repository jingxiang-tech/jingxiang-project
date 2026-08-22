package com.jingxiang.component.user.manager.merchant.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 商户空间用户的用户名存在性校验结果。
 *
 * @author chenjw
 */
@Getter
@Setter
public class MerchantSpaceUserUsernameExistsBrief {

    /** 是否已存在。 */
    private Boolean exists;
}
