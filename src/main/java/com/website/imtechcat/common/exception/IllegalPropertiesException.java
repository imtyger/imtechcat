package com.website.imtechcat.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName IllegalPropertiesException
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/6/10 19:16
 * @Version 1.0
 **/
@Setter
@Getter
public class IllegalPropertiesException extends
		RuntimeException {


	private String message;
	private Integer code;

	public IllegalPropertiesException(){
		setMessage("Parameter is illegal");
	}

	public IllegalPropertiesException(String message){
		this.message = message;
	}

	public IllegalPropertiesException(Integer code,String message){
		this.code = code;
		this.message = message;
	}

}
