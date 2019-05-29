package com.website.imtechcat.controller;

import com.alibaba.fastjson.JSON;
import com.website.imtechcat.repository.UserRepository;
import com.website.imtechcat.service.UserService;
import com.website.imtechcat.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;

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

	@Resource
	private JwtUtil jwtUtil;

	@RequestMapping(value={"/"},method = RequestMethod.GET)
	public String login(){
		return "/login";
	}

	@RequestMapping(value={"/api/1.0/login"},method = RequestMethod.POST)
	public ResponseEntity<?> loginPost(@RequestParam("username") String userName,
															@RequestParam("password") String userPwd,
															HashMap map){
		logger.info("--loginPost:"+userName+","+userPwd);
		try{
			int count = userServiceImpl.login(userName,userPwd);
			if(count != 1){
				map.put("message","用户名密码错误");
				return new ResponseEntity<>("/login", HttpStatus.NOT_FOUND);
			}
			String token = jwtUtil.createToken(userName);
			logger.info("创建token:"+token);
			map.put("expire",1000 * 6);
			map.put("token",token);
			map.put("userName",userName);
		}catch(Exception ex){
			map.put("message",ex.getMessage());
		}
		return new ResponseEntity<>(map,HttpStatus.OK);
	}

	@RequestMapping("/register")
	public int register(){
		String userName = "admin";
		String userPwd = "admin";
		return userServiceImpl.register(userName,userPwd);
	}
}
