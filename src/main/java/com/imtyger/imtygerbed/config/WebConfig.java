package com.imtyger.imtygerbed.config;

import com.imtyger.imtygerbed.exception.handle.BusinessExceptionResolver;
import com.imtyger.imtygerbed.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author imtygerx@gmail.com
 * @Date 2019/5/29 14:36
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Resource
	private BusinessExceptionResolver businessExceptionResolver;

	@Resource
	private JwtInterceptor jwtInterceptor;

//	@Resource
//	private IDInterceptor idInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry){
		//添加拦截器
		registry.addInterceptor(jwtInterceptor)
				.addPathPatterns("/**")
				.excludePathPatterns("/","/login");
				// "/swagger-ui.html","/swagger-resources", "/swagger-resources/**", "/v2/**");
				// WebMvcConfigurer.super.addInterceptors(registry);
//		registry.addInterceptor(idInterceptor)
//				.addPathPatterns("/**")
//				.excludePathPatterns("/", "/login");
	}

	/**
	 * 全局异常处理
	 * @param resolvers
	 */
	@Override
	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
		resolvers.add(businessExceptionResolver);
	}
}
