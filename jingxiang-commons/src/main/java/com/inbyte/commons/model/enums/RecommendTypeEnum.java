package com.inbyte.commons.model.enums;


/**
 * 用户来源类型
 *
 * @author chenjw
 * @date 2023-02-06 11:33:27
 */
public enum RecommendTypeEnum {

    /**
     * Organic Traffic
     */
    OT("自然流"),
    USER("用户推荐"),
    MERCHANT( "商家推荐"),
    ;

    public final String name;

    RecommendTypeEnum(String name) {
        this.name = name;
    }

}
