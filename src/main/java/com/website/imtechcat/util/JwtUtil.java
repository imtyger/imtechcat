package com.website.imtechcat.util;

import io.jsonwebtoken.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


import javax.servlet.ServletException;
import java.util.Date;

/**
 * @ClassName JwtUtil
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/5/29 9:59
 * @Version 1.0
 **/
@Component
public class JwtUtil {
	private static Logger logger = LoggerFactory.getLogger(JwtUtil.class);

	//加密秘钥
	public static final String SECRET = "aHR0cHM6Ly9teS5vc2NoaW5hLm5ldC91LzM2ODE4Njg=";
	//有效时间
	public static final long EXPIRE = 1000 * 6;
	//用户凭证
	public static final String HEADER = "token";

	//解析token
	public Claims parseToken(String jsonWebToken){
		try{
			Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(jsonWebToken).getBody();
			logger.info("从token中解析到的username:"+claims.toString());
			return claims;
		}catch(Exception ex) {
			logger.error("获取token失败",ex);
		}
		return null;
	}

	//生成token
	public String createToken(String userName){
		logger.info("header="+HEADER+",expire="+EXPIRE+",secret="+SECRET);
		Date date = new Date();
		Date expireDate = new Date(date.getTime()+ EXPIRE );
		JwtBuilder builder = Jwts.builder()
				.setHeaderParam("typ","JWT")
				.setSubject(userName)
				.setIssuedAt(date)
				.setExpiration(expireDate)
				.signWith(SignatureAlgorithm.HS256,SECRET);
		return builder.compact();
	}

	//判断token是否过期
	public boolean isTokenExpired(Date date){
		return date.before(new Date());
	}
}
