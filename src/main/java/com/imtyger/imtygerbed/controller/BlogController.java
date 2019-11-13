package com.imtyger.imtygerbed.controller;

import com.imtyger.imtygerbed.common.Constant;
import com.imtyger.imtygerbed.common.Result;
import com.imtyger.imtygerbed.common.annotation.PassToken;
import com.imtyger.imtygerbed.entity.BlogEntity;
import com.imtyger.imtygerbed.entity.UserEntity;
import com.imtyger.imtygerbed.model.Blog;
import com.imtyger.imtygerbed.service.BlogService;
import com.imtyger.imtygerbed.service.TagService;
import com.imtyger.imtygerbed.service.UserService;
import com.imtyger.imtygerbed.util.CheckUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
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
	public ResponseEntity<Result> queryBlogList(HttpServletRequest request, Integer pageNum, Integer pageSize,
												ModelMap modelMap){
		log.info("this.queryBlogList()==> {} , {}", pageNum, pageSize);
		if(null == pageNum || pageNum <= 0 || CheckUtil.isNull(pageNum+"")){
			pageNum = Integer.parseInt(constant.getNum());
		}
		if(null == pageSize || pageSize <= 0 || CheckUtil.isNull(pageSize+"")){
			pageSize = Integer.parseInt(constant.getSize());
		}

		try {
			Long count = blogServiceImpl.homeBlogCount();
			modelMap.put(constant.getCount(),count);

			if(count == 0){
				modelMap.put(constant.getPageNum(),pageNum);
				modelMap.put(constant.getPageSize(),pageSize);
				modelMap.put(constant.getPageTotal(),0);
				modelMap.put(constant.getList(),new ArrayList());
				return new ResponseEntity<>(Result.success(modelMap),HttpStatus.OK);
			}

			Page<BlogEntity> blogList = blogServiceImpl.findHomeBlogList(pageNum,pageSize,null);

			modelMap.put(constant.getPageNum(),pageNum);
			modelMap.put(constant.getPageSize(),pageSize);
			modelMap.put(constant.getPageTotal(),blogList.getTotalPages());
			modelMap.put(constant.getList(),blogList.getContent());

			return new ResponseEntity<>(Result.success(modelMap),HttpStatus.OK);
		}catch (Exception ex){
			String msg = ex.getMessage();
			log.error("query blog list catch an exception=>", ex);
			return new ResponseEntity<>(Result.fail(msg), HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/api/1.0/home/blog/new",method = RequestMethod.POST)
	public ResponseEntity<Result> newBlog(@RequestBody BlogEntity blogEntity, HttpServletRequest request){
		log.info("this.newBlog()==> ");
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

			String htmlBody = CheckUtil.getHtmlBody(blogEntity.getBlogHtml());
			if(htmlBody.length() > 200 ){
				blogEntity.setBlogProfile(htmlBody.substring(0,200));
			}else{
				blogEntity.setBlogProfile(htmlBody);
			}

			BlogEntity entity = blogServiceImpl.newBlog(blogEntity);
			if(entity == null || entity.getId() == null){
				return new ResponseEntity<>(Result.fail(), HttpStatus.OK);
			}

			List<String> tags = entity.getTags();
			if(tags != null && tags.size() != 0){
				tagServiceImpl.updateTagCount(tags,1);
			}

			Map result = new HashMap();
			result.put("id", entity.getId());
			result.put("blogTitle", entity.getBlogTitle());
			result.put("createdAt", entity.getCreatedAt());
			result.put("lastUpdatedAt", entity.getLastUpdatedAt());
			result.put("tags", entity.getTags());

			return new ResponseEntity<>(Result.success(result), HttpStatus.OK);
		}catch (Exception ex){
			String msg = ex.getMessage();
			log.error(" new blog catch an exception=>", ex);
			return new ResponseEntity<>(Result.fail(msg), HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/api/1.0/home/blog/update",method = RequestMethod.PUT)
	public ResponseEntity<Result> updateBlog(@RequestBody BlogEntity blogEntity) {
		log.info("this.updateBlog()==>" + " id:"+ blogEntity.getId());
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
			log.info(entity.toString());
			List<String> tags = entity.getTags();
			if(tags != null && tags.size() != 0) {
				tagServiceImpl.updateTagCount(tags, 1);
			}

			return new ResponseEntity<>(Result.success(), HttpStatus.OK);
		} catch (Exception ex) {
			String msg = ex.getMessage();
			log.error("update blog catch an exception=>", ex);
			return new ResponseEntity<>(Result.fail(msg), HttpStatus.OK);

		}
	}

	@RequestMapping(value = "/api/1.0/home/blog/update/{id}/{status}",method = RequestMethod.PUT)
	public ResponseEntity<Result> updateBlogByIdAndStatus(@PathVariable("id") String id, @PathVariable("status") Boolean status, ModelMap modelMap) {
		log.info("this.updateBlog()==>" + " id:"+ id + " , status:" + status);
		if(CheckUtil.isNull(id) || CheckUtil.isNull(status+"")){
			return new ResponseEntity<>(Result.fail("传递值null"),HttpStatus.OK);
		}

		try {

			BlogEntity entity = blogServiceImpl.findBlogEntityById(id);
			entity.setStatus(status);
			BlogEntity blogEntity = blogServiceImpl.updateBlog(entity);
			modelMap.put("status",blogEntity.isStatus());

			return new ResponseEntity<>(Result.success(modelMap), HttpStatus.OK);
		} catch (Exception ex) {
			String msg = ex.getMessage();
			log.error("update blog catch an exception=>", ex);
			return new ResponseEntity<>(Result.fail(msg), HttpStatus.OK);

		}
	}


	@RequestMapping(value = "/api/1.0/home/blog/delete/{id}",method = RequestMethod.DELETE)
	public ResponseEntity<Result> deleteBlogById(@PathVariable("id") String id) {
		log.info("this.deleteBlog()==>" + " id:"+ id );
        if (CheckUtil.isNull(id)){
			return new ResponseEntity<>(Result.unValid("参数缺失"),HttpStatus.OK);
		}

		try {
			BlogEntity blogEntity = blogServiceImpl.findBlogEntityById(id);
			if (blogEntity == null) {
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

	@RequestMapping(value = "/api/1.0/home/blog-edit/{id}", method = RequestMethod.GET)
	public ResponseEntity<Result> getHomeBlogById(@PathVariable("id") String id){
		log.info("this.getHomeBlogById()==> id:" + id );
		if(CheckUtil.isNull(id)){
			return new ResponseEntity<>(Result.fail("传递值null"),HttpStatus.OK);
		}

		try {
			BlogEntity blogEntity = blogServiceImpl.findBlogEntityById(id);

			return new ResponseEntity<>(Result.success(blogEntity),HttpStatus.OK);
		}catch (Exception ex){
			String msg = ex.getMessage();
			log.error("get home blog details by id catch an exception=>", ex);
			return new ResponseEntity<>(Result.fail(msg), HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/api/1.0/blogs/bytagname", method = RequestMethod.GET)
	@PassToken
	public ResponseEntity<Result> getBlogListByTagName(String tagName, ModelMap modelMap){
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
				modelMap.put("tagName",tagName);
				modelMap.put("blogTitle",blogEntity.getBlogTitle());
				modelMap.put("blogHtml",blogEntity.getBlogHtml());
				blogList.add(modelMap);
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
	public ResponseEntity<Result> getBlogList(Integer pageNum, Integer pageSize, ModelMap modelMap, Model model){
		log.info("this.getBlogList()==>" + " pageNum:"+ pageNum+ ", pageSize:"+pageSize);
		if(null == pageNum || pageNum <= 0 || CheckUtil.isNull(pageNum+"")){
			pageNum = Integer.parseInt(constant.getNum());
		}
		if(null == pageSize || pageSize <= 0 || pageSize > 300 || CheckUtil.isNull(pageSize+"")){
			pageSize = 30;
		}

		List<Blog> blogList = new ArrayList<>();
		try {
			Long count = blogServiceImpl.showBlogCount();
			modelMap.put(constant.getCount(),count);

			if(count == 0){
				modelMap.put(constant.getPageNum(),pageNum);
				modelMap.put(constant.getPageSize(),pageSize);
				modelMap.put(constant.getPageTotal(),0);
				modelMap.put(constant.getList(),blogList);
				return new ResponseEntity<>(Result.success(modelMap),HttpStatus.OK);
			}

			Page<BlogEntity> blogs = blogServiceImpl.findBlogList(pageNum,pageSize,null);
			for(BlogEntity entity : blogs.getContent()){
				Blog blog = new Blog();
				blog.setId(entity.getId());
				blog.setBlogTitle(entity.getBlogTitle());
				blog.setBlogProfile(entity.getBlogProfile());
				blog.setTags(entity.getTags());
				blog.setCreatedAt(entity.getCreatedAt());
				blogList.add(blog);
			}

			modelMap.put(constant.getPageNum(),pageNum);
			modelMap.put(constant.getPageSize(),pageSize);
			modelMap.put(constant.getPageTotal(),blogs.getTotalPages());
			modelMap.put(constant.getList(),blogList);

			return new ResponseEntity<>(Result.success(modelMap),HttpStatus.OK);
		}catch (Exception ex){
			String msg = ex.getMessage();
			log.error("get blog list catch an exception=>", ex);
			return new ResponseEntity<>(Result.fail(msg), HttpStatus.OK);
		}
	}


	@RequestMapping(value = "/api/1.0/blogs/post/{id}", method = RequestMethod.GET)
	@PassToken
	public ResponseEntity<Result> getBlogDetailsById(@PathVariable("id") String id, ModelMap modelMap){
		log.info("this.getBlogDetailsById()==> id:" + id );
		if(CheckUtil.isNull(id)){
			return new ResponseEntity<>(Result.fail("传递值null"),HttpStatus.OK);
		}

		try {
			BlogEntity blogEntity = blogServiceImpl.findBlogEntityById(id);
			if(blogEntity != null){
				long visitCount = blogEntity.getVisitCount() + 1;
				blogEntity.setVisitCount(visitCount);
				BlogEntity entity = blogServiceImpl.updateBlogVisitCount(blogEntity);

				modelMap.addAttribute("id",entity.getId());
				modelMap.addAttribute("blogTitle", entity.getBlogTitle());
				modelMap.addAttribute("blogHtml", entity.getBlogHtml());
				modelMap.addAttribute("tags",entity.getTags());
				modelMap.addAttribute("createdAt",entity.getCreatedAt());

			}

			return new ResponseEntity<>(Result.success(modelMap),HttpStatus.OK);
		}catch (Exception ex){
			String msg = ex.getMessage();
			log.error("get blog details by id catch an exception=>", ex);
			return new ResponseEntity<>(Result.fail(msg), HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/api/1.0/tags/post/{tagName}", method = RequestMethod.GET)
	@PassToken
	public ResponseEntity<Result> getTagNameList(@PathVariable("tagName") String tagName){
		log.info(" this getTagNameList==> tagName : {} " , tagName);
		if(CheckUtil.isNull(tagName)){
			return new ResponseEntity<>(Result.fail("传递值null"),HttpStatus.OK);
		}
		List<Map> result = new ArrayList<>();
		try {
			List<BlogEntity> list = blogServiceImpl.findBlogEntitiesByTagName(tagName);
			if(list != null){
				for(BlogEntity entity : list){
					Map map = new HashMap();
					map.put("id",entity.getId());
					map.put("blogTitle",entity.getBlogTitle());
					map.put("createdAt",entity.getCreatedAt());
					result.add(map);
				}
			}

			return new ResponseEntity<>(Result.success(result),HttpStatus.OK);
		}catch (Exception ex){
			String msg = ex.getMessage();
			log.error(" get tag name list catch exception => " + ex);
			return new ResponseEntity<>(Result.fail(msg),HttpStatus.OK);
		}
	}


    @RequestMapping(value = "/api/1.0/about", method = RequestMethod.GET)
    @PassToken
    public ResponseEntity<Result> getAbout(ModelMap modelMap){
        log.info("this.getAbout()==> ");

        try {
            BlogEntity blogEntity = blogServiceImpl.findBlogEntityByBlogTitleLike("关于本博客");
            if(blogEntity != null){
                long visitCount = blogEntity.getVisitCount() + 1;
                blogEntity.setVisitCount(visitCount);
                BlogEntity entity = blogServiceImpl.updateBlogVisitCount(blogEntity);

                modelMap.addAttribute("id",entity.getId());
                modelMap.addAttribute("blogTitle", entity.getBlogTitle());
                modelMap.addAttribute("blogHtml", entity.getBlogHtml());
                modelMap.addAttribute("tags",entity.getTags());
                modelMap.addAttribute("createdAt",entity.getCreatedAt());

            }

            return new ResponseEntity<>(Result.success(modelMap),HttpStatus.OK);
        }catch (Exception ex){
            String msg = ex.getMessage();
            log.error("get about catch an exception=>", ex);
            return new ResponseEntity<>(Result.fail(msg), HttpStatus.OK);
        }
    }
}
