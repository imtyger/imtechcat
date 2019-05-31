package com.website.imtechcat.controller;

import com.website.imtechcat.common.ResultBean;
import com.website.imtechcat.service.UserService;
import com.website.imtechcat.util.JwtUtil;
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
	public ResponseEntity loginPost(@RequestParam("username") String userName,
												@RequestParam("password") String userPwd, ModelMap map){
		ResultBean result = new ResultBean();
		try{
			int user_count = userServiceImpl.login(userName,userPwd);
			switch(user_count){
				case 0 :
					result.setCode(user_count);
					result.setMsg("用户名不存在");
					break;
				case 1:
					result.setCode(user_count);
					result.setMsg("密码不正确");
					break;
				default:
					String token = jwtUtil.createToken(userName);
					map.put("token",token);
					result.setCode(200);
					result.setMsg("登录成功");
					result.setTime(System.currentTimeMillis());
					result.setData(map);
			}
			return new ResponseEntity(result,HttpStatus.OK);
		}catch(Exception ex){
			result.setCode(5);
			result.setMsg(ex.getMessage());
			return new ResponseEntity(result,HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping("/register")
	public int register(){
		String userName = "admin";
		String userPwd = "admin";
		return userServiceImpl.register(userName,userPwd);
	}

	@RequestMapping("/home")
	public String home(){
		return "/home";
	}
}
