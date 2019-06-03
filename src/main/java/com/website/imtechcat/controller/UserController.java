package com.website.imtechcat.controller;

import com.website.imtechcat.common.Result;
import com.website.imtechcat.entity.UserEntity;
import com.website.imtechcat.service.UserService;
import com.website.imtechcat.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

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
	public ResponseEntity<Result> loginPost(@RequestBody UserEntity userEntity){
		try{
			String id = userServiceImpl.login(userEntity);
			if(id == null){
				return new ResponseEntity<>(Result.fail(),HttpStatus.OK);
			}

			String token = jwtUtil.createToken(id);
			Map map = new HashMap();
			map.put("token",token);
			return new ResponseEntity<>(Result.success(map),HttpStatus.OK);

		}catch(Exception ex){
			String msg = ex.getCause().getMessage();
			return new ResponseEntity<>(Result.fail(msg),HttpStatus.OK);
		}
	}

	@RequestMapping(value="/register",method = RequestMethod.POST)
	public ResponseEntity<Result> register(@RequestBody UserEntity userEntity){
		try{
			String id = userServiceImpl.register(userEntity);
			if(id == null){
				return new ResponseEntity<>(Result.fail(),HttpStatus.OK);
			}
			String token = jwtUtil.createToken(id);
			Map map = new HashMap();
			map.put("token",token);
			return new ResponseEntity<>(Result.success(map),HttpStatus.OK);
		}catch(Exception ex){
			String msg = ex.getCause().getMessage();
			return new ResponseEntity<>(Result.fail(msg),HttpStatus.OK);
		}
	}

}
