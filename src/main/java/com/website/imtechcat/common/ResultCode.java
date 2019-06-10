package com.website.imtechcat.common;



/**
 * @ClassName ResultCode
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/5/30 20:03
 * @Version 1.0
 **/
public enum ResultCode{

	SUCCESS(200, "操作成功"),
	FAILED(500, "操作失败"),
	VALIDATE_FAILED(400, "参数检验失败"),
	UNAUTHORIZED(401, "暂未登录或token已经过期"),
	FORBIDDEN(403, "没有相关权限"),
	EXCEPTION(300,"系统异常");

	private Integer code;

	private String msg;

	public void setCode(Integer code) {
		this.code = code;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}


	ResultCode(Integer code, String msg){
		this.code = code;
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}


	public String getMsg() {
		return msg;
	}



	@Override
	public String toString() {
		return "ResultCode{" +
				"code=" + code +
				", msg='" + msg + '\'' +
				'}';
	}



}
