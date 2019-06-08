package com.website.imtechcat.repository;

import com.website.imtechcat.entity.MarkEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarkRepository extends MongoRepository<MarkEntity,String> {

	//获取用户id书签列表
	List<MarkEntity> findMarkEntitiesByUserId(String userId);

	//获取指定书签名称列表
	List<MarkEntity> findMarkEntitiesByUserIdAndMarkName(String userId, String markName);


}
