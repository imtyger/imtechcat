package com.imtyger.imtygerbed.repository;

import com.imtyger.imtygerbed.entity.BlogEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BlogRepository extends MongoRepository<BlogEntity, String> {

//	List<BlogEntity> findByStatusIsTrueAndFlagIsTrueAndBlogContentIgnoreCaseLike(String query);
//
//	BlogEntity findBlogEntityById(String id);
//
//	BlogEntity findBlogEntityByBlogTitleLike(String blogTitle);
//
//	int countByTagsContainsAndStatusIsTrue(String tagName);
//
//	long countBlogEntitiesByBlogTitleIsNotNull();
//
//	long countBlogEntitiesByFlagIsTrueAndStatusIsTrue();
//
//	Page<BlogEntity> findBlogEntitiesByBlogTitleIsNotNull(PageUtil pageUtil);
//
//	Page<BlogEntity> findBlogEntitiesByFlagIsTrueAndStatusIsTrue(PageUtil pageUtil);
//
//	List<BlogEntity> findBlogEntitiesByTagsContainsAndFlagIsTrue(String tagName);
}
