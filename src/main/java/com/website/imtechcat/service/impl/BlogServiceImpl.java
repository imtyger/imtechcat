package com.website.imtechcat.service.impl;

import com.website.imtechcat.common.PageUtil;
import com.website.imtechcat.common.annotation.PassToken;
import com.website.imtechcat.entity.BlogEntity;
import com.website.imtechcat.model.Blog;
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
		blogEntity.setFlag(true);
		blogEntity.setStatus(false);
		return blogRepository.insert(blogEntity);
	}

	@Override
	public BlogEntity deleteBlog(BlogEntity blogEntity) {
		blogEntity.setFlag(false);
		blogEntity.setStatus(false);
		return blogRepository.save(blogEntity);
	}

	@Override
	public BlogEntity updateBlog(BlogEntity blogEntity) {
		blogEntity.setLastUpdatedAt(new Date());
		return blogRepository.save(blogEntity);
	}


	@Override
	public BlogEntity updateBlogVisitCount(BlogEntity blogEntity) {
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
	public boolean findById(String id) {
		BlogEntity entity = blogRepository.findBlogEntityById(id);
		if(entity != null){
			return true;
		}
		return false;
	}

//	@Override
//	public Long blogCount() {
//		return blogRepository.countBlogEntitiesByFlagIsTrue();
//	}

	@Override
	public Long homeBlogCount() {
		return blogRepository.countBlogEntitiesByBlogTitleIsNotNull();
	}

	@Override
	public Long showBlogCount() {
		return blogRepository.countBlogEntitiesByFlagIsTrueAndStatusIsTrue();
	}

	@Override
	public Page<BlogEntity> findHomeBlogList(Integer pageNum, Integer pageSize, Sort sort) {
		PageUtil pageUtil = PageUtil.newPage(pageNum,pageSize,sort);

		Page<BlogEntity> blogEntities = blogRepository.findBlogEntitiesByBlogTitleIsNotNull(pageUtil);
		return blogEntities;
	}

	@Override
	public Page<BlogEntity> findBlogList(Integer pageNum, Integer pageSize, Sort sort) {
		PageUtil pageUtil = PageUtil.newPage(pageNum,pageSize,sort);

		Page<BlogEntity> blogEntities = blogRepository.findBlogEntitiesByFlagIsTrueAndStatusIsTrue(pageUtil);
		return blogEntities;
	}

	@Override
	public List<BlogEntity> findBlogEntitiesByTagName(String tagName) {
		return blogRepository.findBlogEntitiesByTagsContainsAndFlagIsTrue(tagName);
	}

	@Override
	public BlogEntity findBlogEntityById(String id) {
		return blogRepository.findBlogEntityById(id);
	}

    @Override
    public BlogEntity findBlogEntityByBlogTitleLike(String blogTitle) {
        return blogRepository.findBlogEntityByBlogTitleLike(blogTitle);
    }

    @Override
	public List<BlogEntity> findBlogEntitiesByQuery(String query) {

		try{
			List<BlogEntity> blogEntities =
					blogRepository.findByStatusIsTrueAndFlagIsTrueAndBlogContentIgnoreCaseLike(query);
			return blogEntities;
		}catch (Exception ex){
			ex.printStackTrace();
			return null;
		}

	}


}
