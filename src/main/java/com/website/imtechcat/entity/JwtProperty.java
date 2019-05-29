package com.website.imtechcat.entity;

import lombok.Data;

/**
 * @ClassName JwtProperty
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/5/29 10:18
 * @Version 1.0
 **/
@Data
public class JwtProperty {

	private long expiry;
	private String issuer;
	private String base64Security;

}
