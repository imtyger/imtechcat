package com.imtyger.imtygerbed.repository;

import com.imtyger.imtygerbed.entity.TagEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends MongoRepository<TagEntity,String> {

//	//获取指定tag名称列表
//	Page<TagEntity> findTagEntityByTagName(String tagName, Pageable pageable);
//
//	//获取tag名称模糊查询列表
//	List<TagEntity> findByTagNameLike(String tagName);
//
//	Long countTagEntitiesByTagName(String tagName);
//
//	TagEntity findTagEntityByTagName(String tagName);
//
//	TagEntity findTagEntityById(String id);
//
//	Page<TagEntity> findTagEntityByTagNameLike(String tagName, Pageable pageable);
//
//	Long countTagEntitiesByTagNameLike(String tagName);



}
