package com.imtyger.imtygerbed.controller;

import com.imtyger.imtygerbed.bean.user.LoginRequest;
import com.imtyger.imtygerbed.common.Result;
import com.imtyger.imtygerbed.common.annotation.PassToken;
import com.imtyger.imtygerbed.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
	private UserService userService;


	@RequestMapping(value={"/api/1.0/login"},method = RequestMethod.POST)
	@PassToken
	public ResponseEntity<Result> loginPost(@RequestBody @Valid LoginRequest loginRequest,
											HttpServletRequest request) throws Exception {
		Map result = userService.login(loginRequest, request);
		return new ResponseEntity<>(Result.success(result), HttpStatus.OK);
	}


	public String error(HttpServletRequest request){
		log.info("===================error===================");
		return "error";
	}


}
