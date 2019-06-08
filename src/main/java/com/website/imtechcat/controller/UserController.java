package com.website.imtechcat.controller;

import com.website.imtechcat.common.Result;
import com.website.imtechcat.common.annotation.PassToken;
import com.website.imtechcat.entity.UserEntity;
import com.website.imtechcat.service.UserService;
import com.website.imtechcat.util.IpAddressUtil;
import com.website.imtechcat.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

	@RequestMapping(value={"/","/login"},method = RequestMethod.GET)
	@PassToken
	public String login(){
		return "/login";
	}


	@RequestMapping(value={"/api/1.0/login"},method = RequestMethod.POST)
	@PassToken
	public ResponseEntity<Result> loginPost(@RequestBody UserEntity userEntity,HttpServletRequest request){

		logger.info("==================loginPost");
		try{
			//最近登录ip
			userEntity.setLastLoginIp(IpAddressUtil.getIpAddr(request));
			logger.info("==================userEntity.LastLoginIp:" + userEntity.getLastLoginIp());
			String id = userServiceImpl.login(userEntity);
			if(StringUtils.isEmpty(id)){
				return new ResponseEntity<>(Result.fail(),HttpStatus.OK);
			}
			logger.info("============id:"+id);
			String token = jwtUtil.createToken(id);
			logger.info("============token:"+token);
			Map map = new HashMap();
			map.put("token",token);
			return new ResponseEntity<>(Result.success(map),HttpStatus.OK);

		}catch(Exception ex){
			String msg = ex.getCause().getMessage();
			return new ResponseEntity<>(Result.fail(msg),HttpStatus.OK);
		}
	}

	@RequestMapping(value={"/register"},method = {RequestMethod.GET,RequestMethod.POST})
	@PassToken
	public ResponseEntity<Result> register(@RequestBody UserEntity userEntity, HttpServletRequest request){
		try{
			//最近登录ip
			userEntity.setLastLoginIp(IpAddressUtil.getIpAddr(request));

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

	@RequestMapping(value={"/error"},method = {RequestMethod.GET,RequestMethod.POST})
	@PassToken
	public String error(HttpServletRequest request){
		logger.info("===================error===================");
		return "error";
	}

}
