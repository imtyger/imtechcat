package com.website.imtechcat.controller;

import com.website.imtechcat.common.Constant;
import com.website.imtechcat.common.Result;
import com.website.imtechcat.common.annotation.PassToken;
import com.website.imtechcat.entity.TagEntity;
import com.website.imtechcat.service.TagService;
import com.website.imtechcat.util.CheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

	@Resource
	private TagService tagServiceImpl;

	@Autowired
	private Constant constant;

	public String tags(HttpServletRequest request){
		return "/home/tags";
	}

	@RequestMapping(value = "/api/1.0/home/tags", method = RequestMethod.GET)
	public ResponseEntity<Result> findTags(HttpServletRequest request,Integer pageNum, Integer pageSize, ModelMap modelMap){
		log.info("this.findTags()==> pageNum:" + pageNum + ", pageSize:"+ pageSize );
		if(null == pageNum || pageNum <= 0 || CheckUtil.isNull(pageNum+"")){
			pageNum = Integer.parseInt(constant.getNum());
		}
		if(null == pageSize || pageSize <= 0 || CheckUtil.isNull(pageSize+"")){
			pageSize = Integer.parseInt(constant.getSize());
		}
		try {
			Long count = tagServiceImpl.tagsCount();

			modelMap.put(constant.getCount(),count);

			if(count == 0 || pageNum > count){
				modelMap.put(constant.getPageNum(),pageNum);
				modelMap.put(constant.getPageSize(),pageSize);
				modelMap.put(constant.getPageTotal(),0);
				modelMap.put(constant.getList(),"");
				return new ResponseEntity<>(Result.success(modelMap),HttpStatus.OK);
			}

			Page<TagEntity> tags = tagServiceImpl.findAll(pageNum,pageSize,null);

			modelMap.put(constant.getPageNum(),pageNum);
			modelMap.put(constant.getPageSize(),pageSize);
			modelMap.put(constant.getPageTotal(),tags.getTotalPages());
			modelMap.put(constant.getList(),tags.getContent());

			return new ResponseEntity<>(Result.success(modelMap),HttpStatus.OK);
		}catch (Exception ex){
			String msg = ex.getMessage();
			log.error(" find tags catch exception => " + ex);
			return new ResponseEntity<>(Result.fail(msg),HttpStatus.OK);
		}
	}



	@RequestMapping(value = "/api/1.0/home/tags/new",method = RequestMethod.POST)
	public ResponseEntity<Result> newTags(HttpServletRequest request, @RequestBody TagEntity tagEntity){
		if(tagEntity == null){
			return new ResponseEntity<>(Result.unValid("传递值null"), HttpStatus.OK);
		}else if(CheckUtil.isNull(tagEntity.getTagName()) || CheckUtil.isNull(tagEntity.getTagDesc())){
			return new ResponseEntity<>(Result.unValid("参数缺失"),HttpStatus.OK);
		}

		try {
			String userId = (String) request.getAttribute(constant.getUserKey());
			if(CheckUtil.isNull(userId)){
				return new ResponseEntity<>(Result.unAuth("解析userId失败"), HttpStatus.OK);
			}

			boolean b = tagServiceImpl.findTagEntityByTagName(tagEntity.getTagName());
			if(true == b){
				return new ResponseEntity<>(Result.fail("Tag已存在"), HttpStatus.OK);
			}

			tagEntity.setUserId(userId);
			TagEntity entity = tagServiceImpl.newTags(tagEntity);

			if(entity == null || entity.getId() == null){
				return new ResponseEntity<>(Result.fail(),HttpStatus.OK);
			}

			return new ResponseEntity<>(Result.success(entity),HttpStatus.OK);
		}catch (Exception ex){
			String msg = ex.getMessage();
			log.error(" new tag catch exception => " + ex);
			return new ResponseEntity<>(Result.fail(msg),HttpStatus.OK);
		}
	}


	@RequestMapping(value = "/api/1.0/home/tags/update",method = RequestMethod.PUT)
	public ResponseEntity<Result> updateTags(@RequestBody TagEntity tagEntity){
		if(tagEntity == null){
			return new ResponseEntity<>(Result.fail("传递值null"),HttpStatus.OK);
		}else if(CheckUtil.isNull(tagEntity.getId())){
			return new ResponseEntity<>(Result.unValid("参数缺失"),HttpStatus.OK);
		}

		try{
			boolean b = tagServiceImpl.findById(tagEntity);
			if(false == b){
				return new ResponseEntity<>(Result.fail("Tag不存在"),HttpStatus.OK);
			}

			TagEntity entity = tagServiceImpl.updateTags(tagEntity);

			return new ResponseEntity<>(Result.success(entity),HttpStatus.OK);
		}catch (Exception ex){
			String msg = ex.getMessage();
			log.error("update tag catch an exception=>",ex);
			return new ResponseEntity<>(Result.fail(msg),HttpStatus.OK);

		}
	}


	@RequestMapping(value = "/api/1.0/home/tags/delete", method = RequestMethod.DELETE)
	public ResponseEntity<Result> deleteTags(@RequestBody TagEntity tagEntity){
		if(tagEntity == null){
			return new ResponseEntity<>(Result.fail("传递值null"),HttpStatus.OK);
		}else if(CheckUtil.isNull(tagEntity.getId())){
			return new ResponseEntity<>(Result.unValid("参数缺失"),HttpStatus.OK);
		}

		try{
			boolean b = tagServiceImpl.findById(tagEntity);
			if(false == b){
				return new ResponseEntity<>(Result.fail("Tag不存在"),HttpStatus.OK);
			}

			tagServiceImpl.deleteTags(tagEntity);
			return new ResponseEntity<>(Result.success(),HttpStatus.OK);
		}catch (Exception ex){
			log.error("del tag catch an exception=>",ex);
			return new ResponseEntity<>(Result.fail(ex.getMessage()),HttpStatus.OK);

		}
	}

	@RequestMapping(value = "/api/1.0/home/tags/defaultname", method = RequestMethod.GET)
	public ResponseEntity<Result> findTagNamesDefault(HttpServletRequest request, ModelMap modelMap){
		log.info("this.findTagNamesDefault()==> ");

		try {
			List<String> tags = tagServiceImpl.findAllTagNameList();

			modelMap.put("tags",tags);

			return new ResponseEntity<>(Result.success(modelMap),HttpStatus.OK);
		}catch (Exception ex){
			String msg = ex.getMessage();
			log.error(" find tags by like name catch exception => " + ex);
			return new ResponseEntity<>(Result.fail(msg),HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/api/1.0/home/tags/likename", method = RequestMethod.GET)
	public ResponseEntity<Result> findTagNameLike(HttpServletRequest request,String tagName, ModelMap modelMap){
		log.info("this.findTagNameLike()==> tagName:" + tagName);
		if(CheckUtil.isNull(tagName)){
			return new ResponseEntity<>(Result.fail("传递值null"),HttpStatus.OK);
		}

		try {
			List<TagEntity> tagEntities = tagServiceImpl.findByTagNameLike(tagName);

			if(tagEntities != null && tagEntities.size() != 0){
				List<String> tags = tagEntities.stream().map(tagEntity -> {
					return tagEntity.getTagName();
				}).collect(Collectors.toList());
				modelMap.put("tags",tags);
			}else{
				modelMap.put("tags",tagEntities);
			}

			return new ResponseEntity<>(Result.success(modelMap),HttpStatus.OK);
		}catch (Exception ex){
			String msg = ex.getMessage();
			log.error(" find tags by like name catch exception => " + ex);
			return new ResponseEntity<>(Result.fail(msg),HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/api/1.0/tags", method = RequestMethod.GET)
	@PassToken
	public ResponseEntity<Result> getTagCloud(ModelMap modelMap){
		log.info(" this getTagCloud==>");
		try {
			List<Map> cloudList = tagServiceImpl.getTagCloudList();

			modelMap.put("list",cloudList);

			return new ResponseEntity<>(Result.success(modelMap),HttpStatus.OK);
		}catch (Exception ex){
			String msg = ex.getMessage();
			log.error(" get tag cloud list catch exception => " + ex);
			return new ResponseEntity<>(Result.fail(msg),HttpStatus.OK);
		}
	}




}
