package com.inbyte.commons.model.enums;

/**
 * App应用类型
 *
 * @author chenjw
 * @date 2022/10/19
 */
public enum AppTypeEnum {

    /**
     * 为什么这个命名有点不规范呢
     * 别急
     * 微信小程序scene参数不能超过32个字符，为避免参数超长，分享基础参数尽量简短
     * 参考文档
     * https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/qrcode-link/qr-code/getUnlimitedQRCode.html
     */

    WXMP("微信小程序"),
    ALIMP("支付宝小程序"),
    DYMP("抖音小程序"),
    XHSMP("小红书小程序"),
    ANDROID("安卓App"),
    IOS("IOS App"),
    H5("H5"),
    ;

    public final String name;

    AppTypeEnum(String name) {
        this.name = name;
    }
}
