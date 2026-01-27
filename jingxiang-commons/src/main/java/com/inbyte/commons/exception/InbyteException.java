package com.inbyte.commons.exception;

import com.inbyte.commons.model.dto.R;

/**
 * 参数为Null错误
 *
 * @author: chenjw
 * @date: 2020/8/14
 */
public class InbyteException extends RuntimeException {

    private R r;

    public InbyteException() {
        super();
    }

    public InbyteException(String msg) {
        super(msg);
    }

    public InbyteException(R r) {
        super(r.getMsg());
        this.r = r;
    }

    public R getResult() {
        return r;
    }

    public void setResult(R r) {
        this.r = r;
    }

    /**
     * 业务异常
     * 状态码400，携带msg信息
     * 不会触发系统报警
     *
     * @param msg
     * @return
     */
    public static InbyteException fail(String msg) {
        return new InbyteException(R.fail(msg));
    }

    /**
     * 服务器异常
     * 状态码500，需要引起开发关注
     * 会触发系统报警
     *
     * @param msg
     * @return
     */
    public static InbyteException error(String msg) {
        return new InbyteException(R.error(msg));
    }

    public static InbyteException valueOf(R r) {
        return new InbyteException(r);
    }
}
