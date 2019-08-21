package com.website.imtechcat.repository;

import com.website.imtechcat.common.PageUtil;
import com.website.imtechcat.entity.BlogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BlogRepository extends MongoRepository<BlogEntity, String> {

	List<BlogEntity> findBlogEntitiesByBlogTitleLike(String blogTitle);

	BlogEntity findBlogEntityById(String id);

	int countByTagsContains(String tagName);

	long countBlogEntitiesByFlagIsTrue();

	Page<BlogEntity> findBlogEntitiesByFlagIsTrue(PageUtil pageUtil);

	Page<BlogEntity> findBlogEntitiesByFlagIsTrueAndStatusIsTrue(PageUtil pageUtil);

	List<BlogEntity> findBlogEntitiesByTagsContainsAndFlagIsTrue(String tagName);
}
