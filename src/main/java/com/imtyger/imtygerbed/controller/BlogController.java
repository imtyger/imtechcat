package com.imtyger.imtygerbed.controller;

import com.imtyger.imtygerbed.common.Result;
import com.imtyger.imtygerbed.bean.blog.BlogNewRequest;
import com.imtyger.imtygerbed.bean.blog.BlogUpdateRequest;
import com.imtyger.imtygerbed.annotation.PassToken;
import com.imtyger.imtygerbed.service.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * @Author Lenovo
 * @Date 2019/7/8 11:03
 */

@RestController
@RequestMapping("/api")
@Slf4j
public class BlogController {

	@Resource
	private BlogService blogService;

	@RequestMapping(value = "/1.0/home/blogs", method = RequestMethod.GET)
	public Result getHomeBlogList(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
								  @RequestParam(required = false, defaultValue = "30") Integer pageSize){
		return Result.success(blogService.queryHomeBlogs(pageNum, pageSize));
	}

	@RequestMapping(value = "/1.0/blogs", method = RequestMethod.GET)
	@PassToken
	public Result getBlogList(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
							  @RequestParam(required = false, defaultValue = "30")Integer pageSize){
		return Result.success(blogService.queryBlogs(pageNum, pageSize));
	}

	@RequestMapping(value = "/1.0/about", method = RequestMethod.GET)
	@PassToken
    public Result getAbout(){
		return Result.success(blogService.getAboutBlog());
	}

	@RequestMapping(value = "/1.0/blogs/post/{id}", method = RequestMethod.GET)
	@PassToken
	public Result getBlogById(@PathVariable("id") @NotEmpty String id){
		return Result.success(blogService.postBlogId(id));
	}

	@RequestMapping(value = "/1.0/home/blogs/new",method = RequestMethod.POST)
	public Result newBlog(@RequestBody @Valid BlogNewRequest blogNewRequest,
										  HttpServletRequest httpServletRequest){
		int count = blogService.newBlog(blogNewRequest,httpServletRequest);
		return Result.success(count);
	}

	@RequestMapping(value = "/1.0/home/blogs/delete/{id}",method = RequestMethod.DELETE)
	public Result deleteBlogById(@PathVariable("id") @NotEmpty String id) {
		int count = blogService.deleteBlogById(id);
		return Result.success(count);
	}


	@RequestMapping(value = "/1.0/home/blogs/edit/{id}", method = RequestMethod.GET)
	public Result getHomeBlogById(@PathVariable("id") @NotEmpty String id){
		return Result.success(blogService.getBlogById(id));
	}


	@RequestMapping(value = "/1.0/home/blogs/update",method = RequestMethod.PUT)
	public Result updateBlog(@RequestBody @Valid BlogUpdateRequest blogUpdateRequest) {
		int count = blogService.updateBlog(blogUpdateRequest);
		return Result.success(count);
	}

	@RequestMapping(value = "/1.0/home/blogs/update/{id}/{status}",method = RequestMethod.PUT)
	public Result updateBlogByIdAndStatus(@PathVariable("id") @NotEmpty String id,
										  @PathVariable("status") @NotEmpty Integer status, ModelMap modelMap) {
		int count = blogService.updateBlogStatus(Integer.parseInt(id), status);
		modelMap.put("status", count);
		return Result.success(modelMap);
	}

	@RequestMapping(value = "/1.0/tags/post/{tag}", method = RequestMethod.GET)
	@PassToken
	public Result getBlogListByTag(@PathVariable("tag") String tag){
		return Result.success(blogService.getBlogListByTag(tag));
	}
}
