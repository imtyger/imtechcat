package com.website.imtechcat.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * @ClassName User
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/5/26 11:01
 * @Version 1.0
 **/
@Document(collection = "users")
@Data
@AllArgsConstructor
public class User {
	@Id
	private String id;//id
	private String userName;//用户名
	private String userPwd;//用户密码
	private String nickName;//昵称
	private String avatarIcon;//头像图标
	private String phoneNum;//手机号
	private String email;//邮箱
	private long createTime;//注册时间
	private long lastUpdateTime;//最后更新时间
	private long lastLoginTime;//最后登录时间
	private String lastLoginIp;//最后登录ip




}
