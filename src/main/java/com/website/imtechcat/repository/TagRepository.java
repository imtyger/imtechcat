package com.website.imtechcat.repository;

import com.website.imtechcat.entity.Tag;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends MongoRepository<Tag,String> {

	//获取全部标签名称
	List<Tag> findTagsByTagNameIsNotNull();


}
