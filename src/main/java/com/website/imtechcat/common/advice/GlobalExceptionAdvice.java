package com.website.imtechcat.common.advice;

import com.website.imtechcat.common.Result;
import com.website.imtechcat.common.ResultCode;
import com.website.imtechcat.common.exception.IllegalPropertiesException;
import com.website.imtechcat.common.exception.NullOrEmptyException;
import com.website.imtechcat.common.exception.TokenNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName GlobalExceptionAdvice
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/6/10 18:54
 * @Version 1.0
 **/
@ControllerAdvice
public class GlobalExceptionAdvice {


	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Result<String> exceptionHandler(HttpServletRequest request, Exception ex){
		return handleErrorInfo(request,ex.getMessage(),ex);
	}



	@ExceptionHandler(TokenNotFoundException.class)
	@ResponseBody
	public Result<String> tokenNotFoundExceptionHandler(HttpServletRequest request, TokenNotFoundException ex){
		if(ex.getCode() == null){
			return handleErrorInfo(request,ex.getMessage(),ex);
		}
		return handleErrorInfo(request,ex.getCode(),ex.getMessage(),ex);
	}


	@ExceptionHandler(NullOrEmptyException.class)
	@ResponseBody
	public Result<String> nullOrEmptyExceptionHandler(HttpServletRequest request, NullOrEmptyException ex){
		if(ex.getCode() == null){
			return handleErrorInfo(request,ex.getMessage(),ex);
		}
		return handleErrorInfo(request,ex.getMessage(),ex);
	}


	@ExceptionHandler(IllegalPropertiesException.class)
	@ResponseBody
	public Result<String> illegalPropExceptionHandler(HttpServletRequest request,
																  IllegalPropertiesException ex){
		if(ex.getCode() == null){
			return handleErrorInfo(request,ex.getMessage(),ex);
		}
		return handleErrorInfo(request,ex.getMessage(),ex);
	}


	private Result<String> handleErrorInfo(HttpServletRequest request, Integer code,String message, Exception ex){
		Result<String> errorMessage = new Result();
		errorMessage.setCode(code);
		errorMessage.setMsg("url:" + request.getRequestURI() + ",message:" + message);
		errorMessage.setData(ex.getMessage());
		return errorMessage;
	}

	private Result<String> handleErrorInfo(HttpServletRequest request,String message, Exception ex){
		Result<String> errorMessage = new Result();
		errorMessage.setCode(ResultCode.EXCEPTION.getCode());
		errorMessage.setMsg("url:" + request.getRequestURI() + ",message:" + message);
		errorMessage.setData(ex.getMessage());
		return errorMessage;
	}

}
