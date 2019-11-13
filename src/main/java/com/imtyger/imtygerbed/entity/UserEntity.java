package com.imtyger.imtygerbed.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @ClassName UserEntity
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/6/1 11:44
 * @Version 1.0
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class UserEntity {
	@Id
	private String id;
	private String username;
	private String password;
	private String nickname;//昵称
	private String avataricon;//头像图标
	private String userphone;//手机号
	private String email;//邮箱
	private Date createTime;//注册时间
	private Date lastUpdateTime;//最后更新时间
	private Date lastLoginTime;//最后登录时间
	private String lastLoginIp;//最后登录ip

}
