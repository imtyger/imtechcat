package com.imtyger.imtygerbed.repository;

import com.imtyger.imtygerbed.entity.BookmarkEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarkRepository extends MongoRepository<BookmarkEntity,String> {

//	BookmarkEntity findMarkEntityById(String id);
//
//	List<BookmarkEntity> findMarkEntitiesByMarkTitleLike(String markTitle);
//
//	BookmarkEntity findByMarkTitle(String markTitle);
//
//	int countByTagsContains(String tagName);
//
//	List<BookmarkEntity> findMarkEntitiesByTagsContains(String tagName);

}
