package com.website.imtechcat.repository;

import com.website.imtechcat.entity.BlogEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BlogRepository extends MongoRepository<BlogEntity, String> {

	List<BlogEntity> findBlogEntitiesByBlogTitleLike(String blogTitle);

	BlogEntity findBlogEntityById(String id);

	int countByTagsContains(String tagName);

	List<BlogEntity> findBlogEntitiesByTagsContains(String tagName);
}
