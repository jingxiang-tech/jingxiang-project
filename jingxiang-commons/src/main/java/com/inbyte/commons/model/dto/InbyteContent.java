package com.inbyte.commons.model.dto;

/**
 * 详情内容区对象
 * 详细参考企业微信云盘：研发空间/协议规范/富文本内容区域规范
 *
 * @author chenjw
 * @date 2023/03/13
 */
public class InbyteContent {

    /**
     * 数据类型
     * 0: text
     * 1: richText
     * 2: image
     * 3: video
     */
    private Integer dataType;

    /**
     * 数据值
     * text	文本内容
     * richText	html 富文本内容
     * image	图片 URL 地址
     * video	视频 URL 地址
     */
    private String value;

    /**
     * 行为
     * 0: 无
     * 1: 小程序跳转
     * 2: 浏览器跳转
     */
    private Integer action;

    /**
     * 路径
     */
    private String path;

    /**
     * 路径参数
     */
    private String pathParam;

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPathParam() {
        return pathParam;
    }

    public void setPathParam(String pathParam) {
        this.pathParam = pathParam;
    }
}
