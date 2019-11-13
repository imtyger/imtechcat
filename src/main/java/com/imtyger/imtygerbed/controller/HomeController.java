package com.imtyger.imtygerbed.controller;

import com.imtyger.imtygerbed.service.UserService;
import com.imtyger.imtygerbed.common.Constant;
import com.imtyger.imtygerbed.common.Result;
import com.imtyger.imtygerbed.util.CheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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
	private UserService userServiceImpl;

	@Resource
	private Constant constant;

	public String home(){
		return "/home";
	}

	@RequestMapping(value = {"/api/1.0/account"},method = {RequestMethod.GET})
	public ResponseEntity<Result> account(HttpServletRequest request){
		try {
			String userId = (String) request.getAttribute(constant.getUserKey());
			log.info(" account => " + userId);
			if(CheckUtil.isNull(userId)){
				return new ResponseEntity<>(Result.unAuth("解析userId失败"),HttpStatus.OK);
			}

			Map userMap = userServiceImpl.account(userId);
			if(userMap == null){
				return new ResponseEntity<>(Result.userNotFound("解析user失败"),HttpStatus.OK);
			}

			return new ResponseEntity<>(Result.success(userMap), HttpStatus.OK);
		}catch (Exception ex){
			String msg = ex.getMessage();
			log.warn(" account catch exception => " + ex);
			return new ResponseEntity<>(Result.fail(msg), HttpStatus.OK);
		}

	}


	@RequestMapping(value={"/logout"},method = RequestMethod.GET)
	public String logout(HttpServletRequest request){
		if(request.getAttribute(constant.getHeader()) != null){
			request.removeAttribute(constant.getHeader());
		}
		return "/";
	}

}
