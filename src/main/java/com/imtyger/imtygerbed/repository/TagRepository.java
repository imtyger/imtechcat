package com.imtyger.imtygerbed.repository;

import com.imtyger.imtygerbed.entity.TagEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends MongoRepository<TagEntity,String> {

	//获取指定用户id下tag列表页
	Page<TagEntity> findTagEntitiesByUserId(String userId, Pageable pageable);


	//获取指定tag名称列表
	Page<TagEntity> findTagEntityByTagName(String tagName, Pageable pageable);

	//获取指定tag名称列表
	Page<TagEntity> findTagEntityByUserIdAndTagName(String userId, String tagName, Pageable pageable);

	//获取tag名称模糊查询列表
	List<TagEntity> findByTagNameLike(String tagName);

	Long countTagEntitiesByTagName(String tagName);

	Long countTagEntitiesByUserId(String userId);

	Long countTagEntitiesByUserIdAndTagName(String userId, String tagName);

	TagEntity findTagEntityByTagName(String tagName);

	TagEntity findTagEntityById(String id);

	Page<TagEntity> findTagEntityByTagNameLike(String tagName, Pageable pageable);

	Long countTagEntitiesByTagNameLike(String tagName);



}
