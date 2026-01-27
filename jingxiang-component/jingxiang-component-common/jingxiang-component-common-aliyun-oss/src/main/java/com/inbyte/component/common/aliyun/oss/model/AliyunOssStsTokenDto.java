package com.inbyte.component.common.aliyun.oss.model;

import lombok.*;

import java.time.LocalDateTime;

/**
 * OSS STS Token返回对象
 *
 * @author chenjw
 * @date 2024/01/01
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AliyunOssStsTokenDto {

    /**
     * 临时访问密钥ID
     */
    private String accessKeyId;

    /**
     * 临时访问密钥Secret
     */
    private String accessKeySecret;

    /**
     * 安全令牌
     */
    private String securityToken;

//    /**
//     * 过期时间（时间戳，秒）
//     */
//    private Long expiration;

    /**
     * 过期时间（LocalDateTime）
     */
    private LocalDateTime expirationTime;

    /**
     * OSS Bucket名称
     */
    private String bucketName;

    /**
     * OSS Endpoint
     */
    private String endpoint;

    /**
     * 文件存放路径
     * 包含了文件名称
     */
    private String fileStoragePath;

    /**
     * 回调参数
     */
    private String callback;
}
