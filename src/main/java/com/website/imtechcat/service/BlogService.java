package com.website.imtechcat.service;

import com.website.imtechcat.entity.BlogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface BlogService {

	BlogEntity newBlog(BlogEntity blogEntity);

	BlogEntity deleteBlog(BlogEntity blogEntity);

	BlogEntity updateBlog(BlogEntity blogEntity);

	BlogEntity updateBlogVisitCount(BlogEntity blogEntity);

	boolean findById(BlogEntity blogEntity);

	boolean findById(String id);

//	Long blogCount();

	Long homeBlogCount();

	Long showBlogCount();

	Page<BlogEntity> findBlogList(Integer pageNum, Integer pageSize, Sort sort);

	Page<BlogEntity> findHomeBlogList(Integer pageNum, Integer pageSize, Sort sort);

	List<BlogEntity> findBlogEntitiesByTagName(String tagName);

	BlogEntity findBlogEntityById(String id);

	BlogEntity findBlogEntityByBlogTitleLike(String blogTitle);

	List<BlogEntity> findBlogEntitiesByQuery(String query);

}
