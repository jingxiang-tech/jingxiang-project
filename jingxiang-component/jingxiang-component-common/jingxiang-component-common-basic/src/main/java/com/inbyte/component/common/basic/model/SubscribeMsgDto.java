package com.inbyte.component.common.basic.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 订阅消息数据模型
 * @author chenjw
 * @date 2025/02/17
 */
@Getter
@Setter
public class SubscribeMsgDto {

    /**
     * 订阅状态
     * 1 表示已订阅，0 表示未订阅
     */
    private Integer subscribed;

    /**
     * 消息是否已发送
     * 1 表示已发送，0 表示未发送
     */
    private Integer sent;

    /**
     * 消息发送时间
     * 格式为：yyyy-MM-dd HH:mm:ss
     */
    private LocalDateTime sendTime;

    /**
     * 消息是否已读
     * 1 表示已读，0 表示未读
     */
    private Integer read;

    /**
     * 消息阅读时间
     * 格式为：yyyy-MM-dd HH:mm:ss
     */
    private LocalDateTime readTime;

}