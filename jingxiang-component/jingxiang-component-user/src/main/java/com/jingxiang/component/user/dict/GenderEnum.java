package com.jingxiang.component.user.dict;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 性别
 *
 * @author chenjw
 */
public enum GenderEnum {

    UNKNOWN(0, "未知"),
    MALE(1, "男"),
    FEMALE(2, "女");

    @EnumValue
    public final int code;
    public final String name;

    GenderEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static GenderEnum of(Integer code) {
        if (code == null) {
            return UNKNOWN;
        }
        for (GenderEnum value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        return UNKNOWN;
    }
}
