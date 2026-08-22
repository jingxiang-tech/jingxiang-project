package com.jingxiang.component.user.admin.common.dict;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 组织成员类型
 *
 * @author chenjw
 */
public enum MemberTypeEnum {

    OWNER("OWNER", "所有者"),
    ADMIN("ADMIN", "管理员"),
    OPERATOR("OPERATOR", "运营"),
    FINANCE("FINANCE", "财务"),
    MEMBER("MEMBER", "普通成员");

    @EnumValue
    public final String code;
    public final String name;

    MemberTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
