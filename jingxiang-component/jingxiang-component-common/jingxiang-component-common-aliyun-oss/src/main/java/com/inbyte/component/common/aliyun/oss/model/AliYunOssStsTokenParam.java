package com.inbyte.component.common.aliyun.oss.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inbyte.commons.model.enums.FileTypeEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * OSS STS安全令牌获取参数
 *
 * @author chenjw
 * @date 2020/08/04 04:35:21
 */
@Getter
@Setter
@ToString
public class AliYunOssStsTokenParam {

    /**
     * 文件名称
     **/
    @NotNull
    private String fileName;

    /**
     * 文件类型
     */
    @NotNull
    private FileTypeEnum fileType;

    /**
     * 模块名称
     * 根据上传操作所在页面填写，方便统计数据规模
     * 例如：
     * - 抖音视频博主头像
     * - 抖音视频评论区用户头像
     * - 微信号头像
     * - 微信好友头像
     * - 微信朋友圈图片
     */
    @NotNull
    private String moduleName;

    @JsonIgnore
    private String mctNo;

    @JsonIgnore
    private String operator;
}