package com.website.imtechcat.controller;

import com.website.imtechcat.common.Constant;
import com.website.imtechcat.common.Result;
import com.website.imtechcat.common.annotation.PassToken;
import com.website.imtechcat.entity.MarkEntity;
import com.website.imtechcat.entity.TagEntity;
import com.website.imtechcat.model.Bookmark;
import com.website.imtechcat.service.MarkService;
import com.website.imtechcat.service.TagService;
import com.website.imtechcat.util.CheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @ClassName MarkController
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/5/27 14:42
 * @Version 1.0
 **/
@Slf4j
@Controller
@RequestMapping("/")
public class MarkController {

	@Resource
	private MarkService markServiceImpl;

	@Resource
	private TagService tagServiceImpl;

	@Resource
	private Constant constant;

	public String marks(HttpServletRequest request){
		return "/home/bookmarks";
	}


	@RequestMapping(value = "/api/1.0/home/bookmarks", method = RequestMethod.GET)
	public ResponseEntity<Result> queryMarks(HttpServletRequest request,Integer pageNum, Integer pageSize){
		log.info("this.queryMarks()==>" + " pageNum:"+ pageNum+ ", pageSize:"+pageSize);
		if(null == pageNum || pageNum <= 0 || CheckUtil.isNull(pageNum+"")){
			pageNum = Integer.parseInt(constant.getNum());
		}
		if(null == pageSize || pageSize <= 0 || CheckUtil.isNull(pageSize+"")){
			pageSize = Integer.parseInt(constant.getSize());
		}

		try{
			Long count = markServiceImpl.marksCount();

			Map map = new HashMap();
			map.put(constant.getCount(),count);

			if(count == 0 || pageNum > count){
				map.put(constant.getPageNum(),pageNum);
				map.put(constant.getPageSize(),pageSize);
				map.put(constant.getPageTotal(),0);
				map.put(constant.getList(),"");
				return new ResponseEntity<>(Result.success(map),HttpStatus.OK);
			}

			Page<MarkEntity> marks = markServiceImpl.findMarks(pageNum, pageSize, null);

			map.put(constant.getPageNum(),pageNum);
			map.put(constant.getPageSize(),pageSize);
			map.put(constant.getPageTotal(),marks.getTotalPages());
			map.put(constant.getList(),marks.getContent());

			return new ResponseEntity<>(Result.success(map),HttpStatus.OK);
		}catch (Exception ex){
			String msg = ex.getMessage();
			log.error(" query marks catch exception => " + ex);
			return new ResponseEntity<>(Result.fail(msg),HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/api/1.0/home/bookmarks/new",method = RequestMethod.POST)
	public ResponseEntity<Result> newMarks(@RequestBody MarkEntity markEntity, HttpServletRequest request){
		if(markEntity == null){
			return new ResponseEntity<>(Result.fail("传递值null"), HttpStatus.OK);
		}else if(CheckUtil.isNull(markEntity.getMarkTitle()) || CheckUtil.isNull(markEntity.getMarkLink())){
			return new ResponseEntity<>(Result.unValid("参数缺失"), HttpStatus.OK);
		}

		try{
			String userId = (String) request.getAttribute(constant.getUserKey());
			if(CheckUtil.isNull(userId)){
				return new ResponseEntity<>(Result.unAuth("解析userId失败"), HttpStatus.OK);
			}
			markEntity.setUserId(userId);

			MarkEntity entity = markServiceImpl.newMarks(markEntity);
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
			log.warn(" new bookmark catch exception => " + ex);
			return new ResponseEntity<>(Result.fail(msg), HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/api/1.0/home/bookmarks/delete", method = RequestMethod.DELETE)
	public ResponseEntity<Result> deleteMark(@RequestBody MarkEntity markEntity){
		if(markEntity == null ){
			return new ResponseEntity<>(Result.fail("传递值null"),HttpStatus.OK);
		}else if(CheckUtil.isNull(markEntity.getId())) {
			return new ResponseEntity<>(Result.unValid("参数缺失"),HttpStatus.OK);
		}

		try{
			boolean b = markServiceImpl.findMarkEntityById(markEntity);
			if(false == b){
				return new ResponseEntity<>(Result.fail("Mark不存在"),HttpStatus.OK);
			}

			List<String> tags = markEntity.getTags();
			markServiceImpl.deleteMark(markEntity);

			if(tags != null && tags.size() != 0){
				tagServiceImpl.updateTagCount(tags,-1);
			}

			return new ResponseEntity<>(Result.success(),HttpStatus.OK);
		}catch (Exception ex){
			String msg = ex.getMessage();
			log.error("del mark catch an exception=>",ex);
			return new ResponseEntity<>(Result.fail(msg),HttpStatus.OK);

		}
	}

	@RequestMapping(value = "/api/1.0/home/bookmarks/update", method = RequestMethod.PUT)
	public ResponseEntity<Result> updateMark(@RequestBody MarkEntity markEntity){
		if(markEntity == null ){
			return new ResponseEntity<>(Result.fail("传递值null"),HttpStatus.OK);
		}else if(CheckUtil.isNull(markEntity.getId())){
			return new ResponseEntity<>(Result.unValid("参数缺失"),HttpStatus.OK);
		}

		try{
			boolean b = markServiceImpl.findMarkEntityById(markEntity);
			if(false == b){
				return new ResponseEntity<>(Result.fail("Mark不存在"),HttpStatus.OK);
			}


			MarkEntity mark = markServiceImpl.updateMark(markEntity);
			List<String> tags = mark.getTags();
			if(tags != null && tags.size() != 0){
				tagServiceImpl.updateTagCount(tags,1);
			}

			return new ResponseEntity<>(Result.success(mark),HttpStatus.OK);
		}catch (Exception ex){
			String msg = ex.getMessage();
			log.error("update mark catch exception=>",ex);
			return new ResponseEntity<>(Result.fail(msg),HttpStatus.OK);

		}
	}

	@RequestMapping(value = "/api/1.0/bookmarks/bytagname", method = RequestMethod.GET)
	@PassToken
	public ResponseEntity<Result> getMarkListByTagName(String tagName){
		log.info("this getMarkListByTagName==> tagName : " + tagName);
		if(CheckUtil.isNull(tagName)){
			return new ResponseEntity<>(Result.fail("传递值null"),HttpStatus.OK);
		}

		try {
			List<MarkEntity> markEntityList = markServiceImpl.findMarkEntitiesByTagName(tagName);

			if(markEntityList == null || markEntityList.size() == 0){
				return new ResponseEntity<>(Result.success(markEntityList),HttpStatus.OK);
			}

			List<Map> markList = new ArrayList<>();
			for(MarkEntity markEntity : markEntityList){
				Map map = new HashMap();
				map.put("markTitle",markEntity.getMarkTitle());
				map.put("markLink",markEntity.getMarkLink());
				markList.add(map);
			}

			return new ResponseEntity<>(Result.success(markList),HttpStatus.OK);
		}catch (Exception ex){
			String msg = ex.getMessage();
			log.error("get mark list by tag name catch exception=>",ex);
			return new ResponseEntity<>(Result.fail(msg),HttpStatus.OK);

		}

	}


	@RequestMapping(value = "/api/1.0/bookmarks", method = RequestMethod.GET)
	@PassToken
	public ResponseEntity<Result> getBookmarkList(HttpServletRequest request,Integer pageNum, Integer pageSize){
		log.info("this.getBookmarkList()==>" + " pageNum:"+ pageNum+ ", pageSize:"+pageSize);
		if(null == pageNum || pageNum <= 0 || CheckUtil.isNull(pageNum+"")){
			pageNum = Integer.parseInt(constant.getNum());
		}
		if(null == pageSize || pageSize <= 0 || pageSize > 300 || CheckUtil.isNull(pageSize+"")){
			pageSize = 30;
		}
		Map resultMap = new HashMap();

		try{
			Long count = markServiceImpl.marksCount();
			resultMap.put(constant.getCount(),count);

			if(count == 0){
				resultMap.put(constant.getPageNum(),pageNum);
				resultMap.put(constant.getPageSize(),pageSize);
				resultMap.put(constant.getPageTotal(),0);
				resultMap.put(constant.getList(),"");
				return new ResponseEntity<>(Result.success(resultMap),HttpStatus.OK);
			}

			Page<MarkEntity> marks = markServiceImpl.findMarks(pageNum, pageSize, null);
			List<MarkEntity> list = marks.getContent();
			List<Bookmark> bookmarks = new ArrayList<>();
			for(MarkEntity entity : list){
				Bookmark bookmark = new Bookmark();
				bookmark.setId(entity.getId());
				bookmark.setMarkTitle(entity.getMarkTitle());
				bookmark.setMarkDesc(entity.getMarkDesc());
				bookmark.setMarkLink(entity.getMarkLink());
				bookmark.setTags(entity.getTags());
				bookmark.setCreatedAt(entity.getCreatedAt());
				bookmarks.add(bookmark);
			}

			resultMap.put(constant.getPageNum(),pageNum);
			resultMap.put(constant.getPageSize(),pageSize);
			resultMap.put(constant.getPageTotal(),marks.getTotalPages());
			resultMap.put(constant.getList(),bookmarks);

			return new ResponseEntity<>(Result.success(resultMap),HttpStatus.OK);
		}catch (Exception ex){
			String msg = ex.getMessage();
			log.error(" get bookmark list catch exception => " + ex);
			return new ResponseEntity<>(Result.fail(msg),HttpStatus.OK);
		}
	}


}
