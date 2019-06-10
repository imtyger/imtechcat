package com.website.imtechcat.controller;

import com.website.imtechcat.common.Result;
import com.website.imtechcat.common.annotation.PassToken;
import com.website.imtechcat.entity.TagEntity;
import com.website.imtechcat.entity.UserEntity;
import com.website.imtechcat.service.TagService;
import com.website.imtechcat.service.UserService;
import com.website.imtechcat.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ClassName HomeController
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/6/8 16:27
 * @Version 1.0
 **/
@Slf4j
@Controller
@RequestMapping("/")
public class HomeController {

	private Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Resource
	private JwtUtil jwtUtil;

	@Resource
	private UserService userServiceImpl;

	@Resource
	private TagService tagServiceImpl;


	@RequestMapping(value={"/home"},method = RequestMethod.GET)
	@PassToken
	public String home(){
		return "/home";
	}

	@RequestMapping(value = {"/api/1.0/account"},method = {RequestMethod.GET,RequestMethod.POST})
	public ResponseEntity<Result> account(HttpServletRequest request){
		String userId = (String) request.getAttribute("userId");
		if(!StringUtils.isEmpty(userId)){
			UserEntity userEntity = userServiceImpl.findUserEntityById(userId);
			return new ResponseEntity<>(Result.success(userEntity), HttpStatus.OK);
		}
		return new ResponseEntity<>(Result.unAuth(),HttpStatus.OK);
	}

	@RequestMapping(value = {"/api/1.0/hometags"},method = {RequestMethod.GET,RequestMethod.POST})
	public List<TagEntity> homeTags(@RequestBody UserEntity userEntity){
		List<TagEntity> list = tagServiceImpl.findTagEntitiesByUserId(userEntity.getId());
		return list;
	}

	@RequestMapping(value={"/logout"},method = {RequestMethod.GET,RequestMethod.POST})
	@PassToken
	public String logout(HttpServletRequest request){
		if(request.getAttribute("Authorization") != null){
			request.removeAttribute("Authorization");
		}
		return "/login";
	}

}
