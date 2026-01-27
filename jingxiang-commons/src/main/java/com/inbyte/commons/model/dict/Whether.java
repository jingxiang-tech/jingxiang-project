package com.inbyte.commons.model.dict;

/**
 * 是否
 * 易思网络
 * @author chenjw
 * @date 2016年08月29日
 */
public class Whether {

    /**
     * 未知
     */
    public static final Integer UNK = -1;
    /**
     * 否
     */
    public static final Integer No = 0;
    /**
     * 是
     */
    public static final Integer Yes = 1;

    private Whether(){}

    public static boolean isTrue(Integer value){
        return value != null && value == Yes;
    }
}
