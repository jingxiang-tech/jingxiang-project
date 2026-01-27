package com.inbyte.commons.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class FutureOrTodayValidator implements ConstraintValidator<FutureOrToday, LocalDate> {

    @Override
    public void initialize(FutureOrToday constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (date == null) {
            return true; // null values are valid
        }
        return !date.isBefore(LocalDate.now());
    }
}