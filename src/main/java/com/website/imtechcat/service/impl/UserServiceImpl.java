package com.website.imtechcat.service.impl;

import com.website.imtechcat.entity.User;
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
	public String findUserId(User user){
		return userRepository.findById(user.getId()).toString();
	}

	@Override
	public int login(String userName, String userPwd) {
		logger.info("-------------------------");
		User user = userRepository.findUserByUserName(userName);
		logger.info("user:"+user.toString());
		if(user == null){
			logger.info("User : " + userName + " not exist.");
			//用户不存在时返回0
			return 0;
		}
		//这里需要做解密
		String pwd = user.getUserPwd();
		if(!userPwd.trim().equals(pwd)){
			//密码错误返回1
			return 1;
		}
		//密码正确成功返回2
		return 2;
	}

	@Override
	public int register(String userName, String userPwd) {
		try{
			//此处需要创建用户id
			User user = new User("",userName,userPwd,"","","","",0,0,0,"");
			logger.info("register user:"+user.toString());
			userRepository.save(user);
			//创建成功返回1
			return 1;
		}catch (Exception ex){
			logger.error("register user :" + userName + " fail.",ex.getMessage());
			//失败返回0
			return 0;
		}


	}
}
