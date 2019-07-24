package com.website.imtechcat.service.impl;

import com.website.imtechcat.entity.UserEntity;
import com.website.imtechcat.repository.UserRepository;
import com.website.imtechcat.service.UserService;
import com.website.imtechcat.util.CheckUtil;
import com.website.imtechcat.util.Sha256Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


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


	@Autowired
	private UserRepository userRepository;


	@Override
	public UserEntity login(UserEntity userEntity) {

		UserEntity user = userRepository.findUserEntityByUsername(userEntity.getUsername());

		//用户不存在
		if(user == null){
			log.error(userEntity.getUsername() + " not exist.");
			return null;
		}

		//将前端传的密码进行sha256加密 再将其和数据库中取出的密码进行比对 是否一致
		String newPwd = Sha256Util.getSHA256(userEntity.getPassword());
		//密码错误
		if(newPwd == null || !user.getPassword().trim().equals(newPwd)){
			log.error("password error.");
			return null;
		}
		//更新最近登录时间
		user.setLastLoginTime(new Date());
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
				log.info("username :" + userEntity.getUsername() + " already exist.");
				return null;
			}

			//密码加密
			userEntity.setPassword(Sha256Util.getSHA256(userEntity.getPassword()));
			Date date = new Date();
			//注册时间
			userEntity.setCreateTime(date);
			//最近登录时间
			userEntity.setLastLoginTime(date);

			//创建成功返回id
			return userRepository.insert(userEntity).getId();
		}catch (Exception ex){
			log.error("register user :" + userEntity + " fail.",ex.getMessage());
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

	@Override
	public Map account(String userId) {
		UserEntity userEntity = this.findUserEntityById(userId);

		if(userEntity == null){
			return null;
		}

		Map userMap = new HashMap();
		String id = userEntity.getId();
		String username = userEntity.getUsername();
		String avataricon = userEntity.getAvataricon();
		String nickname = userEntity.getNickname();

		userMap.put("id",id);
		userMap.put("username",username);

		if(!CheckUtil.isNull(avataricon)){
			userMap.put("avataricon",avataricon);
		}
		if(!CheckUtil.isNull(nickname)){
			userMap.put("nickname",nickname);
		}

		return userMap;
	}
}
