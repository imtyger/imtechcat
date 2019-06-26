package com.website.imtechcat.common.exception;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName IllegalPropertiesException
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/6/10 19:16
 * @Version 1.0
 **/
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
