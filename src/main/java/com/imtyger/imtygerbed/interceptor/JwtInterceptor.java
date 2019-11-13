package com.imtyger.imtygerbed.interceptor;

import com.imtyger.imtygerbed.common.Constant;
import com.imtyger.imtygerbed.common.ResultCode;
import com.imtyger.imtygerbed.common.annotation.PassToken;
import com.imtyger.imtygerbed.common.exception.NullOrEmptyException;
import com.imtyger.imtygerbed.common.exception.TokenNotFoundException;
import com.imtyger.imtygerbed.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName JwtInterceptor
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/5/29 14:00
 * @Version 1.0
 **/
@Slf4j
@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {

	@Resource
	private JwtTokenUtil jwtTokenUtil;

	@Resource
	private Constant constant;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
							 Object handler){

		if (!(handler instanceof HandlerMethod) || ((HandlerMethod) handler).
				getMethodAnnotation(PassToken.class) != null) {
			//如果不是HandlerMethod或者忽略登录
			// logger.info("无需token校验,handler:{}", handler);
			return true;
		}

		log.info("####################################");
		log.info("路由： " + request.getRequestURI());
		log.info("####################################");

		//通过request获取请求token信息
		String authorization = request.getHeader(constant.getHeader());
		log.info("authorization:" + authorization);

		//判断请求头信息是否为空，
		if(authorization == null || StringUtils.isEmpty(authorization) || authorization.trim().equals(constant.getNullStr())){
			//errorMsg = "Header:Authorization is null or empty.";
			throw new NullOrEmptyException(ResultCode.UNAUTHORIZED.getCode(),ResultCode.UNAUTHORIZED.getMsg());
		}

		//判断是否以Bearer 开头
		if(!authorization.startsWith(constant.getPrefix())) {
			throw new TokenNotFoundException(ResultCode.UNAUTHORIZED.getCode(),"Authorization:Bearer not found.");
		}

		//取出token 判断是否为空或null
		String token = authorization.replace(constant.getPrefix() + " ","");
		if(StringUtils.isEmpty(token) || token == null || token.trim().equals(constant.getNullStr())){
			throw new TokenNotFoundException(ResultCode.UNAUTHORIZED.getCode(),"token is empty or null.");
		}

		//解析token 判断claims是否为空
		Claims claims = jwtTokenUtil.parseToken(token);
		if(claims == null || claims.isEmpty() || claims.getSubject().isEmpty()){
			throw new TokenNotFoundException(ResultCode.UNAUTHORIZED.getCode(),"parse token failed, claims not found.");
		}

		//判断token是否失效
		boolean b = jwtTokenUtil.isTokenExpired(token);
		if(b){
			throw new TokenNotFoundException(ResultCode.UNAUTHORIZED.getCode(),"token expired.");
		}
		request.setAttribute(constant.getUserKey(),claims.getSubject());

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
						   Object handler, @Nullable ModelAndView modelAndView) throws Exception {
		// logger.info("===postHandle===");
	}

}
