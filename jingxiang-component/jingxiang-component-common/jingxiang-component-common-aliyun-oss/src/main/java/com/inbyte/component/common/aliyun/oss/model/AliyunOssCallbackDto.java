package com.inbyte.component.common.aliyun.oss.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * OSS回调返回对象
 * 用于响应阿里云OSS回调请求
 * 
 * @author chenjw
 * @date 2024/12/27
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AliyunOssCallbackDto {

    /**
     * 状态
     * OK: 成功
     * verify not ok: 验证失败
     * 对应OSS回调响应格式：{"Status": "OK"}
     */
    @JsonProperty("Status")
    private String status;
}