package com.website.imtechcat.repository;

import com.website.imtechcat.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {

	//判断用户名是否存在
	UserEntity findUserEntityByUsername(String username);

	//判断手机号是否存在
	boolean findByUserphone(String userphone);

	//判断邮箱是否存在
	boolean findByEmail(String email);


}
