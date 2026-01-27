package com.inbyte.commons.model.enums;

/**
 * 通用范围字典
 * 形容：
 * 完成状态，全部、部分、无
 * 包含状态，全部、部分、无
 *
 * 杭州易思网络
 *
 * @author chenjw
 * @date 2025年01月15日
 */
public enum AvailableScopeEnum {

    ALL("全部可用"),
    PARTIAL_AVAILABLE("部分可用"),
    PARTIAL_UNAVAILABLE("部分不可用"),
    ;

    public final String name;

    AvailableScopeEnum(String name) {
        this.name = name;
    }

}
