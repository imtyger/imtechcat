package com.website.imtechcat.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName Result
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/5/31 14:01
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {
	private Integer code;
	private String msg;
	private T data;


	//成功
	public static <T> Result<T> success(){
		return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), null);
	}
	public static <T> Result<T> success(T data){
		return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), data);
	}

	public static <T> Result<T> success(String msg,T data){
		return new Result<>(ResultCode.SUCCESS.getCode(), msg, data);
	}

	//失败
	public static <T> Result<T> fail(){
		return new Result<>(ResultCode.FAILED.getCode(),ResultCode.FAILED.getMsg(),null);
	}
	public static <T> Result<T> fail(String msg){
		return new Result<>(ResultCode.FAILED.getCode(),msg,null);
	}

	public static <T> Result<T> fail(String msg,T data){
		return new Result<>(ResultCode.FAILED.getCode(),msg,data);
	}

	//授权失败
	public static <T> Result<T> unAuth(){
		return new Result<>(ResultCode.UNAUTHORIZED.getCode(),ResultCode.UNAUTHORIZED.getMsg(),null);
	}

	public static <T> Result<T> unAuth(String msg){

		return new Result<>(ResultCode.UNAUTHORIZED.getCode(),msg,null);
	}

	public static <T> Result<T> unAuth(String msg,T data){

		return new Result<>(ResultCode.UNAUTHORIZED.getCode(),msg,data);
	}


	//参数校验失败
	public static <T> Result<T> unValid(){
		return new Result<>(ResultCode.VALIDATE_FAILED.getCode(),ResultCode.VALIDATE_FAILED.getMsg(),null);
	}
	public static <T> Result<T> unValid(String msg){
		return new Result<>(ResultCode.VALIDATE_FAILED.getCode(),msg,null);
	}

	public static <T> Result<T> unValid(String msg,T data){
		return new Result<>(ResultCode.VALIDATE_FAILED.getCode(),msg,data);
	}
}
