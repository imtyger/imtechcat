package com.website.imtechcat.controller;

import com.website.imtechcat.common.Constant;
import com.website.imtechcat.common.Result;
import com.website.imtechcat.common.annotation.PassToken;
import com.website.imtechcat.entity.BlogEntity;
import com.website.imtechcat.entity.UserEntity;
import com.website.imtechcat.model.Blog;
import com.website.imtechcat.service.BlogService;
import com.website.imtechcat.service.TagService;
import com.website.imtechcat.service.UserService;
import com.website.imtechcat.util.CheckUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @ClassName BlogController
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/7/8 11:03
 * @Version 1.0
 **/
@Slf4j
@Controller
@RequestMapping("/")
public class BlogController {

	@Resource
	private BlogService blogServiceImpl;

	@Resource
	private UserService userServiceImpl;

	@Resource
	private TagService tagServiceImpl;

	@Resource
	private Constant constant;

	public String blog(HttpServletRequest request){
		return "/home/blog";
	}

	@RequestMapping(value = "/api/1.0/home/blogs", method = RequestMethod.GET)
	public ResponseEntity<Result> queryBlogList(HttpServletRequest request,Integer pageNum, Integer pageSize){
		log.info("this.queryBlogList()==>" + " pageNum:"+ pageNum+ ", pageSize:"+pageSize);
		if(null == pageNum || pageNum <= 0 || CheckUtil.isNull(pageNum+"")){
			pageNum = Integer.parseInt(constant.getNum());
		}
		if(null == pageSize || pageSize <= 0 || CheckUtil.isNull(pageSize+"")){
			pageSize = Integer.parseInt(constant.getSize());
		}

		try {
			Long count = blogServiceImpl.blogCount();

			Map map = new HashMap();
			map.put(constant.getCount(),count);

			if(count == 0 || pageNum > count){
				map.put(constant.getPageNum(),pageNum);
				map.put(constant.getPageSize(),pageSize);
				map.put(constant.getPageTotal(),0);
				map.put(constant.getList(),"");
				return new ResponseEntity<>(Result.success(map),HttpStatus.OK);
			}

			Page<BlogEntity> blogList = blogServiceImpl.findBlogList(pageNum,pageSize,null);

			map.put(constant.getPageNum(),pageNum);
			map.put(constant.getPageSize(),pageSize);
			map.put(constant.getPageTotal(),blogList.getTotalPages());
			map.put(constant.getList(),blogList.getContent());

			return new ResponseEntity<>(Result.success(map),HttpStatus.OK);
		}catch (Exception ex){
			String msg = ex.getMessage();
			log.error("query blog list catch an exception=>", ex);
			return new ResponseEntity<>(Result.fail(msg), HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/api/1.0/home/blog/new",method = RequestMethod.POST)
	public ResponseEntity<Result> newBlog(@RequestBody BlogEntity blogEntity, HttpServletRequest request){
		if(blogEntity == null){
			return new ResponseEntity<>(Result.fail("传递值null"), HttpStatus.OK);
		}else if (CheckUtil.isNull(blogEntity.getBlogTitle()) || CheckUtil.isNull(blogEntity.getBlogContent())
				|| CheckUtil.isNull(blogEntity.getBlogHtml())){
			return new ResponseEntity<>(Result.unValid("参数缺失"),HttpStatus.OK);
		}

		try {
			String userId = (String) request.getAttribute(constant.getUserKey());
			if(CheckUtil.isNull(userId)){
				return new ResponseEntity<>(Result.unValid("解析userId失败"), HttpStatus.OK);
			}

			UserEntity userEntity = userServiceImpl.findUserEntityById(userId);
			String nickName = userEntity.getNickname();
			String userName = userEntity.getUsername();
			if(CheckUtil.isNull(nickName)){
				blogEntity.setAuthor(userName);
			}else{
				blogEntity.setAuthor(nickName);
			}

			BlogEntity entity = blogServiceImpl.newBlog(blogEntity);
			if(entity == null || entity.getId() == null){
				return new ResponseEntity<>(Result.fail(), HttpStatus.OK);
			}

			List<String> tags = entity.getTags();
			if(tags != null && tags.size() != 0){
				tagServiceImpl.updateTagCount(tags,1);
			}

			return new ResponseEntity<>(Result.success(entity), HttpStatus.OK);
		}catch (Exception ex){
			String msg = ex.getMessage();
			log.error(" new blog catch an exception=>", ex);
			return new ResponseEntity<>(Result.fail(msg), HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/api/1.0/home/blog/update",method = RequestMethod.PUT)
	public ResponseEntity<Result> updateBlog(@RequestBody BlogEntity blogEntity) {
		if (blogEntity == null){
			return new ResponseEntity<>(Result.fail("未收到传递值"), HttpStatus.OK);
		}else if (CheckUtil.isNull(blogEntity.getId())){
			return new ResponseEntity<>(Result.unValid("参数缺失"),HttpStatus.OK);
		}

		try {
			boolean b = blogServiceImpl.findById(blogEntity);
			if (false == b) {
				return new ResponseEntity<>(Result.fail("博客不存在"), HttpStatus.OK);
			}

			BlogEntity entity = blogServiceImpl.updateBlog(blogEntity);
			List<String> tags = entity.getTags();
			if(tags != null && tags.size() != 0){
				tagServiceImpl.updateTagCount(tags,1);
			}

			return new ResponseEntity<>(Result.success(entity), HttpStatus.OK);
		} catch (Exception ex) {
			String msg = ex.getMessage();
			log.error("update blog catch an exception=>", ex);
			return new ResponseEntity<>(Result.fail(msg), HttpStatus.OK);

		}
	}


	@RequestMapping(value = "/api/1.0/home/blog/delete",method = RequestMethod.DELETE)
	public ResponseEntity<Result> deleteBlog(@RequestBody BlogEntity blogEntity) {
		if (blogEntity == null) {
			return new ResponseEntity<>(Result.fail("未收到传递值"), HttpStatus.OK);
		}else if (CheckUtil.isNull(blogEntity.getId())){
			return new ResponseEntity<>(Result.unValid("参数缺失"),HttpStatus.OK);
		}

		try {
			boolean b = blogServiceImpl.findById(blogEntity);
			if (false == b) {
				return new ResponseEntity<>(Result.fail("博客不存在"), HttpStatus.OK);
			}
			List<String> tags = blogEntity.getTags();
			blogServiceImpl.deleteBlog(blogEntity);

			if(tags != null && tags.size() != 0){
				tagServiceImpl.updateTagCount(tags,-1);
			}
			return new ResponseEntity<>(Result.success(), HttpStatus.OK);
		} catch (Exception ex) {
			String msg = ex.getMessage();
			log.error("delete blog catch an exception=>", ex);
			return new ResponseEntity<>(Result.fail(msg), HttpStatus.OK);

		}
	}

	@RequestMapping(value = "/api/1.0/blogs/bytagname", method = RequestMethod.GET)
	@PassToken
	public ResponseEntity<Result> getBlogListByTagName(String tagName){
		log.info("this getBlogListByTagName==> tagName : " + tagName);
		if(CheckUtil.isNull(tagName)){
			return new ResponseEntity<>(Result.fail("传递值null"),HttpStatus.OK);
		}

		try {
			List<BlogEntity> blogEntityList = blogServiceImpl.findBlogEntitiesByTagName(tagName);

			if(blogEntityList == null || blogEntityList.size() == 0){
				return new ResponseEntity<>(Result.success(blogEntityList),HttpStatus.OK);
			}

			List<Map> blogList = new ArrayList<>();
			for(BlogEntity blogEntity : blogEntityList){
				Map map = new HashMap();
				map.put("tagName",tagName);
				map.put("blogTitle",blogEntity.getBlogTitle());
				map.put("blogHtml",blogEntity.getBlogHtml());
				blogList.add(map);
			}

			return new ResponseEntity<>(Result.success(blogList),HttpStatus.OK);
		}catch (Exception ex){
			String msg = ex.getMessage();
			log.error("get blog list by tag name catch exception=>",ex);
			return new ResponseEntity<>(Result.fail(msg),HttpStatus.OK);

		}

	}


	@RequestMapping(value = "/api/1.0/blogs", method = RequestMethod.GET)
	@PassToken
	public ResponseEntity<Result> getBlogList(Integer pageNum, Integer pageSize){
		log.info("this.getBlogList()==>" + " pageNum:"+ pageNum+ ", pageSize:"+pageSize);
		if(null == pageNum || pageNum <= 0 || CheckUtil.isNull(pageNum+"")){
			pageNum = Integer.parseInt(constant.getNum());
		}
		if(null == pageSize || pageSize <= 0 || pageSize > 300 || CheckUtil.isNull(pageSize+"")){
			pageSize = 30;
		}
		Map resultMap = new HashMap();
		List<Blog> blogList = new ArrayList<>();
		try {
			Long count = blogServiceImpl.blogCount();
			resultMap.put(constant.getCount(),count);

			if(count == 0){
				resultMap.put(constant.getPageNum(),pageNum);
				resultMap.put(constant.getPageSize(),pageSize);
				resultMap.put(constant.getPageTotal(),0);
				resultMap.put(constant.getList(),blogList);
				return new ResponseEntity<>(Result.success(resultMap),HttpStatus.OK);
			}

			Page<BlogEntity> blogs = blogServiceImpl.findBlogList(pageNum,pageSize,null);
			List<BlogEntity> list = blogs.getContent();
			for(BlogEntity entity : list){
				Blog blog = new Blog();
				blog.setId(entity.getId());
				blog.setBlogTitle(entity.getBlogTitle());
				blog.setBlogHtml(entity.getBlogHtml());
				blog.setTags(entity.getTags());
				blog.setCreatedAt(entity.getCreatedAt());
				blogList.add(blog);
			}

			resultMap.put(constant.getPageNum(),pageNum);
			resultMap.put(constant.getPageSize(),pageSize);
			resultMap.put(constant.getPageTotal(),blogs.getTotalPages());
			resultMap.put(constant.getList(),blogList);

			return new ResponseEntity<>(Result.success(resultMap),HttpStatus.OK);
		}catch (Exception ex){
			String msg = ex.getMessage();
			log.error("get blog list catch an exception=>", ex);
			return new ResponseEntity<>(Result.fail(msg), HttpStatus.OK);
		}
	}


	@RequestMapping(value = "/api/1.0/blogs/post/{id}", method = RequestMethod.GET)
	@PassToken
	public ResponseEntity<Result> getBlogDetailsById(@PathVariable("id") String id){
		log.info("this.getBlogDetailsById()==> id:" + id );
		if(CheckUtil.isNull(id)){
			return new ResponseEntity<>(Result.fail("传递值null"),HttpStatus.OK);
		}

		try {
			BlogEntity blogEntity = blogServiceImpl.findBlogEntityById(id);

			long visitCount = blogEntity.getVisitCount() + 1;
			blogEntity.setVisitCount(visitCount);
			BlogEntity entity = blogServiceImpl.updateBlog(blogEntity);

			Blog blog = new Blog();
			blog.setId(entity.getId());
			blog.setBlogTitle(entity.getBlogTitle());
			blog.setBlogHtml(entity.getBlogHtml());
			blog.setTags(entity.getTags());
			blog.setCreatedAt(entity.getCreatedAt());

			return new ResponseEntity<>(Result.success(blog),HttpStatus.OK);
		}catch (Exception ex){
			String msg = ex.getMessage();
			log.error("get blog details by id catch an exception=>", ex);
			return new ResponseEntity<>(Result.fail(msg), HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/api/1.0/tags/post/{tagName}", method = RequestMethod.GET)
	@PassToken
	public ResponseEntity<Result> getTagNameList(@PathVariable("tagName") String tagName){
		log.info(" this getTagNameList==> tagName : " + tagName);
		if(CheckUtil.isNull(tagName)){
			return new ResponseEntity<>(Result.fail("传递值null"),HttpStatus.OK);
		}

		try {
			List<BlogEntity> list = blogServiceImpl.findBlogEntitiesByTagName(tagName);
			if(list == null){
				return new ResponseEntity<>(Result.success(null),HttpStatus.OK);
			}

			List<Map> result = new ArrayList<>();
			for(BlogEntity entity : list){
				Map map = new HashMap();
				map.put("id",entity.getId());
				map.put("blogTitle",entity.getBlogTitle());
				map.put("createdAt",entity.getCreatedAt());
				result.add(map);
			}

			return new ResponseEntity<>(Result.success(result),HttpStatus.OK);
		}catch (Exception ex){
			String msg = ex.getMessage();
			log.error(" get tag name list catch exception => " + ex);
			return new ResponseEntity<>(Result.fail(msg),HttpStatus.OK);
		}
	}
}
