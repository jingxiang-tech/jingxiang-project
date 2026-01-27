package com.inbyte.commons.model.enums;

/**
 * 是、否、未知状态字典
 *
 * @author chenjw
 * @date 2022/10/19
 */
public enum WhetherStatusEnum {

    /**
     * 为什么这个命名有点不规范呢
     * 别急
     * 微信小程序scene参数不能超过32个字符，为避免参数超长，分享基础参数尽量简短
     * 参考文档
     * https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/qrcode-link/qr-code/getUnlimitedQRCode.html
     */

    YES("是"),
    NO("否"),
    UNK("未知"),
    ;

    public final String name;

    WhetherStatusEnum(String name) {
        this.name = name;
    }
}
