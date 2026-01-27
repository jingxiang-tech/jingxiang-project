package com.inbyte.commons.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FutureOrTodayValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface FutureOrToday {
    String message() default "日期不能为以前的时间";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}