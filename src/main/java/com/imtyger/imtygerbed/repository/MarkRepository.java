package com.imtyger.imtygerbed.repository;

import com.imtyger.imtygerbed.entity.MarkEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarkRepository extends MongoRepository<MarkEntity,String> {

	//获取用户id书签列表
	List<MarkEntity> findMarkEntitiesByUserId(String userId);

	//获取指定书签名称列表
	List<MarkEntity> findMarkEntitiesByUserIdAndMarkTitle(String userId, String markTitle);

	MarkEntity findMarkEntityById(String id);

	List<MarkEntity> findMarkEntitiesByMarkTitleLike(String markTitle);

	MarkEntity findByMarkTitle(String markTitle);

	List<MarkEntity> findMarkEntitiesByTagsContaining(List<String> tags);

	int countByTagsContains(String tagName);

	List<MarkEntity> findMarkEntitiesByTagsContains(String tagName);

}
