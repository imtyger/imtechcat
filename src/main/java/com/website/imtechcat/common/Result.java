package com.website.imtechcat.common;

import java.io.Serializable;

/**
 * @ClassName Result
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/5/31 14:01
 * @Version 1.0
 **/
public class Result<T> implements Serializable {
	private Integer code;
	private String msg;
	private T data;


	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Result(Integer code, String msg, T data){
		this.code = code;
		this.data = data;
		this.msg = msg;
	}

	//成功返回
	public static <T> Result<T> success(T data){
		return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), data);
	}

	public static <T> Result<T> success(T data, String msg){
		return new Result<>(ResultCode.SUCCESS.getCode(), msg, data);
	}

	//失败返回
	public static <T> Result<T> fail(String msg){
		return new Result<>(ResultCode.FAILED.getCode(),msg,null);
	}

	public static <T> Result<T> fail(){
		return new Result<>(ResultCode.FAILED.getCode(),ResultCode.FAILED.getMsg(),null);
	}

	//验证失败返回
	public static <T> Result<T> unAuth(String msg){
		return new Result<>(ResultCode.UNAUTHORIZED.getCode(),msg,null);
	}

	public static <T> Result<T> unAuth(){
		return new Result<>(ResultCode.UNAUTHORIZED.getCode(),ResultCode.UNAUTHORIZED.getMsg(),null);
	}
}
