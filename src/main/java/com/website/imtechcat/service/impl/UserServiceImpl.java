package com.website.imtechcat.service.impl;

import com.website.imtechcat.entity.UserEntity;
import com.website.imtechcat.repository.UserRepository;
import com.website.imtechcat.service.UserService;
import com.website.imtechcat.util.IpAddressUtil;
import com.website.imtechcat.util.Sha256Util;
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
	public UserEntity login(UserEntity userEntity) {

		UserEntity user = userRepository.findUserEntityByUsername(userEntity.getUsername());

		//用户不存在
		if(user == null){
			logger.error(userEntity.getUsername() + " not exist.");
			return null;
		}

		//将前端传的密码进行sha256加密 再将其和数据库中取出的密码进行比对 是否一致
		String newPwd = Sha256Util.getSHA256(userEntity.getPassword());
		//密码错误
		if(newPwd == null || !user.getPassword().trim().equals(newPwd)){
			logger.error("password error.");
			return null;
		}
		//更新最近登录时间
		user.setLastLoginTime(System.currentTimeMillis());
		//更新最近登录ip
		user.setLastLoginIp(userEntity.getLastLoginIp());
		return 	userRepository.save(user);
	}

	@Override
	public String register(UserEntity userEntity) {
		try{
			//判断用户名是否重复
			UserEntity user = userRepository.findUserEntityByUsername(userEntity.getUsername());
			if(user != null){
				logger.info("username :" + userEntity.getUsername() + " already exist.");
				return null;
			}

			//密码加密
			userEntity.setPassword(Sha256Util.getSHA256(userEntity.getPassword()));
			long time = System.currentTimeMillis();
			//注册时间
			userEntity.setCreateTime(time);
			//最近登录时间
			userEntity.setLastLoginTime(time);

			//创建成功返回id
			return userRepository.insert(userEntity).getId();
		}catch (Exception ex){
			logger.error("register user :" + userEntity + " fail.",ex.getMessage());
		}
		return null;
	}

	@Override
	public UserEntity findUserEntityById(String id) {
		return userRepository.findUserEntityById(id);
	}

	@Override
	public String modifyUserEntity(UserEntity userEntity) {
		return userRepository.save(userEntity).getId();
	}
}
