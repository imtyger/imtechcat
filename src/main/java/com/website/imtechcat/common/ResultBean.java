package com.website.imtechcat.common;

import lombok.*;

/**
 * @ClassName ResultBean
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/5/31 14:01
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultBean<T> {
	private Integer code;
	private String msg;
	private Long time;
	private T data;
}
