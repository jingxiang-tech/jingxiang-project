package com.inbyte.commons.util;

import com.inbyte.commons.exception.InbyteException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Set;


/**
 * 断言工具
 *
 * @author chenjw
 * @date 2023/3/2
 */
public class Assert {

    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    public static void ok(Object o) {
        Set<ConstraintViolation<Object>> violations = VALIDATOR.validate(o);
        for (ConstraintViolation<Object> violation : violations) {
            throw InbyteException.fail(violation.getMessage());
        }
    }

    public static void notNull(Object o, String message) {
        if (o == null) {
            throw InbyteException.error(message);
        }
    }

}
