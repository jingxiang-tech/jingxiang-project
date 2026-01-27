package com.inbyte.commons.model.dict;

/**
 * 有无
 * 易思网络
 *
 * @author chenjw
 * @date 2016年08月29日
 */
public enum HaveDict {

    No(0, "无"),
    Yes(1, "有");

    public final int code;
    public final String name;

    HaveDict(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public boolean yes() {
        return this == Yes ? true : false;
    }
}
