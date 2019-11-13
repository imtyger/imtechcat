package com.imtyger.imtygerbed.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
/*
 *@Description //登录才能操作的注解
 *@Date 2019/6/7 13:35
 **/
public @interface LoginToken {
	boolean required() default true;
}
