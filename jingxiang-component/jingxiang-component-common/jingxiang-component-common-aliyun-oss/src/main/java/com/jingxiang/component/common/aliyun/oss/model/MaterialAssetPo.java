package com.jingxiang.component.common.aliyun.oss.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jingxiang.commons.model.enums.FileTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 素材/对象存储实体，表 {@code material_asset}。
 *
 * @author chenjw
 * @date 2024-03-12 09:40:07
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@TableName("material_asset")
public class MaterialAssetPo {

    /**
     * 素材主键
     */
    @TableId(value = "material_id", type = IdType.AUTO)
    private Integer materialId;

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
    private Integer fileSize;

    /**
     * 文件类型
     */
    private FileTypeEnum fileType;

    /**
     * 文件类型回调
     */
    private String mimeType;

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
