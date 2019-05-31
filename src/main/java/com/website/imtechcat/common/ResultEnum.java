package com.website.imtechcat.common;

import lombok.Data;

/**
 * @ClassName ResultEnum
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/5/30 20:03
 * @Version 1.0
 **/
public enum ResultEnum {

	UNKNOWN_ERROR(-1,"未知错误"),
	SUCCESS(200,"成功"),
	USER_NOT_EXIST(1,"用户不存在"),
	USER_IS_EXISTS(2,"用户已存在"),
	DATA_IS_NULL(3,"数据为空"),
	;

	private Integer code;

	private String msg;

	ResultEnum(Integer code,String msg){
		this.code = code;
		this.msg = msg;
	}

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




}
