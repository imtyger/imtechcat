package com.website.imtechcat.util;

import com.website.imtechcat.common.ResultBean;
import com.website.imtechcat.common.ResultEnum;

/**
 * @ClassName ResultUtil
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/5/30 20:06
 * @Version 1.0
 **/
public class ResultUtil {

	//成功带参数
	public static ResultBean success(Object object){
		ResultBean result = new ResultBean();
		result.setCode(ResultEnum.SUCCESS.getCode());
		result.setMsg(ResultEnum.SUCCESS.getMsg());
		result.setData(object);
		return result;
	}

	//成功不带参数
	public static ResultBean success(){
		return success(null);
	}

	//失败
	public static ResultBean fail(Integer code,String msg){
		ResultBean result = new ResultBean();
		result.setCode(code);
		result.setMsg(msg);
		return result;
	}
}
