package com.imtyger.imtygerbed.common.exception;

import io.swagger.models.auth.In;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName NullOrEmptyException
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/6/10 19:14
 * @Version 1.0
 **/
@Data
public class NullOrEmptyException extends RuntimeException{


	private Integer code;
	private String message;

	public NullOrEmptyException(Integer code,String message){
		this.code = code;
		this.message = message;
	}

}
