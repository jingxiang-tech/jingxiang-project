package com.inbyte.commons.model.dto;

import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 路径参数
 *
 * 注：可以基础
 * @author chenjw
 * @date 2022-11-28 13:54:45
 **/
@Getter
@Setter
public class BasePath {

    /**
     * 页面
     * 小程序页面地址
     * 等于 page
     **/
    @NotNull(message = "上传资源所在页面路径不能为空")
    private String path;

    /**
     * 进入页面地址所需参数
     * JSON对象类型
     * 可空
     **/
    @Nullable
    private JSONObject pathParam;

}