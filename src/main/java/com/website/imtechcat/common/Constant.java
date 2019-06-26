package com.website.imtechcat.common;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @ClassName Constant
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/6/12 19:05
 * @Version 1.0
 **/
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
	@Value("${jwt.token}")
	private String token;
	@Value("${jwt.prefix}")
	private String prefix;
	@Value("{jwt.user.key}")
	private String userKey;

	@Value("{string.null}")
	private String nullStr;
	@Value("{string.url}")
	private String url;
	@Value("{string.result}")
	private String result;

	@Value("{string.tags.get.zero}")
	private String tagsZero;
	@Value("{string.tags.get.empty}")
	private String tagsEmpty;

	@Value("{string.tags.count}")
	private String tagsCount;
	@Value("{string.tags.pagenum}")
	private String tagsPageNum;
	@Value("{string.tags.pagesize}")
	private String tagsPageSize;
	@Value("{string.tags.pagetotal}")
	private String tagsPageTotal;
	@Value("{string.tags}")
	private String tags;

}
