package com.website.imtechcat.interceptor;

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
public class JwtInterceptor extends HandlerInterceptorAdapter {

	private Logger logger = LoggerFactory.getLogger(JwtInterceptor.class);

	@Resource
	private JwtUtil jwt;

	public static final String USER_KEY = "username";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
							 Object handler) throws Exception {

		if(request.getRequestURI().equals("/") || RequestMethod.OPTIONS.toString().equals(request.getMethod())){
			logger.info("自动排除生成token路径");
			return true;
		}

		String token = getToken(request);
		if(token == null || token.trim().equals("")){
			throw new Exception("token未获取，请重新登录");
		}

		Claims claims = jwt.parseToken(token);
		logger.info("获取到的token:"+claims);

		boolean b = claims==null || claims.isEmpty() || jwt.isTokenExpired(claims.getExpiration());
		if(b){
			throw new Exception("token超时，请重新登录");
		}

		request.setAttribute(USER_KEY,claims.getSubject());
		return true;
	}

	//获取请求token
	private String getToken(HttpServletRequest request){
		String token = request.getHeader("token");
		if(token == null || token.trim().equals("")){
			token = request.getParameter("token");
		}
		return token;
	}

}
