package com.website.imtechcat.util;

import com.website.imtechcat.common.exception.TokenNotFoundException;
import com.website.imtechcat.entity.UserEntity;
import io.jsonwebtoken.*;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @ClassName JwtUtil
 * @Description jwt配置处理类
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

	/**
	 *解析token
	 **/
	public Claims parseToken(String jsonWebToken) {
		Claims claims = Jwts.parser()
				.setSigningKey(getSecret())
				.parseClaimsJws(jsonWebToken).getBody();
		return claims;

	}

	/**
	 *生成token
	 **/
	public String createToken(UserEntity userEntity){
		Date date = new Date();
		Date expireDate = new Date(date.getTime()+ Long.parseLong(getExpire()) );
		JwtBuilder builder = Jwts.builder()
				.setHeaderParam("typ","JWT")
				.setSubject(userEntity.getId())
				.setIssuedAt(date)
				.setExpiration(expireDate)
				.signWith(SignatureAlgorithm.HS256,getSecret());
		return builder.compact();
	}

	/**
	 * 判断token是否已经失效
	 */
	public boolean isTokenExpired(String jsonWebToken) {
		Date expiredDate = getExpiredDateFromToken(jsonWebToken);
		return expiredDate.before(new Date());
	}

	/**
	 * 从token中获取过期时间
	 */
	private Date getExpiredDateFromToken(String jsonWebToken) {
		Claims claims = parseToken(jsonWebToken);
		return claims.getExpiration();
	}

	/**
	 *判断token是否可以被刷新
	 *@return
	 **/
	public boolean canRefresh(String jsonWebToken){
		return !isTokenExpired(jsonWebToken);
	}

	/**
	 * 验证token是否还有效
	 *
	 * @param jsonWebToken       客户端传入的token
	 * @param userEntity 从数据库中查询出来的用户信息
	 */
	public boolean validateToken(String jsonWebToken, UserEntity userEntity) {
		String id = getUserIdFromToken(jsonWebToken);
		return id.equals(userEntity.getId()) && !isTokenExpired(jsonWebToken);
	}

	/**
	 * 从token中获取用户ID
	 */
	public String getUserIdFromToken(String jsonWebToken) {
		String userId;
		try {
			Claims claims = parseToken(jsonWebToken);
			userId =  claims.getSubject();
		} catch (Exception e) {
			userId = null;
		}
		return userId;
	}
}
