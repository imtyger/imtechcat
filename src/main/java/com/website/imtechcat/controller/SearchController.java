package com.website.imtechcat.controller;

import com.website.imtechcat.common.Constant;
import com.website.imtechcat.common.Result;
import com.website.imtechcat.common.annotation.PassToken;
import com.website.imtechcat.entity.TagEntity;
import com.website.imtechcat.service.TagService;
import com.website.imtechcat.util.CheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SearchController
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/6/20 15:38
 * @Version 1.0
 **/
@Slf4j
@Controller
@RequestMapping("/")
public class SearchController {

	@Resource
	private TagService tagServiceImpl;

	@Resource
	private Constant constant;

	@RequestMapping(value={"/search"},method = RequestMethod.GET)
	@PassToken
	public String search(){
		log.info("================search================");
		return "/search";
	}

	@RequestMapping(value={"/api/1.0/searchtagnamelike"},method = RequestMethod.GET)
	public ResponseEntity<Result> findByTagNameLike(String searchStr){

		if(!CheckUtil.isNull(searchStr)){
			return new ResponseEntity<>(Result.fail(constant.getEmpty()), HttpStatus.OK);
		}

		List<TagEntity> tagEntityList = tagServiceImpl.findByTagNameLike(searchStr.trim());
		Map map = new HashMap();


		if(tagEntityList.size() == 0 || tagEntityList == null){
			map.put(constant.getCount(),"0");
			return new ResponseEntity<>(Result.fail(constant.getEmpty(),map), HttpStatus.OK);
		}

		map.put(constant.getCount(),tagEntityList.size());
		map.put(constant.getList(),tagEntityList);

		return new ResponseEntity<>(Result.success(map),HttpStatus.OK);
	}
}
