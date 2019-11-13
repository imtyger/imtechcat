package com.imtyger.imtygerbed.controller;

import com.imtyger.imtygerbed.common.Constant;
import com.imtyger.imtygerbed.common.Result;
import com.imtyger.imtygerbed.common.annotation.PassToken;
import com.imtyger.imtygerbed.entity.UserEntity;
import com.imtyger.imtygerbed.service.UserService;
import com.imtyger.imtygerbed.util.CheckUtil;
import com.imtyger.imtygerbed.util.IpAddressUtil;
import com.imtyger.imtygerbed.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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

	@Resource
	private UserService userServiceImpl;

	@Resource
	private JwtTokenUtil jwtTokenUtil;

	@Resource
	private Constant constant;


	public String login(){
		return "/login";
	}


	@RequestMapping(value={"/api/1.0/login"},method = RequestMethod.POST)
	@PassToken
	public ResponseEntity<Result> loginPost(@RequestBody UserEntity userEntity, HttpServletRequest request){
		log.info("method: loginPost=> " + userEntity.toString());
		if(null == userEntity){
			log.warn(" userEntity is null.");
			return new ResponseEntity<>(Result.fail(),HttpStatus.OK);
		}

		String username = userEntity.getUsername();
		String password = userEntity.getPassword();

		if (CheckUtil.isNull(username) || CheckUtil.isNull(password) ||
				CheckUtil.isSpecial(username) || CheckUtil.isSpaces(password)){
			log.warn(" username/password is null or special or has spaces.");
			return new ResponseEntity<>(Result.fail(),HttpStatus.OK);
		}

		int nameCharSize = CheckUtil.getCharNum(username);
		int pwdCharSize = CheckUtil.getCharNum(password);

		if(nameCharSize < 5 || nameCharSize > 20 || pwdCharSize < 5 || pwdCharSize > 20){
			log.warn(" username/password wrong length.");
			return new ResponseEntity<>(Result.fail(),HttpStatus.OK);
		}

		try{
			userEntity.setLastLoginIp(IpAddressUtil.getIpAddr(request));

			UserEntity user = userServiceImpl.login(userEntity);
			if(null == user){
				log.warn(" userEntity does not exist.");
				return new ResponseEntity<>(Result.userNotFound(),HttpStatus.OK);
			}

			String token = jwtTokenUtil.createToken(user);
			if(null == token || CheckUtil.isNull(token)){
				log.warn(" token is null");
				return new ResponseEntity<>(Result.unAuth(),HttpStatus.OK);
			}

			Map userMap = new HashMap();
			userMap.put("id", user.getId());
			userMap.put("avataricon", user.getAvataricon());
			userMap.put("nickname", user.getNickname());
			userMap.put("username", user.getUsername());

			Map result = new HashMap();
			result.put(constant.getToken(),token);
			result.put("refresh_token","");
			result.put("expires_in",Long.parseLong(constant.getExpire()) / 1000);
			result.put(constant.getUserKey(),userMap);
			return new ResponseEntity<>(Result.success(result),HttpStatus.OK);

		}catch(Exception ex){
			String msg = ex.getMessage();
			log.error("login post catch exception => " + ex);
			return new ResponseEntity<>(Result.fail(msg),HttpStatus.OK);
		}
	}

	public String error(HttpServletRequest request){
		log.info("===================error===================");
		return "error";
	}


}
