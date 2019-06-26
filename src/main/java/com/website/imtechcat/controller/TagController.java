package com.website.imtechcat.controller;

import com.website.imtechcat.common.Constant;
import com.website.imtechcat.common.Result;
import com.website.imtechcat.common.ResultCode;
import com.website.imtechcat.common.annotation.PassToken;
import com.website.imtechcat.entity.TagEntity;
import com.website.imtechcat.entity.UserEntity;
import com.website.imtechcat.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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
	private TagService tagServiceImpl;

	@Resource
	private Constant constant;

	@RequestMapping(value = "/home/tags", method = RequestMethod.GET)
	@PassToken
	public String tags(HttpServletRequest request){
		return "/home/tags";
	}


	@RequestMapping(value = "/api/1.0/alltagsbyuserid", method = RequestMethod.GET)
	public ResponseEntity<Result> findAllTagsByUserId(@RequestBody UserEntity userEntity,Integer pageNum,
															  Integer pageSize){
		String userId = userEntity.getId();
		Long tagsCount = tagServiceImpl.tagsCountByUser(userId);

		Map map = new HashMap();
		map.put(constant.getTagsCount(),tagsCount);

		if(tagsCount == 0){
			return new ResponseEntity<>(Result.fail(constant.getTagsZero(),map), HttpStatus.OK);
		}

		Page<TagEntity> tagEntityPage = tagServiceImpl.findTagEntitiesPageByUserId(userEntity.getId(),pageNum,
				pageSize,null);
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


	@RequestMapping(value = "/api/1.0/alltagsbyuseridandtagname", method = RequestMethod.GET)
	public ResponseEntity<Result> findAllTagsByUserIdAndTagName(@RequestBody UserEntity userEntity,
																String tagName, Integer pageNum,
																Integer pageSize){
		String userId = userEntity.getId();
		Long tagsCount = tagServiceImpl.tagsCountByUserIdAndTagName(userId,tagName);

		Map map = new HashMap();
		map.put(constant.getTagsCount(),tagsCount);

		if(tagsCount == 0){
			return new ResponseEntity<>(Result.fail(constant.getTagsZero(),map), HttpStatus.OK);
		}

		Page<TagEntity> tagEntityPage = tagServiceImpl.findTagEntitiesPageByUserId(userEntity.getId(),pageNum,
				pageSize,null);
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


	@RequestMapping(value = "/api/1.0/newtags",method = RequestMethod.POST)
	public ResponseEntity<Result> newTags(@RequestBody TagEntity tagEntity,@RequestBody UserEntity userEntity){
		tagEntity.setUserId(userEntity.getId());
		TagEntity entity = tagServiceImpl.newTags(tagEntity);

		if(entity == null || entity.getId() == null){
			return new ResponseEntity<>(Result.fail(),HttpStatus.OK);
		}
		return new ResponseEntity<>(Result.success(),HttpStatus.OK);
	}


	@RequestMapping(value = "/api/1.0/updatetags",method = RequestMethod.POST)
	public ResponseEntity<Result> newTags(@RequestBody TagEntity tagEntity){
		tagEntity.setLastUpdatedAt(System.currentTimeMillis());
		TagEntity entity = tagServiceImpl.updateTags(tagEntity);

		if(entity == null || entity.getId() == null){
			return new ResponseEntity<>(Result.fail(),HttpStatus.OK);
		}
		return new ResponseEntity<>(Result.success(),HttpStatus.OK);
	}


	@RequestMapping(value = "/api/1.0/delalltags", method = RequestMethod.DELETE)
	public ResponseEntity<Result> deleteAllTags(HttpServletRequest request){
		try{
			tagServiceImpl.deleteAllTags();
			return new ResponseEntity<>(Result.success(),HttpStatus.OK);
		}catch (Exception ex){
			log.error("del all tags catch an exception=>",ex);
			return new ResponseEntity<>(Result.fail(ex.getMessage()),HttpStatus.OK);
		}
	}


	@RequestMapping(value = "/api/1.0/deltags", method = RequestMethod.DELETE)
	public ResponseEntity<Result> deleteTags(@RequestBody TagEntity tagEntity){
		try{
			boolean b = tagServiceImpl.findTagEntityByIdAndUserIdExists(tagEntity);
			if(false == b){
				return new ResponseEntity<>(Result.fail("Tag不存在"),HttpStatus.OK);
			}
			tagServiceImpl.deleteTags(tagEntity);
			return new ResponseEntity<>(Result.success(),HttpStatus.OK);
		}catch (Exception ex){
			log.error("del all tags catch an exception=>",ex);
			return new ResponseEntity<>(Result.fail(ex.getMessage()),HttpStatus.OK);

		}
	}

}
