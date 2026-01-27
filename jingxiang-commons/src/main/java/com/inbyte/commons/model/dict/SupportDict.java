package com.inbyte.commons.model.dict;

/**
 * 支持
 * 易思网络
 *
 * @author chenjw
 * @date 2016年08月29日
 */
public enum SupportDict {

    No(0, "不支持"),
    Yes(1, "支持");

    public final int code;
    public final String name;

    SupportDict(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public boolean yes() {
        return this == Yes ? true : false;
    }
}
