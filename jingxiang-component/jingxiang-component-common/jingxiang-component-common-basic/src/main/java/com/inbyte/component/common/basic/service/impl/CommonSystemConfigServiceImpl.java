package com.inbyte.component.common.basic.service.impl;

import com.inbyte.component.common.basic.service.CommonSystemConfigService;
import com.inbyte.component.common.basic.dao.CommonSystemConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 系统配置服务
 *
 * @author chenjw
 * @date 2024-03-11 11:03:58
 **/
@Service
public class CommonSystemConfigServiceImpl implements CommonSystemConfigService {

    @Autowired
    private CommonSystemConfigMapper commonSystemConfigMapper;

    @Override
    public String getValue(String key) {
        return commonSystemConfigMapper.getValue(key);
    }

    @Override
    public Integer getInteger(String key) {
        return Integer.valueOf(commonSystemConfigMapper.getValue(key));
    }
}
