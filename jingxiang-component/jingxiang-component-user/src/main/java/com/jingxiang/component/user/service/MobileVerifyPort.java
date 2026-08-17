package com.jingxiang.component.user.service;

/**
 * 手机验证码校验端口（由业务系统实现；组件默认 no-op 通过）
 *
 * @author chenjw
 */
public interface MobileVerifyPort {

    /**
     * 校验手机验证码是否通过；未通过应抛业务异常
     *
     * @param tel  手机号
     * @param code 验证码，可为空（调用方已自行校验时）
     */
    void verify(String tel, String code);
}
