package com.website.imtechcat.common.advice;

import com.alibaba.fastjson.JSONObject;
import com.website.imtechcat.common.Result;
import com.website.imtechcat.common.ResultCode;
import com.website.imtechcat.common.exception.IllegalPropertiesException;
import com.website.imtechcat.common.exception.NullOrEmptyException;
import com.website.imtechcat.common.exception.TokenNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName GlobalExceptionAdvice
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/6/10 18:54
 * @Version 1.0
 **/
@ControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {

	@ExceptionHandler(value = RuntimeException.class)
	@ResponseBody
	public Result<Object> customExceptionHandler(HttpServletRequest request,Exception ex){
		Result<Object> result = new Result();
		if( ex instanceof TokenNotFoundException){
			TokenNotFoundException tokenException = (TokenNotFoundException) ex;
			result.setCode(tokenException.getCode());
			result.setMsg(tokenException.getMessage());
		} else if( ex instanceof NullOrEmptyException){
			NullOrEmptyException nullOrEmptyExcep = (NullOrEmptyException) ex;
			result.setCode(nullOrEmptyExcep.getCode());
			result.setMsg(nullOrEmptyExcep.getMessage());
		} else if( ex instanceof IllegalPropertiesException) {
			IllegalPropertiesException illegalPropException = (IllegalPropertiesException) ex;
			result.setCode(illegalPropException.getCode());
			result.setMsg(illegalPropException.getMessage());
		} else {
			result.setCode(ResultCode.EXCEPTION.getCode());
			String message = ex.getMessage();
			if(message == null || StringUtils.isEmpty(message)){
				message = ResultCode.EXCEPTION.getMsg();
			}
			result.setMsg(message);
		}
		result.setData(newData(request));
		log.info(" customExceptionHandler=>" + result.toString());
		return result;
	}


	private Object newData(HttpServletRequest request){
		return new JSONObject().put("uri",request.getRequestURI());
	}


}
