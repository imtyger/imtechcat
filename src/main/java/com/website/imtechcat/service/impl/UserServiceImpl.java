package com.website.imtechcat.service.impl;

import com.website.imtechcat.entity.UserEntity;
import com.website.imtechcat.repository.UserRepository;
import com.website.imtechcat.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @ClassName UserServiceImpl
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/5/26 11:51
 * @Version 1.0
 **/
@Slf4j
@Service
public class UserServiceImpl implements UserService {

	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;


	@Override
	public String login(UserEntity user) {
		UserEntity userEntity = userRepository.findUserEntityByUsername(user.getUsername());
		if(userEntity == null){
			logger.info("UserEntity : " + user + " not exist.");
			//用户不存在时返回0
			return null;
		}
		//这里需要做解密
		if(!userEntity.getPassword().trim().equals(user.getPassword())){
			//密码错误返回0
			return null;
		}
		//密码正确成功返回1
		return userEntity.getId();
	}

	@Override
	public String register(UserEntity userEntity) {
		try{
			UserEntity user = userRepository.save(userEntity);
			//创建成功返回1
			return user.getId();
		}catch (Exception ex){
			logger.error("register user :" + userEntity + " fail.",ex.getMessage());
		}
		return null;
	}
}
