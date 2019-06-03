package com.website.imtechcat.repository;

import com.website.imtechcat.entity.TagEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends MongoRepository<TagEntity,String> {

	//获取指定用户id下tag列表
	List<TagEntity> findTagEntitiesByUserId(String userId);

	//获取全部tags
	List<TagEntity> findDistinctByTagName();




}
