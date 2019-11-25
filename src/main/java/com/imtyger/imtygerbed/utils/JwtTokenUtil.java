package com.imtyger.imtygerbed.utils;

import com.imtyger.imtygerbed.common.Result;
import com.imtyger.imtygerbed.entity.UserEntity;
import com.imtyger.imtygerbed.exception.BusinessException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author imtygerx@gmail.com
 * @Date 2019/5/29 9:59
 **/

@Component
@Slf4j
public class JwtTokenUtil {
	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expire}")
	private String expire;

	/**
	 *解析token
	 **/
	public Claims parseToken(String jsonWebToken){
		try{
			return Jwts.parser().setSigningKey(secret).parseClaimsJws(jsonWebToken).getBody();

		} catch (ExpiredJwtException ex){
			throw new BusinessException(Result.UNAUTHORIZED.getValue(), "token已经过期");

		} catch (SignatureException ex){
			throw new BusinessException(Result.UNAUTHORIZED.getValue(), "token解析失败");

		} catch (Exception ex){
			log.error("解析token异常: ", ex);
			throw new BusinessException(Result.UNAUTHORIZED.getValue(), "token解析失败");
		}
	}

	/**
	 *生成token
	 **/
	public String createToken(UserEntity userEntity){
		Date date = new Date();
		Date expireDate = new Date(date.getTime() + Long.parseLong(expire));
		JwtBuilder builder = Jwts.builder()
				.setHeaderParam("typ","JWT")
				.setSubject(userEntity.getUsername())
				.setIssuedAt(date)
				.setExpiration(expireDate)
				.signWith(SignatureAlgorithm.HS256, secret);
		return builder.compact();
	}

	/**
	 * 判断token是否已经失效
	 */
	public boolean isTokenExpired(String jsonWebToken) {
		Date expireDate = getExpiredDateFromToken(jsonWebToken);
		return expireDate.before(new Date());
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
		return id.equals(userEntity.getId().toString()) && !isTokenExpired(jsonWebToken);
	}

	/**
	 * 从token中获取用户ID
	 */
	private String getUserIdFromToken(String jsonWebToken) {
			Claims claims = parseToken(jsonWebToken);
			return claims.getSubject();
	}
}
