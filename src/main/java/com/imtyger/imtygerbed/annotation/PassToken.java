package com.imtyger.imtygerbed.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: imtygerx@gmail.com
 * @Date 2019/6/7 13:34
 * @Desc 跳过验证
 */

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PassToken {
	boolean required() default true;
}
