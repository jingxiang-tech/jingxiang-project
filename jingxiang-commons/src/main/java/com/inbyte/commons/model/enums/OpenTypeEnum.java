package com.inbyte.commons.model.enums;

/**
 * 文章打开方式
 *
 * @author : chenjw
 * @create 2024/3/28
 */
public enum OpenTypeEnum {

    LOCAL("本地打开"),
    OFFICIAL_ACCOUNT("跳转公众号"),
    H5("跳转H5"),
    ;

    public final String name;

    OpenTypeEnum(String name) {
        this.name = name;
    }

}
