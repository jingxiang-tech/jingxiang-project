package com.inbyte.component.common.dict;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 字典管理配置
 *
 * @author chenjw
 * @date 2018年1月18日
 */
@ComponentScan
@Configuration
@MapperScan("com.inbyte.component.common.dict.dao")
public class DictConfiguration {

}
