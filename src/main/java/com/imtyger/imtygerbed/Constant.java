package com.imtyger.imtygerbed;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @Author imtygerx@gmail.com
 * @Date 2019/7/8 11:03
 */

@Data
@Configuration
@PropertySource(value = "classpath:constant.properties")
@Component
public class Constant {

	//jwt token
	@Value("${jwt.secret}")
	private String secret;
	@Value("${jwt.expire}")
	private String expire;
	@Value("${jwt.header}")
	private String header;
	@Value("${jwt.accessToken}")
	private String accessToken;
	@Value("${jwt.refreshToken}")
	private String refreshToken;
	@Value("${jwt.expiresIn}")
	private String expiresIn;
	@Value("${jwt.prefix}")
	private String prefix;
	@Value("${jwt.userKey}")
	private String userKey;

	@Value("${string.null}")
	private String nullStr;
	@Value("${string.url}")
	private String url;
	@Value("${string.result}")
	private String result;

	@Value("${string.zero}")
	private String zero;
	@Value("${string.empty}")
	private String empty;

	@Value("${string.count}")
	private String count;
	@Value("${string.pageNum}")
	private String pageNum;
	@Value("${string.pageSize}")
	private String pageSize;
	@Value("${string.pageTotal}")
	private String pageTotal;
	@Value("${string.list}")
	private String list;

	@Value("${page.num}")
	private String num;
	@Value("${page.size}")
	private String size;


}
