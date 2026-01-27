package com.inbyte.commons.model.enums;

/**
 * 付款平台类型
 * 杭州易思网络
 *
 * @author chenjw
 * @date 2016年08月15日
 */
public enum PaymentTypeEnum {

    CASH_PAY("现金"),
    WEIXIN_PAY("微信支付"),
    ALI_PAY("支付宝"),
    OTHER_PAY("其它"),
    ;

    public final String name;

    PaymentTypeEnum(String name) {
        this.name = name;
    }


}
