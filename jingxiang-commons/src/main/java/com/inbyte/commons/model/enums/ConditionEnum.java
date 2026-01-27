package com.inbyte.commons.model.enums;

/**
 * 无，部分，全部枚举
 *
 * @author chenjw
 * @date 2022/10/19
 */
public enum ConditionEnum {

    NONE("无"),
    ALL("全部"),
    PARTIAL("部分"),
    ;

    public final String name;

    ConditionEnum(String name) {
        this.name = name;
    }
}
