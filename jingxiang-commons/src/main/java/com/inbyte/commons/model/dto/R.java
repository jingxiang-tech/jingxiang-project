package com.inbyte.commons.model.dto;

import com.alibaba.fastjson2.JSON;

import java.io.Serializable;

/**
 * 接口响应结果实体包装类
 *
 * @author chenjw
 * @date 2020年7月6日
 */
public class R<E> implements Serializable {

    /**
     * 请求响应状态,参考字典ResultStatus
     *
     * @mock 200
     */
    private Integer status;
    /**
     * 消息提示
     *
     * @mock 请求成功
     */
    private String msg;
    /**
     * 返回结果
     */
    private E data;

    public R(Integer status, String msg, E data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public R() {
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 请求结果成功
     */
    public boolean succeeded() {
        return status == ResultStatus.Success.code;
    }

    /**
     * 请求结果失败
     */
    public boolean failed() {
        return status != ResultStatus.Success.code &&
                status != ResultStatus.Created.code &&
                status != ResultStatus.Accepted.code;
    }

    /**
     * 业务处理成功
     *
     * @param msg 处理成功提示消息, 注意: 当响应结果为 String 类型时, 需要调用 success(String msg, T data) 重载方法, 否则会将结果设置为 msg 消息
     */
    public static <T> R<T> ok(String msg) {
        return ok(msg, null);
    }

    /**
     * 业务处理成功
     * 状态码 200
     *
     * @param <T>
     * @return
     */
    public static <T> R<T> ok() {
        return ok(null, null);
    }

    /**
     * 业务处理成功
     *
     * @param data 响应数据, 注意: 当响应结果为 String 类型时, 需要调用 success(String msg, T data) 重载方法, 否则会将结果设置为 msg 消息
     * @param <T>
     * @return
     */
    public static <T> R<T> ok(T data) {
        return ok(null, data);
    }

    /**
     * 业务处理成功
     *
     * @param msg
     * @param data
     * @param <T>
     * @return
     */
    public static <T> R<T> ok(String msg, T data) {
        return new R(ResultStatus.Success.code, msg, data);
    }

    /**
     * 业务处理成功
     *
     * @param data
     * @return
     */
    public static R<String> okStr(String data) {
        return ok(null, data);
    }

    /**
     * 业务处理失败
     *
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> R<T> fail(String msg) {
        return fail(msg, null);
    }

    /**
     * 业务处理失败
     *
     * @param msg
     * @param data
     * @param <T>
     * @return
     */
    public static <T> R<T> fail(String msg, T data) {
        return new R(ResultStatus.Failure.code, msg, data);
    }

    public static <T> R<T> fail(ResultStatus statusCode) {
        return new R(statusCode.code, statusCode.name, null);
    }

    /**
     * 系统异常
     * 状态码 500
     *
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> R<T> error(String msg) {
        return new R(ResultStatus.Internal_Server_Error.code, msg, null);
    }

    public static <T> R<T> set(ResultStatus status) {
        return set(status, status.name, null);
    }

    public static <T> R<T> set(ResultStatus status, String msg) {
        return set(status, msg, null);
    }

    public static <T> R<T> set(ResultStatus status, String msg, T data) {
        return new R(status.code, msg, data);
    }

    public static <T> R<T> set(Integer status) {
        return new R(status, null, null);
    }

    public static <T> R<T> set(Integer status, String msg) {
        return new R(status, msg, null);
    }

    public static <T> R<T> set(Integer status, String msg, T data) {
        return new R(status, msg, data);
    }

    public static <T> R<T> valueOf(R resp) {
        return new R(resp.getStatus(), resp.getMsg(), null);
    }

    /**
     * 分页接口使用
     * 本方法将参数包装 page 属性, 同时提供数据总行数【totalItems】, 与 list 数据
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> R page(T data) {
        return new R(ResultStatus.Success.code, null, new Page<>(data));
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
