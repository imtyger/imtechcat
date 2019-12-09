package com.imtyger.imtygerbed.controller;

import com.imtyger.imtygerbed.annotation.PassToken;
import com.imtyger.imtygerbed.bean.user.LoginRequest;
import com.imtyger.imtygerbed.common.Result;
import com.imtyger.imtygerbed.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @Author imtygerx@gmail.com
 * @Date 2019/5/26 12:10
 */

@RestController
@Slf4j
@RequestMapping("/api")
public class UserController {

	@Resource
	private UserService userService;

	@RequestMapping(value={"/1.0/login"},method = RequestMethod.POST)
	@PassToken
	public Result loginPost(@RequestBody @Valid LoginRequest loginRequest,
							HttpServletRequest request) {
		return Result.success(userService.login(loginRequest, request));
	}

	@RequestMapping(value={"/1.0/home/record/login"},method = RequestMethod.GET)
	public Result loginInfo(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
							@RequestParam(required = false, defaultValue = "30")Integer pageSize) {
		return Result.success(userService.queryLoginInfo(pageNum,pageSize));
	}
}
