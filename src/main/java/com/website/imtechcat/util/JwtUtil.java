package com.website.imtechcat.util;

import io.jsonwebtoken.*;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @ClassName JwtUtil
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/5/29 9:59
 * @Version 1.0
 **/
@Component
@Data
public class JwtUtil {
	private static Logger logger = LoggerFactory.getLogger(JwtUtil.class);

	@Value("${spring.jwt.secret}")
	private String secret;
	@Value("${spring.jwt.expire}")
	private String expire;
	@Value("${spring.jwt.header}")
	private String header;

	public String getSecret() {
		return secret;
	}

	public String getExpire() {
		return expire;
	}

	public String getHeader() {
		return header;
	}

	//解析token
	public Claims parseToken(String jsonWebToken){
		try{
			Claims claims = Jwts.parser().setSigningKey(getSecret()).parseClaimsJws(jsonWebToken).getBody();
			logger.info("从token中解析到的username:"+claims.toString());
			return claims;
		}catch(Exception ex) {
			logger.error("获取token失败",ex);
		}
		return null;
	}

	//生成token
	public String createToken(String userName){
		logger.info("header="+getHeader()+",expire="+getExpire()+",secret="+getSecret());
		Date date = new Date();
		Date expireDate = new Date(date.getTime()+ Long.parseLong(getExpire()) );
		JwtBuilder builder = Jwts.builder()
				.setHeaderParam("typ","JWT")
				.setSubject(userName)
				.setIssuedAt(date)
				.setExpiration(expireDate)
				.signWith(SignatureAlgorithm.HS256,getSecret());
		return builder.compact();
	}

	//判断token是否过期
	public boolean isTokenExpired(Date date){
		return date.before(new Date());
	}
}
