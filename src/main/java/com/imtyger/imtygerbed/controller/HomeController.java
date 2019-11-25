package com.imtyger.imtygerbed.controller;

import com.imtyger.imtygerbed.common.Result;
import com.imtyger.imtygerbed.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author imtygerx@gmail.com
 * @Date 2019/6/8 16:27
 */

@RestController
@Slf4j
@RequestMapping("/api")
public class HomeController {

	@Value("${jwt.userKey}")
	private String userKey;

	@Resource
	private UserService userService;

	@RequestMapping(value = {"/1.0/account"}, method = {RequestMethod.GET})
	public Result account(HttpServletRequest request){
		String userId = (String) request.getAttribute(userKey);
		return Result.success(userService.account(userId));
	}

	@RequestMapping(value = {"/1.0/logout"}, method = RequestMethod.GET)
	public Result logout(){
		// 前端删除local storage里的token，后端无需处理，返回成功即可
		return Result.success();
	}
}
