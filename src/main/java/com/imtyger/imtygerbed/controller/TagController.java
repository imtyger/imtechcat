package com.imtyger.imtygerbed.controller;

import com.imtyger.imtygerbed.bean.tag.TagRequest;
import com.imtyger.imtygerbed.bean.tag.TagUpdateRequest;
import com.imtyger.imtygerbed.common.Result;
import com.imtyger.imtygerbed.common.annotation.PassToken;
import com.imtyger.imtygerbed.model.Tag;
import com.imtyger.imtygerbed.service.TagService;
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
	private TagService tagService;


	@RequestMapping(value = "/api/1.0/home/tags", method = RequestMethod.GET)
	public ResponseEntity<Result> findTags(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
										   @RequestParam(required = false,
												   defaultValue = "30") Integer pageSize) {
		Map result = tagService.queryHomeTags(pageNum, pageSize);
		return new ResponseEntity<>(Result.success(result), HttpStatus.OK);
	}


	@RequestMapping(value = "/api/1.0/tags", method = RequestMethod.GET)
	@PassToken
	public ResponseEntity<Result> getTagCloud(ModelMap modelMap) {
		List<Tag> tagList = tagService.getTagCloud();
		modelMap.put("list", tagList);
		return new ResponseEntity<>(Result.success(modelMap), HttpStatus.OK);

	}

	@RequestMapping(value = "/api/1.0/home/tags/defaultname", method = RequestMethod.GET)
	public ResponseEntity<Result> findTagNamesDefault(ModelMap modelMap){
		List<String> tags = tagService.createTagNameList();
		modelMap.put("tags",tags);
		return new ResponseEntity<>(Result.success(modelMap),HttpStatus.OK);
	}

	@RequestMapping(value = "/api/1.0/home/tags/new",method = RequestMethod.POST)
	public ResponseEntity<Result> newTags(@RequestBody @Valid TagRequest tagRequest, HttpServletRequest request){
		Integer count = tagService.newTag(tagRequest,request);
		return new ResponseEntity<>(Result.success(count),HttpStatus.OK);
	}

	@RequestMapping(value = "/api/1.0/home/tags/update",method = RequestMethod.PUT)
	public ResponseEntity<Result> updateTags(@RequestBody TagUpdateRequest tagUpdateRequest){
		Integer count = tagService.updateTag(tagUpdateRequest);
		return new ResponseEntity<>(Result.success(count),HttpStatus.OK);
	}

	@RequestMapping(value = "/api/1.0/home/tags/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Result> deleteTagById(@PathVariable("id") @NotEmpty String id){
		Integer count = tagService.deleteTagById(id);
		return new ResponseEntity<>(Result.success(count),HttpStatus.OK);
	}


//	@RequestMapping(value = "/api/1.0/home/tag/likename", method = RequestMethod.GET)
//	public ResponseEntity<Result> findTagNameLike(HttpServletRequest request,String tagName, ModelMap modelMap){
//		log.info("this.findTagNameLike()==> tagName:" + tagName);
//		if(CheckUtil.isNull(tagName)){
//			return new ResponseEntity<>(Result.fail("传递值null"),HttpStatus.OK);
//		}
//
//		try {
//			List<TagEntity> tagEntities = tagServiceImpl.findByTagNameLike(tagName);
//
//			if(tagEntities != null && tagEntities.size() != 0){
//				List<String> tag = tagEntities.stream().map(tagEntity -> {
//					return tagEntity.getTagName();
//				}).collect(Collectors.toList());
//				modelMap.put("tag",tag);
//			}else{
//				modelMap.put("tag",tagEntities);
//			}
//
//			return new ResponseEntity<>(Result.success(modelMap),HttpStatus.OK);
//		}catch (Exception ex){
//			String msg = ex.getMessage();
//			log.error(" find tag by like name catch exception => " + ex);
//			return new ResponseEntity<>(Result.fail(msg),HttpStatus.OK);
//		}
//	}

}
