package com.website.imtechcat.controller;

import com.website.imtechcat.common.Constant;
import com.website.imtechcat.common.Result;
import com.website.imtechcat.common.annotation.PassToken;
import com.website.imtechcat.entity.BlogEntity;
import com.website.imtechcat.model.Blog;
import com.website.imtechcat.service.BlogService;
import com.website.imtechcat.util.CheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
