package com.inbyte.commons.model.enums;

/**
 * 文件类型
 *
 * @author chenjw
 * @date 2023/03/14
 */
public enum FileTypeEnum {

    IMAGE("图片"),
    VIDEO("视频"),
    AUDIO("音频"),
    DOC("文档"),
    PDF("PDF"),
    RAR("压缩包"),
    OTHER("其它"),

    ;

    public final String name;

    FileTypeEnum(String name) {
        this.name = name;
    }

}
