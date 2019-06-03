package com.website.imtechcat.controller;

import com.website.imtechcat.entity.MarkEntity;
import com.website.imtechcat.service.MarkService;
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

	@RequestMapping("/marks/marks")
	public List<MarkEntity> findMarkEntitiesByUserId(String userId){
		return markServiceImpl.findMarkEntitiesByUserId(userId);
	}

	@RequestMapping("/marks/marksbymarkname")
	public List<MarkEntity> findMarkEntitiesByMarkName(String markName){
		return markServiceImpl.findMarkEntitiesByMarkName(markName);
	}

}
