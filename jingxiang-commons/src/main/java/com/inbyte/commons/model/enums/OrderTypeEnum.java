package com.inbyte.commons.model.enums;

/**
 * 订单类型
 * 杭州易思网络
 * @author chenjw
 * @date 2016年08月15日
 */
public enum OrderTypeEnum {

    CAMP_SITE("CMP", "营位订单"),
    HOTEL("HTL", "酒店订单"),
    TICKET("TKT", "门票订单"),

    CARD_VALUE("CDV", "储值卡订单"),
    CARD_TIMES("CDT", "计次卡订单"),
    CARD_PERIOD("CDP", "期间卡订单"),

    GOODS("GDS", "商品订单"),

    COUPON("CPN", "优惠券订单"),

    ACTIVITY("ACT", "活动订单"),

    // 不存在的订单，为了赠送会员卡场景使用
    VIRTUAL("VTL", "虚拟订单"),
    ;

    public final String code;
    public final String name;

    OrderTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据订单号取得订单类型枚举
     * @param orderNo
     * @return
     */
    public static OrderTypeEnum getByOrderNo(String orderNo) {
        return getByCode(orderNo.substring(0, 3));
    }

//    /**
//     * 根据code值取得订单类型枚举
//     * @param code
//     * @return
//     */
//    public static OrderTypeDict getByCode(String code) {
//        int intCode = Integer.parseInt(code);
//        return getByCode(intCode);
//    }

    /**
     * 根据code值取得订单类型枚举
     * @param code
     * @return
     */
    public static OrderTypeEnum getByCode(String code) {
        OrderTypeEnum[] values = OrderTypeEnum.values();
        for (OrderTypeEnum otc : values) {
            if (otc.code.equals(code)) {
                return otc;
            }
        }
        throw new IllegalArgumentException("订单类型" +code + "的枚举:" + OrderTypeEnum.class.getName() + "未定义");
    }
}
