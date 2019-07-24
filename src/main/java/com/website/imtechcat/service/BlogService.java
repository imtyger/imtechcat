package com.website.imtechcat.service;

import com.website.imtechcat.entity.BlogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface BlogService {

	BlogEntity newBlog(BlogEntity blogEntity);

	void deleteBlog(BlogEntity blogEntity);

	BlogEntity updateBlog(BlogEntity blogEntity);

	boolean findById(BlogEntity blogEntity);

	Long blogCount();

	Page<BlogEntity> findBlogList(Integer pageNum, Integer pageSize, Sort sort);

	List<BlogEntity> findBlogEntitiesByTagName(String tagName);

	BlogEntity findBlogEntityById(String id);

}
