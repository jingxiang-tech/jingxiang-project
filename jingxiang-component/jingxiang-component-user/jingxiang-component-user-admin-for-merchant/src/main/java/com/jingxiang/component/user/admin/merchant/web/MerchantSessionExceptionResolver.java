package com.jingxiang.component.user.admin.merchant.web;

import com.jingxiang.commons.model.dto.R;
import com.jingxiang.commons.model.dto.ResultStatus;
import com.jingxiang.component.user.admin.merchant.exception.MerchantSessionUnavailableException;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 商户后台会话异常处理器。
 *
 * @author chenjw
 */
@Order(0)
@ControllerAdvice
public class MerchantSessionExceptionResolver {

    @ExceptionHandler(MerchantSessionUnavailableException.class)
    @ResponseBody
    public R<?> handleSessionUnavailable(MerchantSessionUnavailableException exception) {
        return R.set(ResultStatus.Unauthorized);
    }
}
