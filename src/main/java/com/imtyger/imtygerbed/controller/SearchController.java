package com.imtyger.imtygerbed.controller;

import com.imtyger.imtygerbed.common.Result;
import com.imtyger.imtygerbed.annotation.PassToken;
import com.imtyger.imtygerbed.model.Blog;
import com.imtyger.imtygerbed.service.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author imtygerx@gmail.com
 * @Date 2019/6/20 15:38
 */

@RestController
@Slf4j
@RequestMapping("/api")
public class SearchController {

	@Resource
	private BlogService blogService;

	@RequestMapping(value={"/1.0/search"},method = RequestMethod.GET)
	@PassToken
	public Result searchBlogList(@RequestParam(value="q", required=false)String query,
								 ModelMap modelMap) {
		List<Blog> blogList = new ArrayList<>();
		query = query.trim();
		if(!StringUtils.isEmpty(query)){
			blogList = blogService.searchLike(query);
		}
		modelMap.put("list", blogList);
		return Result.success(modelMap);
	}
}
