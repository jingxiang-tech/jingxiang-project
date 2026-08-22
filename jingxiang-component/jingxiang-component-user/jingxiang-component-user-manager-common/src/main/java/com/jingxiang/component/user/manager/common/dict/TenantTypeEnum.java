package com.jingxiang.component.user.manager.common.dict;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 租户/组织类型
 *
 * @author chenjw
 */
public enum TenantTypeEnum {

    PLATFORM("PLATFORM", "平台运营主体"),
    MERCHANT("MERCHANT", "商户"),
    ENTERPRISE("ENTERPRISE", "企业"),
    AGENCY("AGENCY", "代理商"),
    SERVICE_PROVIDER("SERVICE_PROVIDER", "服务商");

    @EnumValue
    public final String code;
    public final String name;

    TenantTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
