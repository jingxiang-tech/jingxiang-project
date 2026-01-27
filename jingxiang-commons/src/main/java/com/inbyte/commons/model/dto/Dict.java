package com.inbyte.commons.model.dto;

/**
 * 字典
 *
 * @author chenjw
 */
public class Dict {

    /**
     * 编码 Integer
     */
    private String code;
    /**
     * 名称
     */
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Dict() {
    }

    public Dict(String code, String name) {
        this.code = code;
        this.name = name;
    }

}
