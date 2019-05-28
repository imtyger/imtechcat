package com.website.imtechcat.controller;

import com.website.imtechcat.entity.Mark;
import com.website.imtechcat.entity.User;
import com.website.imtechcat.service.MarkService;
import com.website.imtechcat.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName MarkController
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/5/27 14:42
 * @Version 1.0
 **/
@Slf4j
@Controller
public class MarkController {

	private Logger logger = LoggerFactory.getLogger(MarkController.class);

	@Resource
	private MarkService markServiceImpl;

	@Resource
	private UserService userServiceImpl;

	@RequestMapping("/{userName}/marks/marklistbyuser")
	public List<Mark> findMarksByUser(User user){
		String userId = userServiceImpl.findUserId(user);
		return markServiceImpl.findMarksByUserId(userId);
	}

	@RequestMapping("/{userName}/marks/marklistbymarkname")
	public List<Mark> findMarksByMarkName(String markName){
		return markServiceImpl.findMarksByMarkName(markName);
	}

}
