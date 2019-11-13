package com.imtyger.imtygerbed.common.exception;

import lombok.Data;

@Data
public class IllegalPropertiesException extends
		RuntimeException {

	private Integer code;
	private String message;

	public IllegalPropertiesException(Integer code,String message){
		this.code = code;
		this.message = message;
	}


}
