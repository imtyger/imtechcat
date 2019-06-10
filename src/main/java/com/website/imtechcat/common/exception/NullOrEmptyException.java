package com.website.imtechcat.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName NullOrEmptyException
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/6/10 19:14
 * @Version 1.0
 **/
@Getter
@Setter
public class NullOrEmptyException extends RuntimeException{


	private String message;
	private Integer code;

	public NullOrEmptyException(){
		setMessage("Parameter is null or empty");
	}

	public NullOrEmptyException(String message){
		this.message = message;
	}

	public NullOrEmptyException(Integer code,String message){
		this.code = code;
		this.message = message;
	}
}
