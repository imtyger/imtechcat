package com.website.imtechcat.controller;

import com.website.imtechcat.common.Result;
import com.website.imtechcat.common.annotation.PassToken;
import com.website.imtechcat.entity.MarkEntity;
import com.website.imtechcat.service.MarkService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

	@Resource
	private MarkService markServiceImpl;

	@RequestMapping(value = "/home/marks", method = {RequestMethod.GET, RequestMethod.POST})
	@PassToken
	public String marks(HttpServletRequest request){

		return "/home/marks";
	}

	@RequestMapping(value = "/api/1.0/marks", method = RequestMethod.GET)
	public List<MarkEntity> findMarkEntitiesByUserId(String userId){
		return markServiceImpl.findMarkEntitiesByUserId(userId);
	}

	@RequestMapping(value = "/api/1.0/marksbymarkname",method = RequestMethod.GET)
	public List<MarkEntity> findMarkEntitiesByUserIdAndMarkName(String userId, String markName){
		return markServiceImpl.findMarkEntitiesByUserIdAndMarkName(userId,markName);
	}

	@RequestMapping(value = "/api/1.0/newmarks",method = RequestMethod.POST)
	public ResponseEntity<Result> newMarks(MarkEntity markEntity){
		markEntity.setCreatedAt(System.currentTimeMillis());
		markEntity.setLastUpdatedAt(System.currentTimeMillis());
		MarkEntity entity = markServiceImpl.newMarks(markEntity);
		if(entity == null || entity.getId() == null){
			return new ResponseEntity<>(Result.fail(), HttpStatus.OK);
		}
		return new ResponseEntity<>(Result.success(), HttpStatus.OK);
	}

}
