package com.inbyte.component.common.aliyun.oss;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云OSS组件
 *
 * @author chenjw
 */
@ComponentScan
@Configuration
@MapperScan(basePackages = "com.inbyte.component.common.aliyun.oss.dao")
public class CommonAliyunOssConfiguration {
}