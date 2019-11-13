package com.imtyger.imtygerbed.common.exception;

import lombok.*;

/**
 * @ClassName TokenNotFoundException
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/6/10 19:11
 * @Version 1.0
 **/
@Data
public class TokenNotFoundException extends RuntimeException {

	private String message;

	private Integer code;

	public TokenNotFoundException(Integer code,String message){
		this.code = code;
		this.message = message;
	}
}
