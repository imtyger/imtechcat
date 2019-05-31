package com.website.imtechcat.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName Result
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/5/30 20:01
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
	private Integer code;
	private String msg;
	private T data;

}
