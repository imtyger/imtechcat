package com.website.imtechcat.config;

import com.website.imtechcat.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.annotation.Resource;

/**
 * @ClassName WebConfig
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/5/29 14:36
 * @Version 1.0
 **/

public class WebConfig extends WebMvcConfigurationSupport {

	@Resource
	private JwtInterceptor interceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry){
		//添加拦截器
		registry.addInterceptor(interceptor);
	}


}
