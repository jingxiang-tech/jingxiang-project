package com.inbyte.commons.model.dto;

import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 分页实体
 *
 * @author chenjw
 */
public class Page<T> {

    /**
     * 数据总条数
     */
    private long totalItems;
    /**
     * 当前页数据信息
     */
    private List<T> list;

    public Page() {
    }

    public <T> Page(T list) {
        PageInfo pageInfo = new PageInfo((List) list);
        this.totalItems = pageInfo.getTotal();
        this.list = pageInfo.getList();
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
