package com.inbyte.commons.model.enums;

/**
 * 支付状态
 * 杭州易思网络
 * @author chenjw
 * @date 2016年08月15日
 */
public enum PaymentStatusEnum {

    WAIT_PAY("未支付"),
    PAID( "已支付"),
    REFUNDING( "退款处理中"),
    CANCELED("已取消"),
    REFUNDED("已退款"),
    NO_PAYMENT_REQUIRED("无需付款")
    ;

    public final String name;

    PaymentStatusEnum(String name) {
        this.name = name;
    }

}