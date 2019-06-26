package com.website.imtechcat.util;

import com.website.imtechcat.common.Constant;
import com.website.imtechcat.common.ResultCode;
import com.website.imtechcat.common.exception.TokenNotFoundException;
import com.website.imtechcat.entity.UserEntity;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @ClassName JwtTokenUtil
 * @Description jwt配置处理类
 * @Author Lenovo
 * @Date 2019/5/29 9:59
 * @Version 1.0
 **/
@Slf4j
@Component
public class JwtTokenUtil {

	@Resource
	private Constant constant;
	/**
	 *解析token
	 **/
	public Claims parseToken(String jsonWebToken){
		Claims claims;
		try{
			claims = Jwts.parser()
					.setSigningKey(constant.getSecret())
					.parseClaimsJws(jsonWebToken).getBody();
			return claims;
		}catch (ExpiredJwtException ex){
			throw new TokenNotFoundException(ResultCode.UNAUTHORIZED.getCode(),"token expired");
		}catch (SignatureException ex){
			throw new TokenNotFoundException(ResultCode.UNAUTHORIZED.getCode(),"token invalid");
		}catch (Exception ex){
			log.error(this.getClass() + " catch an exception =>" + ex);
			// throw new TokenNotFoundException(ResultCode.EXCEPTION.getCode(),ex.getMessage());
		}
		return null;
	}

	/**
	 *生成token
	 **/
	public String createToken(UserEntity userEntity){
		Date date = new Date();
		Date expireDate = new Date(date.getTime()+ Long.parseLong(constant.getExpire()) );
		JwtBuilder builder = Jwts.builder()
				.setHeaderParam("typ","JWT")
				.setSubject(userEntity.getId())
				.setIssuedAt(date)
				.setExpiration(expireDate)
				.signWith(SignatureAlgorithm.HS256,constant.getSecret());
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
			Claims claims = parseToken(jsonWebToken);
			return claims.getSubject();
	}
}
