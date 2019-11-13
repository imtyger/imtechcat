package com.imtyger.imtygerbed.repository;

import com.imtyger.imtygerbed.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {

	//判断用户名是否存在
	UserEntity findUserEntityByUsername(String username);

	UserEntity findUserEntityById(String id);

	//判断手机号是否存在
	boolean findByUserphone(String userphone);

	//判断邮箱是否存在
	boolean findByEmail(String email);


}
