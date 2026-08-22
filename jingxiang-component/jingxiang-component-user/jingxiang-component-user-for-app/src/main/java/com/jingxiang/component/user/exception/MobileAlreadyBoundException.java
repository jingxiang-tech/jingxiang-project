package com.jingxiang.component.user.exception;

import com.jingxiang.commons.exception.InbyteException;
import com.jingxiang.commons.model.dto.R;

/**
 * 手机号已被其他用户绑定
 *
 * @author chenjw
 */
public class MobileAlreadyBoundException extends InbyteException {

    public static final String CODE = "MOBILE_ALREADY_BOUND";

    public MobileAlreadyBoundException(Long occupiedUserId) {
        super(R.fail("手机号已被其他账号绑定"));
        this.occupiedUserId = occupiedUserId;
    }

    /**
     * 已占用该手机号的用户ID（供后续账号合并扩展）
     */
    private final Long occupiedUserId;

    public Long getOccupiedUserId() {
        return occupiedUserId;
    }

    public String getCode() {
        return CODE;
    }
}
