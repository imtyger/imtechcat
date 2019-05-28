package com.website.imtechcat.service.impl;

import com.website.imtechcat.entity.User;
import com.website.imtechcat.repository.UserRepository;
import com.website.imtechcat.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
	public String findUserId(User user){
		return userRepository.findById(user.getId()).toString();
	}

	@Override
	public int login(String userName, String userPwd) {
		int count = 0;
		User user = userRepository.findUserByUserNameAndUserPwd(userName,userPwd);
		if(user != null){
			count = 1;
		}
		return count;
	}

	@Override
	public int register(String userName, String userPwd) {
		int count = 0;
		User user = userRepository.findUserByUserNameAndUserPwd(userName,userPwd);
		if(user != null){
			return count;
		}
		user = new User("",userName,userPwd,"","","","",0,0,0,"");
		logger.info("user:"+user.toString());
		userRepository.save(user);
		count = 1;
		return count;
	}
}
