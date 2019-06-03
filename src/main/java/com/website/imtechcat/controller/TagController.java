package com.website.imtechcat.controller;

import com.website.imtechcat.entity.TagEntity;
import com.website.imtechcat.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
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

	@RequestMapping("/tags/tags")
	public List<TagEntity> findTags(String userId){
		return tagServiceImpl.findTagEntitiesByUserId(userId);
	}

	@RequestMapping("/tags/newtags")
	public int newTags(List<TagEntity> tagEntities){
		return tagServiceImpl.newTag(tagEntities);
	}



}
