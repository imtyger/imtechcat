package com.website.imtechcat.interceptor;

import com.website.imtechcat.common.annotation.PassToken;
import com.website.imtechcat.common.exception.NullOrEmptyException;
import com.website.imtechcat.common.exception.TokenNotFoundException;
import com.website.imtechcat.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private Logger logger = LoggerFactory.getLogger(JwtInterceptor.class);

	@Resource
	private JwtUtil jwtUtil;

	public static final String USER_KEY = "userId";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
							 Object handler){

		if (!(handler instanceof HandlerMethod) || ((HandlerMethod) handler).
				getMethodAnnotation(PassToken.class) != null) {
			//如果不是HandlerMethod或者忽略登录
			// logger.info("无需token校验,handler:{}", handler);
			return true;
		}

		logger.info("####################################");
		logger.info("路由： " + request.getRequestURI());
		logger.info("####################################");

		//通过request获取请求token信息
		String authorization = request.getHeader("Authorization");
		logger.info("===authorization===" + authorization);

		//判断请求头信息是否为空，
		if(authorization == null || StringUtils.isEmpty(authorization)){
			throw new NullOrEmptyException("Header:Authorization is null or empty.");
		}

		//判断是否以Bearer 开头
		if(!authorization.startsWith("Bearer")) {
			throw new TokenNotFoundException("Authorization:Bearer not found.");
		}

		//获取token
		String token = authorization.replace("Bearer ","");
		Claims claims = jwtUtil.parseToken(token);
		if(claims == null || claims.isEmpty() || claims.getSubject().isEmpty()){
			throw new TokenNotFoundException("parse token failed, claims not found.");
		}

		//判断claims是否为空 判断token是否失效
		boolean b = jwtUtil.isTokenExpired(token);
		if(b){
			logger.info("======="+b);
			throw new TokenNotFoundException("token expired.");
		}
		request.setAttribute(USER_KEY,claims.getSubject());

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
						   Object handler, @Nullable ModelAndView modelAndView) throws Exception {
		// logger.info("===postHandle===");
	}

}
