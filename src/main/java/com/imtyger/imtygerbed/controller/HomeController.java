package com.imtyger.imtygerbed.controller;

import com.imtyger.imtygerbed.common.Constant;
import com.imtyger.imtygerbed.common.Result;
import com.imtyger.imtygerbed.model.User;
import com.imtyger.imtygerbed.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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

	@Resource
	private UserService userService;

	@Resource
	private Constant constant;


	@RequestMapping(value = {"/api/1.0/account"},method = {RequestMethod.GET})
	public ResponseEntity<Result> account(HttpServletRequest request){
		String userId = (String) request.getAttribute(constant.getUserKey());

		User user = userService.account(userId);
		return new ResponseEntity<>(Result.success(user), HttpStatus.OK);
	}


	@RequestMapping(value={"/logout"},method = RequestMethod.GET)
	public String logout(HttpServletRequest request){
		if(request.getAttribute(constant.getHeader()) != null){
			request.removeAttribute(constant.getHeader());
		}
		return "/";
	}

}
