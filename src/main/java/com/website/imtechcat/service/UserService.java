package com.website.imtechcat.service;

import com.website.imtechcat.entity.UserEntity;

import java.util.Map;

public interface UserService {

	//登录
	UserEntity login(UserEntity userEntity);

	//注册
	String register(UserEntity userEntity);

	//获取用户
	UserEntity findUserEntityById(String id);

	//更新用户信息
	String modifyUserEntity(UserEntity userEntity);

	Map account(String userId);

}
