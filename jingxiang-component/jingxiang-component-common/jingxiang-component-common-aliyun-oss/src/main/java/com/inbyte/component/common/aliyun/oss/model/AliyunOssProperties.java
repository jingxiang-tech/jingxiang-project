package com.inbyte.component.common.aliyun.oss.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "aliyun.oss")
@Getter
@Setter
public class AliyunOssProperties {

    private String accessKeyId;
    private String accessKeySecret;
    private String region;
    private String endpoint;
    private String bucketName;
    
    /**
     * STS角色ARN
     */
    private String roleArn;
    
    /**
     * STS角色会话名称
     */
    private String roleSessionName;
    
    /**
     * STS临时凭证有效期（秒），默认3600秒
     */
    private Long durationSeconds = 3600L;
}