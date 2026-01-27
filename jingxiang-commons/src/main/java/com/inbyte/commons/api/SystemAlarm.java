package com.inbyte.commons.api;

/**
 * 报警通知
 *
 * @author chenjw
 */
public interface SystemAlarm {

    /**
     * 发送文本消息
     *
     * @return
     */
    void alert(String module, String requestInfo);

    /**
     * 发送文本消息
     *
     * @return
     */
    void alert(String module, String requestInfo, Exception e);


}
