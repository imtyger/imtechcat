package com.website.imtechcat.controller;

import com.website.imtechcat.common.Constant;
import com.website.imtechcat.common.Result;
import com.website.imtechcat.common.annotation.PassToken;
import com.website.imtechcat.entity.TagEntity;
import com.website.imtechcat.entity.UserEntity;
import com.website.imtechcat.service.TagService;
import com.website.imtechcat.service.UserService;
import com.website.imtechcat.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName HomeController
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/6/8 16:27
 * @Version 1.0
 **/
@Slf4j
@Controller
@RequestMapping("/")
public class HomeController {

	@Resource
	private JwtTokenUtil jwtTokenUtil;

	@Resource
	private UserService userServiceImpl;

	@Resource
	private TagService tagServiceImpl;

	@Resource
	private Constant constant;


	@RequestMapping(value={"/home"},method = RequestMethod.GET)
	@PassToken
	public String home(){
		return "/home";
	}

	@RequestMapping(value = {"/api/1.0/account"},method = {RequestMethod.GET,RequestMethod.POST})
	public ResponseEntity<Result> account(HttpServletRequest request){
		String user = (String) request.getAttribute(constant.getUserKey());
		if(!StringUtils.isEmpty(user)){
			UserEntity userEntity = userServiceImpl.findUserEntityById(user);
			return new ResponseEntity<>(Result.success(userEntity), HttpStatus.OK);
		}
		return new ResponseEntity<>(Result.unAuth(),HttpStatus.OK);
	}

	@RequestMapping(value = {"/api/1.0/hometags"},method = {RequestMethod.GET})
	public ResponseEntity<Result> homeTags(Integer pageNum, Integer pageSize){
		Long tagsCount = tagServiceImpl.tagsCount();

		Map map = new HashMap();
		map.put(constant.getTagsCount(),tagsCount);

		if(tagsCount == 0){
			return new ResponseEntity<>(Result.fail(constant.getTagsZero(),map),HttpStatus.OK);
		}

		Page<TagEntity> tagEntityPage = tagServiceImpl.findAll(pageNum,pageSize,null);
		if(tagEntityPage.isEmpty() || tagEntityPage == null){
			map.put(constant.getTags(),null);
			return new ResponseEntity<>(Result.fail(constant.getTagsEmpty(),map),HttpStatus.OK);
		}

		map.put(constant.getTagsPageNum(),pageNum);
		map.put(constant.getTagsPageSize(),pageSize);
		map.put(constant.getTagsPageTotal(),tagEntityPage.getTotalPages());
		map.put(constant.getTags(),tagEntityPage.getContent());

		return new ResponseEntity<>(Result.success(map),HttpStatus.OK);
	}


	@RequestMapping(value = {"/api/1.0/hometagsbytagname"},method = {RequestMethod.GET})
	public ResponseEntity<Result> homeTagsByTagName(String tagName, Integer pageNum, Integer pageSize){
		Long tagsCount = tagServiceImpl.tagsCountByTagName(tagName);

		Map map = new HashMap();
		map.put(constant.getTagsCount(),tagsCount);

		if(tagsCount == 0){
			return new ResponseEntity<>(Result.fail(constant.getTagsZero(),map),HttpStatus.OK);
		}

		Page<TagEntity> tagEntityPage = tagServiceImpl.findAll(pageNum,pageSize,null);
		if(tagEntityPage.isEmpty() || tagEntityPage == null){
			map.put(constant.getTags(),null);
			return new ResponseEntity<>(Result.fail(constant.getTagsEmpty(),map),HttpStatus.OK);
		}

		map.put(constant.getTagsPageNum(),pageNum);
		map.put(constant.getTagsPageSize(),pageSize);
		map.put(constant.getTagsPageTotal(),tagEntityPage.getTotalPages());
		map.put(constant.getTags(),tagEntityPage.getContent());

		return new ResponseEntity<>(Result.success(map),HttpStatus.OK);
	}


	@RequestMapping(value={"/logout"},method = {RequestMethod.GET,RequestMethod.POST})
	@PassToken
	public String logout(HttpServletRequest request){
		if(request.getAttribute(constant.getHeader()) != null){
			request.removeAttribute(constant.getHeader());
		}
		return "/";
	}

}
