package com.website.imtechcat.controller;

import com.website.imtechcat.repository.UserRepository;
import com.website.imtechcat.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/5/26 12:10
 * @Version 1.0
 **/
@Slf4j
@Controller
@RequestMapping("/")
public class UserController {

	private Logger logger = LoggerFactory.getLogger(UserController.class);

	@Resource
	private UserService userServiceImpl;

	@RequestMapping(value={"/"},method = RequestMethod.GET)
	public String login(){
		return "login";
	}

	@RequestMapping(value={"/api/1.0/login"},method = RequestMethod.POST)
	public String loginPost(@RequestParam("username") String userName,
							@RequestParam("password") String userPwd,
							ModelMap modelMap){
		logger.info("--loginPost:"+userName+","+userPwd);
		try{
			int count = userServiceImpl.login(userName,userPwd);
			if(count == 1){
				modelMap.put("userName",userName);
				return "home";
			}
		}catch(Exception ex){
			modelMap.put("message",ex.getMessage());
		}
		modelMap.put("message","用户名密码错误");
		return "login";
	}

	@RequestMapping("/register")
	public int register(){
		String userName = "admin";
		String userPwd = "admin";
		return userServiceImpl.register(userName,userPwd);
	}
}
