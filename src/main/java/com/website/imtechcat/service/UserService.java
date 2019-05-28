package com.website.imtechcat.service;

import com.website.imtechcat.entity.User;

public interface UserService {

	//登录
	int login(String userName,String userPwd);

	//注册
	int register(String userName,String userPwd);

	//获取用户id
	String findUserId(User user);
}
