package com.jingxiang.component.user.dict;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 第三方身份类型
 *
 * @author chenjw
 */
public enum IdentityTypeEnum {

    WECHAT_MP("WECHAT_MP", "微信小程序"),
    ALIPAY_MP("ALIPAY_MP", "支付宝小程序"),
    DOUYIN("DOUYIN", "抖音"),
    APPLE("APPLE", "Apple"),
    GOOGLE("GOOGLE", "Google");

    @EnumValue
    public final String code;
    public final String name;

    IdentityTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
