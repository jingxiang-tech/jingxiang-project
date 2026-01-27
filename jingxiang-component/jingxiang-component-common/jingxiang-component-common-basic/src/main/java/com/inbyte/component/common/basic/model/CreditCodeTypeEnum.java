package com.inbyte.component.common.basic.model;

/**
 * 企业代码类型
 * 1：统一社会信用代码（18 位） 2：组织机构代码（9 位 xxxxxxxx-x） 3：营业执照注册号(15 位)
 *
 * @author : chenjw
 * @create 2024/5/2
 */
public enum CreditCodeTypeEnum {

    UNIFIED_SOCIAL_CREDIT("统一社会信用代码", 1),
    ORGANIZING_INSTITUTION("组织机构代码", 2),
    BUSINESS_LICENSE("营业执照注册号", 3),
    ;

    public final String name;
    public final int code;

    CreditCodeTypeEnum(String name, int code){
        this.name = name;
        this.code = code;
    }
}
