package com.website.imtechcat.interceptor;

import com.website.imtechcat.common.annotation.PassToken;
import com.website.imtechcat.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.omg.CORBA.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;

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

	public static final String USER_KEY = "username";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
							 Object handler) throws Exception {

		// if(request.getRequestURI().equals("/") || request.getRequestURI().equals("/login") || RequestMethod.OPTIONS.toString().equals(request.getMethod())){
		// 	logger.info("自动排除生成token路径");
		// 	return true;
		// }
		if (!(handler instanceof HandlerMethod) || ((HandlerMethod) handler).
				getMethodAnnotation(PassToken.class) != null) {
			//如果不是HandlerMethod或者忽略登录
			logger.info("无需token校验,handler:{}", handler);
			return true;
		}

		logger.info("####################################");
		logger.info("路由： " + request.getRequestURI());
		logger.info("####################################");

		String token = getToken(request);
		if(token == null || token.trim().equals("")){
			throw new Exception("token未获取，请重新登录");
		}

		Claims claims = jwtUtil.parseToken(token);
		logger.info("获取到的token:"+claims);

		boolean b = claims==null || claims.isEmpty() || jwtUtil.isTokenExpired(claims.getExpiration());
		if(b){
			throw new Exception("token超时，请重新登录");
		}

		request.setAttribute(USER_KEY,claims.getSubject());
		return true;
	}

	//获取请求token
	private String getToken(HttpServletRequest request){
		String token = request.getHeader(jwtUtil.getHeader());
		if(token == null || token.trim().equals("")){
			token = request.getParameter(jwtUtil.getHeader());
		}
		if(token == null || token.trim().equals("")){
			token = request.getHeader("Authorization");
			logger.info("===============token==========="+token);
			token = token.split(" ")[1];
			logger.info("===============token==========="+token);
		}
		logger.info("===============token==========="+token);
		return token;
	}

}
