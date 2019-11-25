package com.imtyger.imtygerbed.controller;

import com.imtyger.imtygerbed.common.Result;
import com.imtyger.imtygerbed.bean.bookmark.BookmarkRequest;
import com.imtyger.imtygerbed.bean.bookmark.BookmarkUpdateRequest;
import com.imtyger.imtygerbed.annotation.PassToken;
import com.imtyger.imtygerbed.service.BookmarkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * @Author imtygerx@gmail.com
 * @Date 2019/5/27 14:42
 */

@RestController
@Slf4j
@RequestMapping("/api")
public class BookmarkController {

	@Resource
	private BookmarkService bookmarkService;


	@RequestMapping(value = "/1.0/bookmarks", method = RequestMethod.GET)
	@PassToken
	public Result getBookmarkList(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
								  @RequestParam(required = false, defaultValue = "30")Integer pageSize) {
		return Result.success(bookmarkService.queryBookmarks(pageNum,pageSize));
	}

	@RequestMapping(value = "/1.0/home/bookmarks", method = RequestMethod.GET)
	public Result queryMarks(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
							 @RequestParam(required = false, defaultValue = "30")Integer pageSize) {
		return Result.success(bookmarkService.queryHomeBookmarks(pageNum, pageSize));
	}

	@RequestMapping(value = "/1.0/home/bookmarks/new", method = RequestMethod.POST)
	public Result newMarks(@RequestBody @Valid BookmarkRequest bookmarkRequest, HttpServletRequest request){
		return Result.success(bookmarkService.newBookmark(bookmarkRequest, request));
	}

	@RequestMapping(value = "/1.0/home/bookmarks/delete/{id}", method = RequestMethod.DELETE)
	public Result deleteMark(@PathVariable("id") @NotEmpty String id) {
		return Result.success(bookmarkService.deleteBookmarkById(id));
	}

	@RequestMapping(value = "/1.0/home/bookmarks/update", method = RequestMethod.PUT)
	public Result updateMark(@RequestBody @Valid BookmarkUpdateRequest request){
		return Result.success(bookmarkService.updateBookmark(request));
	}
}
