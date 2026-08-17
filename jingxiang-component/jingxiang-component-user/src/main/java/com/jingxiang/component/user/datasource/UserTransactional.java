package com.jingxiang.component.user.datasource;

import org.springframework.core.annotation.AliasFor;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用户库事务：固定绑定 {@link UserDataSourceConfiguration#TRANSACTION_MANAGER}
 *
 * @author chenjw
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Transactional(transactionManager = UserDataSourceConfiguration.TRANSACTION_MANAGER)
public @interface UserTransactional {

    @AliasFor(annotation = Transactional.class)
    Class<? extends Throwable>[] rollbackFor() default {Exception.class};
}
