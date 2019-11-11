package com.website.imtechcat.repository;

import com.website.imtechcat.common.PageUtil;
import com.website.imtechcat.entity.BlogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BlogRepository extends MongoRepository<BlogEntity, String> {

	List<BlogEntity> findByStatusIsTrueAndFlagIsTrueAndBlogContentIgnoreCaseLike(String query);

	List<BlogEntity> findBlogEntitiesByBlogContentIsLike(String query);

	BlogEntity findBlogEntityById(String id);

	BlogEntity findBlogEntityByBlogTitleLike(String blogTitle);

	int countByTagsContains(String tagName);

	int countByTagsContainsAndStatusIsTrue(String tagName);

	long countBlogEntitiesByFlagIsTrue();

	long countBlogEntitiesByBlogTitleIsNotNull();

	long countBlogEntitiesByFlagIsTrueAndStatusIsTrue();

	Page<BlogEntity> findBlogEntitiesByFlagIsTrue(PageUtil pageUtil);

	Page<BlogEntity> findBlogEntitiesByBlogTitleIsNotNull(PageUtil pageUtil);

	Page<BlogEntity> findBlogEntitiesByFlagIsTrueAndStatusIsTrue(PageUtil pageUtil);

	List<BlogEntity> findBlogEntitiesByTagsContainsAndFlagIsTrue(String tagName);
}
