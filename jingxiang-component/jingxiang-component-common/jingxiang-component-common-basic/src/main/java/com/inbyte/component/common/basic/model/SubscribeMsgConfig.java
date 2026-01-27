package com.inbyte.component.common.basic.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 订阅消息数据参数
 *
 * @author chenjw
 * @date 2025/02/17
 */
@Getter
@Setter
public class SubscribeMsgConfig {

    /**
     * 订阅消息类型
     * 例如：PRE_TRIP_NOTICE, CHECK_IN_NOTICE, CHECK_OUT_NOTICE
     */
    private String subscribeMsgType;

    /**
     * 订阅消息模板ID
     */
    private String templateId;

}