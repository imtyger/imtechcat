package com.website.imtechcat.repository;

import com.website.imtechcat.entity.TagEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends MongoRepository<TagEntity,String> {

	//获取指定用户id下tag列表页
	Page<TagEntity> findTagEntitiesByUserId(String userId, Pageable pageable);

	//获取指定用户id下tag
	boolean findTagEntityByIdAndUserIdExists(String id,String userId);

	//获取指定tag名称列表
	Page<TagEntity> findTagEntityByTagName(String tagName, Pageable pageable);

	//获取指定tag名称列表
	Page<TagEntity> findTagEntityByUserIdAndTagName(String userId, String tagName, Pageable pageable);

	//获取tag名称模糊查询列表
	List<TagEntity> findByTagNameLike(String tagName);


}
