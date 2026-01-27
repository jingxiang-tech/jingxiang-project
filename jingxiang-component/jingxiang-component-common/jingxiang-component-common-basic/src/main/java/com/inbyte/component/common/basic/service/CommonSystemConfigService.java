package com.inbyte.component.common.basic.service;

/**
 * 系统配置服务
 *
 * @author chenjw
 * @date 2024-03-11 11:03:58
 **/
public interface CommonSystemConfigService {

    String getValue(String key);

    Integer getInteger(String key);
}
