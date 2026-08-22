package com.jingxiang.component.user.admin.merchant.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户名是否已存在。
 *
 * @author chenjw
 */
@Getter
@Setter
public class UsernameExistsBrief {

    /** 是否已存在。 */
    private Boolean exists;
}
