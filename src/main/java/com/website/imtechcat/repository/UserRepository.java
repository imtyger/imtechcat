package com.website.imtechcat.repository;

import com.website.imtechcat.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User,String> {

	//判断用户名是否存在
	int findUserByUserName(String userName);

	//判断手机号是否存在
	int findUserByPhoneNum(String phoneNum);

	//判断邮箱是否存在
	int findUserByEmail(String email);

	//通过用户名密码获取账户
	User findUserByUserNameAndUserPwd(String userName,String userPwd);

	//获取用户id
	String findById(User user);

}
