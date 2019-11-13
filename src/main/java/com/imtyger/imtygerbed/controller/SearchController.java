package com.imtyger.imtygerbed.controller;

import com.imtyger.imtygerbed.common.Constant;
import com.imtyger.imtygerbed.common.Result;
import com.imtyger.imtygerbed.common.annotation.PassToken;
import com.imtyger.imtygerbed.entity.BlogEntity;
import com.imtyger.imtygerbed.model.Blog;
import com.imtyger.imtygerbed.service.BlogService;
import com.imtyger.imtygerbed.util.CheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName SearchController
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/6/20 15:38
 * @Version 1.0
 **/
@Slf4j
@Controller
@RequestMapping("/")
public class SearchController {

	@Resource
	private BlogService blogServiceImpl;

	@Resource
	private Constant constant;


	@RequestMapping(value={"/api/1.0/search"},method = RequestMethod.GET)
	@PassToken
	public ResponseEntity<Result> searchBlogList(@RequestParam(value="q",required=false)String query,
												 ModelMap modelMap){
		log.info(" this search blog list ==> query : " + query);
		List<Blog> blogList = new ArrayList<>();
		if(CheckUtil.isNull(query)){
			modelMap.put("list",blogList);
			return new ResponseEntity<>(Result.success(modelMap),HttpStatus.OK);
		}

		List<BlogEntity> blogEntities = blogServiceImpl.findBlogEntitiesByQuery(query);
		for(BlogEntity entity: blogEntities){
			Blog blog = new Blog();
			blog.setId(entity.getId());
			blog.setBlogTitle(entity.getBlogTitle());
			blog.setBlogProfile(entity.getBlogProfile());
			blog.setCreatedAt(entity.getCreatedAt());
			blog.setTags(entity.getTags());
			blogList.add(blog);
		}
		modelMap.put("list",blogList);
		return new ResponseEntity<>(Result.success(modelMap),HttpStatus.OK);
	}
}
