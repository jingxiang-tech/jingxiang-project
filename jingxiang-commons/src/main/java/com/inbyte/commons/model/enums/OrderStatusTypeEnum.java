package com.inbyte.commons.model.enums;

/**
 * 订单状态
 * 杭州易思网络
 *
 * @author chenjw
 * @date 2016年08月15日
 */
public enum OrderStatusTypeEnum {

    /**
     * 0：全部
     * 1：待支付
     * 2：已支付, 包含（1、已支付；2、待使用；5、待评价；6、已完成）
     * 3：退款, 包含（10、退款申请中；11、退款被拒绝；12、退款完成）
     *
     */
    All("全部"),
    WAIT_PAY("待支付"),
    PAID("已支付"),
    REFUND("退款"),
    ;

    public final String name;

    OrderStatusTypeEnum(String name) {
        this.name = name;
    }


}
