package com.imtyger.imtygerbed.config;

import com.imtyger.imtygerbed.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @ClassName WebConfig
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/5/29 14:36
 * @Version 1.0
 **/
@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Resource
	private JwtInterceptor interceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry){
		//添加拦截器
		registry.addInterceptor(interceptor).addPathPatterns("/**")
		.excludePathPatterns("/","/login");
				// "/swagger-ui.html","/swagger-resources", "/swagger-resources/**", "/v2/**");
		// WebMvcConfigurer.super.addInterceptors(registry);
	}


}
