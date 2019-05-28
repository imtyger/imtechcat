package com.website.imtechcat.controller;

import com.website.imtechcat.entity.Tag;
import com.website.imtechcat.repository.TagRepository;
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

	@RequestMapping("/{userName}/tags/tagslist")
	public List<Tag> findTags(){
		return tagServiceImpl.findTags();
	}

	@RequestMapping("/{userName}/tags/addtags")
	public int addTags(List<Tag> tagList){
		return 0;
	}

	@RequestMapping("/{userName}/tags/deltags")
	public int delTags(List<Tag> tagList){
		return 0;
	}


}
