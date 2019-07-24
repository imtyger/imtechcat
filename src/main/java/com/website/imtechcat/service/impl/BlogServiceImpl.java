package com.website.imtechcat.service.impl;

import com.website.imtechcat.common.PageUtil;
import com.website.imtechcat.entity.BlogEntity;
import com.website.imtechcat.repository.BlogRepository;
import com.website.imtechcat.service.BlogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @ClassName BlogServiceImpl
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/7/8 10:58
 * @Version 1.0
 **/
@Service
public class BlogServiceImpl implements BlogService {

	@Resource
	private BlogRepository blogRepository;


	@Override
	public BlogEntity newBlog(BlogEntity blogEntity) {
		blogEntity.setCreatedAt(new Date());
		blogEntity.setLastUpdatedAt(new Date());
		return blogRepository.insert(blogEntity);
	}

	@Override
	public void deleteBlog(BlogEntity blogEntity) {
		blogRepository.delete(blogEntity);
	}

	@Override
	public BlogEntity updateBlog(BlogEntity blogEntity) {
		blogEntity.setLastUpdatedAt(new Date());
		return blogRepository.save(blogEntity);
	}

	@Override
	public boolean findById(BlogEntity blogEntity) {
		BlogEntity entity = blogRepository.findBlogEntityById(blogEntity.getId());
		if(entity != null){
			return true;
		}
		return false;
	}

	@Override
	public Long blogCount() {
		return blogRepository.count();
	}

	@Override
	public Page<BlogEntity> findBlogList(Integer pageNum, Integer pageSize, Sort sort) {
		PageUtil pageUtil = PageUtil.newPage(pageNum,pageSize,sort);

		Page<BlogEntity> blogEntities = blogRepository.findAll(pageUtil);
		return blogEntities;
	}

	@Override
	public List<BlogEntity> findBlogEntitiesByTagName(String tagName) {
		return blogRepository.findBlogEntitiesByTagsContains(tagName);
	}

	@Override
	public BlogEntity findBlogEntityById(String id) {
		return blogRepository.findBlogEntityById(id);
	}

}
