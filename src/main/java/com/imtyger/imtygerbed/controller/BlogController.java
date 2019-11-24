package com.imtyger.imtygerbed.controller;

import com.imtyger.imtygerbed.bean.blog.BlogNewRequest;
import com.imtyger.imtygerbed.bean.blog.BlogUpdateRequest;
import com.imtyger.imtygerbed.common.Result;
import com.imtyger.imtygerbed.common.annotation.PassToken;
import com.imtyger.imtygerbed.model.BlogEdit;
import com.imtyger.imtygerbed.model.BlogList;
import com.imtyger.imtygerbed.model.BlogShow;
import com.imtyger.imtygerbed.service.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
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
	private BlogService blogService;


	@RequestMapping(value = "/api/1.0/home/blogs", method = RequestMethod.GET)
	public ResponseEntity<Result> getHomeBlogList(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
												@RequestParam(required = false, defaultValue = "30")Integer pageSize){
		Map result = blogService.queryHomeBlogs(pageNum, pageSize);
		return new ResponseEntity<>(Result.success(result),HttpStatus.OK);
	}

	@RequestMapping(value = "/api/1.0/blogs", method = RequestMethod.GET)
	@PassToken
	public ResponseEntity<Result> getBlogList(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
											  @RequestParam(required = false, defaultValue = "30")Integer pageSize){
		Map result = blogService.queryBlogs(pageNum, pageSize);
		return new ResponseEntity<>(Result.success(result),HttpStatus.OK);
	}

	@RequestMapping(value = "/api/1.0/about", method = RequestMethod.GET)
	@PassToken
    public ResponseEntity<Result> getAbout(){

		BlogShow blogShow = blogService.getAboutBlog();
		return new ResponseEntity<>(Result.success(blogShow),HttpStatus.OK);
	}

	@RequestMapping(value = "/api/1.0/blogs/post/{id}", method = RequestMethod.GET)
	@PassToken
	public ResponseEntity<Result> getBlogById(@PathVariable("id") @NotEmpty String id){
		BlogShow blogShow = blogService.postBlogId(id);
		return new ResponseEntity<>(Result.success(blogShow),HttpStatus.OK);
	}


	@RequestMapping(value = "/api/1.0/home/blogs/new",method = RequestMethod.POST)
	public ResponseEntity<Result> newBlog(@RequestBody @Valid BlogNewRequest blogNewRequest,
										  HttpServletRequest httpServletRequest){
		int count = blogService.newBlog(blogNewRequest,httpServletRequest);
		return new ResponseEntity<>(Result.success(count), HttpStatus.OK);
	}

	@RequestMapping(value = "/api/1.0/home/blogs/delete/{id}",method = RequestMethod.DELETE)
	public ResponseEntity<Result> deleteBlogById(@PathVariable("id") @NotEmpty String id) {
		int count = blogService.deleteBlogById(id);
		return new ResponseEntity<>(Result.success(count), HttpStatus.OK);
	}


	@RequestMapping(value = "/api/1.0/home/blogs/edit/{id}", method = RequestMethod.GET)
	public ResponseEntity<Result> getHomeBlogById(@PathVariable("id") @NotEmpty String id){
		BlogEdit blogEdit = blogService.getBlogById(id);
		return new ResponseEntity<>(Result.success(blogEdit),HttpStatus.OK);
	}


	@RequestMapping(value = "/api/1.0/home/blogs/update",method = RequestMethod.PUT)
	public ResponseEntity<Result> updateBlog(@RequestBody @Valid BlogUpdateRequest blogUpdateRequest) {
		int count = blogService.updateBlog(blogUpdateRequest);
		return new ResponseEntity<>(Result.success(count), HttpStatus.OK);
	}

	@RequestMapping(value = "/api/1.0/home/blogs/update/{id}/{status}",method = RequestMethod.PUT)
	public ResponseEntity<Result> updateBlogByIdAndStatus(@PathVariable("id") @NotEmpty String id,
														  @PathVariable("status") @NotEmpty Integer status, ModelMap modelMap) {
		int count = blogService.updateBlogStatus(id,status);
		modelMap.put("status", count);
		return new ResponseEntity<>(Result.success(modelMap), HttpStatus.OK);
	}

	@RequestMapping(value = "/api/1.0/tags/post/{tag}", method = RequestMethod.GET)
	@PassToken
	public ResponseEntity<Result> getBlogListByTag(@PathVariable("tag") String tag){
		List<BlogList> lists = blogService.getBlogListByTag(tag);
		return new ResponseEntity<>(Result.success(lists),HttpStatus.OK);
	}


//	@RequestMapping(value = "/api/1.0/blog/bytagname", method = RequestMethod.GET)
//	@PassToken
//	public ResponseEntity<Result> getBlogListByTagName(String tagName, ModelMap modelMap){
//		log.info("this getBlogListByTagName==> tagName : " + tagName);
//		if(CheckUtil.isNull(tagName)){
//			return new ResponseEntity<>(Result.fail("传递值null"),HttpStatus.OK);
//		}
//
//		try {
//			List<BlogEntity> blogEntityList = blogServiceImpl.findBlogEntitiesByTagName(tagName);
//
//			if(blogEntityList == null || blogEntityList.size() == 0){
//				return new ResponseEntity<>(Result.success(blogEntityList),HttpStatus.OK);
//			}
//
//			List<Map> blogList = new ArrayList<>();
//			for(BlogEntity blogEntity : blogEntityList){
//				modelMap.put("tagName",tagName);
//				modelMap.put("blogTitle",blogEntity.getBlogTitle());
//				modelMap.put("blogHtml",blogEntity.getBlogHtml());
//				blogList.add(modelMap);
//			}
//
//			return new ResponseEntity<>(Result.success(blogList),HttpStatus.OK);
//		}catch (Exception ex){
//			String msg = ex.getMessage();
//			log.error("get blog list by tag name catch exception=>",ex);
//			return new ResponseEntity<>(Result.fail(msg),HttpStatus.OK);
//
//		}
//
//	}

}
