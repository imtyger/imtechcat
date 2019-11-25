package com.imtyger.imtygerbed.interceptor;

import cn.hutool.core.util.StrUtil;
import com.imtyger.imtygerbed.common.Result;
import com.imtyger.imtygerbed.annotation.PassToken;
import com.imtyger.imtygerbed.exception.BusinessException;
import com.imtyger.imtygerbed.utils.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
 * @Author imtyger@gmail.com
 * @Date 2019/5/29 14:00
 */

@Component
@Slf4j
public class JwtInterceptor extends HandlerInterceptorAdapter {

	@Resource
	private JwtTokenUtil jwtTokenUtil;

	@Value("${jwt.header}")
	private String header;

	@Value("${jwt.prefix}")
	private String prefix;

	@Value("${jwt.userKey}")
	private String userKey;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
							 Object handler){

		log.info("Request URI：{}", request.getRequestURI());

		// 如果不是HandlerMethod或者忽略登录,无需校验token
		if (!(handler instanceof HandlerMethod) || ((HandlerMethod) handler).
				getMethodAnnotation(PassToken.class) != null) {
			log.info("无需token校验,handler:{}", handler);
			return true;
		}

		//通过request获取请求token信息
		String authorizationStr = request.getHeader(header);
		log.info("Header authorization: {}", authorizationStr);

		if (!StringUtils.isEmpty(authorizationStr) || authorizationStr.startsWith(prefix)) {

			String tokenStr = authorizationStr.replace(prefix + StrUtil.SPACE, StrUtil.EMPTY);
			if (!StringUtils.isEmpty(tokenStr)) {

				Claims claims = jwtTokenUtil.parseToken(tokenStr);
				boolean isExpired = jwtTokenUtil.isTokenExpired(tokenStr);
				if (!isExpired) {
					request.setAttribute(userKey, claims.getSubject());
					return true;
				}
			}
		}

		throw new BusinessException(Result.UNAUTHORIZED.getValue(), "暂未登录或token已经过期");
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
						   Object handler, @Nullable ModelAndView modelAndView) {
	}
}
