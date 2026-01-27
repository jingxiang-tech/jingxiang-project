package com.inbyte.component.common.basic;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "inbyte.merchant")
public class InbyteMerchantProperties {

    private String mctNo;
    private String mctPinyinName;

    public String getMctNo() {
        return mctNo;
    }

    public void setMctNo(String mctNo) {
        this.mctNo = mctNo;
    }

    public String getMctPinyinName() {
        return mctPinyinName;
    }

    public void setMctPinyinName(String mctPinyinName) {
        this.mctPinyinName = mctPinyinName;
    }
}