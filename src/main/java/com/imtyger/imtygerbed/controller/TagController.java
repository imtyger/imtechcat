package com.imtyger.imtygerbed.controller;

import com.imtyger.imtygerbed.common.Result;
import com.imtyger.imtygerbed.bean.tag.TagRequest;
import com.imtyger.imtygerbed.bean.tag.TagUpdateRequest;
import com.imtyger.imtygerbed.annotation.PassToken;
import com.imtyger.imtygerbed.model.Tag;
import com.imtyger.imtygerbed.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author imtygerx@gmail.com
 * @Date 2019/5/27 14:42
 */

@RestController
@Slf4j
public class TagController {

	@Resource
	private TagService tagService;


	@RequestMapping(value = "/api/1.0/home/tags", method = RequestMethod.GET)
	public Result findTags(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
										   @RequestParam(required = false,
												   defaultValue = "30") Integer pageSize) {
		return Result.success(tagService.queryHomeTags(pageNum, pageSize));
	}

	@RequestMapping(value = "/api/1.0/tags", method = RequestMethod.GET)
	@PassToken
	public Result getTagCloud(ModelMap modelMap) {
		List<Tag> tagList = tagService.getTagCloud();
		modelMap.put("list", tagList);
		return Result.success(modelMap);
	}

	@RequestMapping(value = "/api/1.0/home/tags/defaultname", method = RequestMethod.GET)
	public Result findTagNamesDefault(ModelMap modelMap){
		List<String> tags = tagService.createTagNameList();
		modelMap.put("tags",tags);
		return Result.success(modelMap);
	}

	@RequestMapping(value = "/api/1.0/home/tags/new",method = RequestMethod.POST)
	public Result newTags(@RequestBody @Valid TagRequest tagRequest, HttpServletRequest request){
		Integer count = tagService.newTag(tagRequest,request);
		return Result.success(count);
	}

	@RequestMapping(value = "/api/1.0/home/tags/update",method = RequestMethod.PUT)
	public Result updateTags(@RequestBody TagUpdateRequest tagUpdateRequest){
		Integer count = tagService.updateTag(tagUpdateRequest);
		return Result.success(count);
	}

	@RequestMapping(value = "/api/1.0/home/tags/delete/{id}", method = RequestMethod.DELETE)
	public Result deleteTagById(@PathVariable("id") @NotEmpty String id){
		Integer count = tagService.deleteTagById(id);
		return Result.success(count);
	}
}
