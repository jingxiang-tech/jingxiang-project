package com.jingxiang.component.user.manager.merchant.model.member;

import lombok.Getter;
import lombok.Setter;

/**
 * 组织成员用户名存在性校验结果
 *
 * @author chenjw
 */
@Getter
@Setter
public class UserMemberUsernameExistsBrief {

    /**
     * 是否已存在
     */
    private Boolean exists;
}
