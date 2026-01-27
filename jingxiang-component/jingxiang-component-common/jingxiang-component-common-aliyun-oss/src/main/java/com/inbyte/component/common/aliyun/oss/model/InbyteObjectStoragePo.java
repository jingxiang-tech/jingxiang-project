package com.inbyte.component.common.aliyun.oss.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.inbyte.commons.model.enums.FileTypeEnum;
import com.inbyte.commons.model.enums.AccountTypeEnum;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 对象存储实体
 *
 * @author chenjw
 * @date 2024-03-12 09:40:07
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@TableName("inbyte_object_storage")
public class InbyteObjectStoragePo {

    /**
     * 对象ID
     */
    @TableId(value = "object_id", type = IdType.AUTO)
    private Integer objectId;

    /**
     * 文件地址
     */
    private String url;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 已上传
     */
    private Integer uploaded;

    /**
     * 文件大小(单位：字节)
     */
    private Integer size;

    /**
     * 文件类型
     */
    private FileTypeEnum fileType;

    /**
     * 文件类型回调
     */
    private String mimeType;

    /**
     * 上传源
     */
    private AccountTypeEnum uploadBy;

    /**
     * 高度
     */
    private Integer height;

    /**
     * 宽度
     */
    private Integer width;

    /**
     * 备注
     */
    private String remark;

    /**
     * 已删除
     */
    private Integer deleted;

    /**
     * 商户号
     */
    private String mctNo;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    private String modifier;

}

