package com.inbyte.commons.model.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 分页参数基类
 *
 * @author chenjw
 */
public class BasePage {

    /**
     * 当前页序号
     */
    @NotNull(message = "分页查询参数不能为空")
    @Min(value = 0, message = "当前页参数不正确")
    @Max(value = 5000, message = "最大查询 5000 页内数据")
    private Integer pageNum;
    /**
     * 每页查询行数
     */
    @NotNull(message = "分页查询参数不能为空")
    @Min(value = 1, message = "单页最少查询 1 条数据")
    @Max(value = 200, message = "单页最大查询 200 条数据")
    private Integer pageSize;

    /**
     * 用户Id
     * 服务端参数，前端忽略
     * @ignore
     */
    @JsonIgnore
    private Integer userId;
    /**
     * 当前门店ID
     * 服务端参数，前端忽略
     * @ignore
     */
    @JsonIgnore
    private String venueId;
    /**
     * 当前门店ID
     * 服务端参数，前端忽略
     * @ignore
     */
    @JsonIgnore
    private String mctNo;

    /**
     * 查询关键字
     **/
    @JsonIgnore
    private String keyword;

    public String getMctNo() {
        return mctNo;
    }

    public void setMctNo(String mctNo) {
        this.mctNo = mctNo;
    }

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getKeyword() {
        return keyword;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
