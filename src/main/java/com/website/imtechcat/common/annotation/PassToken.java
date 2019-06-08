package com.website.imtechcat.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
/*
 *@Description //跳过验证
 *@Date 2019/6/7 13:34
 **/
public @interface PassToken {
	boolean required() default true;
}
