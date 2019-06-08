package com.website.imtechcat.controller;

import com.website.imtechcat.entity.TagEntity;
import com.website.imtechcat.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName TagController
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/5/27 14:42
 * @Version 1.0
 **/
@Slf4j
@Controller
public class TagController {

	private Logger logger = LoggerFactory.getLogger(TagController.class);

	@Resource
	private TagService tagServiceImpl;

	@RequestMapping(value = "/home/tags", method = RequestMethod.GET)
	public String tags(HttpServletRequest request){
		return "/home/tags";
	}

	@RequestMapping(value = "/api/1.0/tags", method = RequestMethod.GET)
	public List<TagEntity> findTags(String userId){

		return tagServiceImpl.findTagEntitiesByUserId(userId);
	}

	@RequestMapping(value = "/api/1.0/newtags",method = RequestMethod.POST)
	public int newTags(List<TagEntity> tagEntities){
		return tagServiceImpl.newTag(tagEntities);
	}

}
