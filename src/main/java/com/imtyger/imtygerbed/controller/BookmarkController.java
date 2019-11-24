package com.imtyger.imtygerbed.controller;

import com.imtyger.imtygerbed.bean.bookmark.BookmarkRequest;
import com.imtyger.imtygerbed.bean.bookmark.BookmarkUpdateRequest;
import com.imtyger.imtygerbed.common.Result;
import com.imtyger.imtygerbed.common.annotation.PassToken;
import com.imtyger.imtygerbed.service.BookmarkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Map;

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
public class BookmarkController {

	@Resource
	private BookmarkService bookmarkService;


	@RequestMapping(value = "/api/1.0/bookmarks", method = RequestMethod.GET)
	@PassToken
	public ResponseEntity<Result> getBookmarkList(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
												  @RequestParam(required = false,
														  defaultValue = "30")Integer pageSize) {
		Map result = bookmarkService.queryBookmarks(pageNum,pageSize);
		return new ResponseEntity<>(Result.success(result), HttpStatus.OK);
	}

	@RequestMapping(value = "/api/1.0/home/bookmarks", method = RequestMethod.GET)
	public ResponseEntity<Result> queryMarks(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
											 @RequestParam(required = false,
													 defaultValue = "30")Integer pageSize) {
		Map result = bookmarkService.queryHomeBookmarks(pageNum,pageSize);
		return new ResponseEntity<>(Result.success(result), HttpStatus.OK);
	}

	@RequestMapping(value = "/api/1.0/home/bookmarks/new",method = RequestMethod.POST)
	public ResponseEntity<Result> newMarks(@RequestBody @Valid BookmarkRequest bookmarkRequest, HttpServletRequest request){
		Integer count = bookmarkService.newBookmark(bookmarkRequest, request);
		return new ResponseEntity<>(Result.success(count), HttpStatus.OK);
	}

	@RequestMapping(value = "/api/1.0/home/bookmarks/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Result> deleteMark(@PathVariable("id") @NotEmpty String id) {
		Integer count = bookmarkService.deleteBookmarkById(id);
		return new ResponseEntity<>(Result.success(count),HttpStatus.OK);
	}

	@RequestMapping(value = "/api/1.0/home/bookmarks/update", method = RequestMethod.PUT)
	public ResponseEntity<Result> updateMark(@RequestBody @Valid BookmarkUpdateRequest request){
		Integer count = bookmarkService.updateBookmark(request);
		return new ResponseEntity<>(Result.success(count),HttpStatus.OK);
	}


}
