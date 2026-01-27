package com.inbyte.commons.model.dict;

/**
 * 是否
 * 易思网络
 * @author chenjw
 * @date 2016年08月29日
 */
public enum WhetherDict {

    UNK(-1, "未知"),
    No(0, "否"),
    Yes(1, "是");

    public final int code;
    public final String name;

    WhetherDict(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public boolean yes() {
        return this == Yes ? true : false;
    }
}
