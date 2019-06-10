package com.website.imtechcat.common.exception;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName TokenNotFoundException
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/6/10 19:11
 * @Version 1.0
 **/
@Getter
@Setter
public class TokenNotFoundException extends RuntimeException {


	private String message;

	private Integer code;

	public TokenNotFoundException(){
		setMessage("Token is not found");
	}

	public TokenNotFoundException(String message){
		this.message = message;
	}

	public TokenNotFoundException(Integer code,String message){
		this.code = code;
		this.message = message;
	}
}
