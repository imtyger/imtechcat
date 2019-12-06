package com.imtyger.imtygerbed.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.imtyger.imtygerbed.bean.user.LoginRequest;
import com.imtyger.imtygerbed.Constant;
import com.imtyger.imtygerbed.common.Result;
import com.imtyger.imtygerbed.entity.LoginInfoEntity;
import com.imtyger.imtygerbed.entity.UserEntity;
import com.imtyger.imtygerbed.exception.BusinessException;
import com.imtyger.imtygerbed.mapper.LoginInfoMapper;
import com.imtyger.imtygerbed.mapper.UserMapper;
import com.imtyger.imtygerbed.model.User;
import com.imtyger.imtygerbed.utils.IpAddressUtil;
import com.imtyger.imtygerbed.utils.JwtTokenUtil;
import com.imtyger.imtygerbed.utils.RedisUtil;
import com.imtyger.imtygerbed.utils.Sha256Util;
import lombok.extern.slf4j.Slf4j;
import nl.bitwalker.useragentutils.UserAgent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserService {

	@Resource
	private UserMapper userMapper;

	@Resource
	private LoginInfoMapper loginInfoMapper;

	@Resource
	private JwtTokenUtil jwtTokenUtil;

	@Resource
	private Constant constant;

	@Resource
	private RedisUtil redisUtil;

	/**
	 * 根据用户名和密码登录
	 */
	public Map<String,Object> login(@NotNull LoginRequest loginRequest, HttpServletRequest request) {
		//1. 检查用户名是否存在
		UserEntity userEntity = checkUserExist(loginRequest.getUsername());
		//2. 检查密码是否正确
		checkPassword(loginRequest, userEntity.getPassword());
		//3. 生成此次登录token
		String token = createAndSaveToken(userEntity);
		//4. 此处将用户信息写入login_info表
		saveLoginInfo(request, userEntity);

		return loginResult(userEntity, token);
	}

	/**
	 * 检查用户名是否存在
	 */
	private UserEntity checkUserExist(String username){
		UserEntity userEntity = queryUserByName(username);
		if(userEntity == null){
			throw new BusinessException(Result.FAIL.getValue(), "用户没找到");
		}
		return userEntity;
	}

	/**
	 * 检查密码是否正确
	 */
	private void checkPassword(@NotNull LoginRequest loginRequest, String savePassword){
		String cacheKey = "login:username:" + loginRequest.getUsername();
		String cacheValue = redisUtil.get(cacheKey);
		boolean login = checkPwdFailCache(cacheValue);
		if(!login){
			throw new BusinessException(Result.FAIL.getValue(), "当前用户密码已输错五次，禁止登陆");
		}

		boolean flag = Sha256Util.validatePassword(loginRequest.getPassword(), savePassword);
		if(!flag) {
			savePwdFailCache(cacheKey);
			throw new BusinessException(Result.FAIL.getValue(), "密码错误");
		}
		redisUtil.delete(cacheKey);
	}

	private void savePwdFailCache(String cacheKey){
		long incr = redisUtil.incr(cacheKey);
		if(incr == 1){
			redisUtil.expire(cacheKey, 24 * 60 * 60);
		}
	}

	private boolean checkPwdFailCache(String cacheValue){
		if(cacheValue != null && Integer.parseInt(cacheValue) == 5){
			return false;
		}
		return true;
	}

	/**
	 * 创建token
	 */
	private String createAndSaveToken(UserEntity userEntity){
		String token = jwtTokenUtil.createToken(userEntity);
		if(!StringUtils.isEmpty(token)){
			return token;
		}
		throw new BusinessException(Result.FAIL.getValue(), "创建token失败");
	}

	/**
	 * 登陆返回
	 */
	private Map<String, Object> loginResult(UserEntity userEntity, String token){
		Map<String, Object> result = new HashMap<>();
		result.put(constant.getAccessToken(),token);
		result.put(constant.getRefreshToken(),"");
		result.put(constant.getExpiresIn(),Long.parseLong(constant.getExpire()) / 1000);
		result.put(constant.getUserKey(),getUserResult(userEntity));
		return result;
	}

	/**
	 * 解析User返回
	 */
	private User getUserResult(@NotNull UserEntity userEntity){
        User user = new User();
        user.setId(userEntity.getId());
        user.setUsername(userEntity.getUsername());
        user.setNickName(userEntity.getNickName());
        user.setAvatar(userEntity.getAvatar());
        return user;
    }


	/**
	 * 验证用户
	 */
	public User account(String username){
		UserEntity userEntity = queryUserByName(username);
		if(userEntity == null) {
			throw new BusinessException(Result.FAIL.getValue(), "用户没找到");
		}
		return getUserResult(userEntity);
	}

	/**
	 * 保存登陆信息到login_info
	 */
	private void saveLoginInfo(HttpServletRequest request, @NotNull UserEntity userEntity){
		LoginInfoEntity loginInfoEntity = new LoginInfoEntity();
		loginInfoEntity.setUserId(userEntity.getId());
		loginInfoEntity.setLastLoginIp(IpAddressUtil.getIpAddr(request));
		loginInfoEntity.setUserAgent(getUserAgent(request));
//		loginInfoEntity.setLastLoginAt(new Date());
		loginInfoMapper.insert(loginInfoEntity);
	}

	/**
	 * 获取用户登陆客户端和系统名称
	 */
	private String getUserAgent(@NotNull HttpServletRequest request){
		//获取浏览器信息转换成UA对象
		UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
		//获取浏览器名称
		String browserName = userAgent.getBrowser().getName();
		//获取系统名称
		String system = userAgent.getOperatingSystem().getName();

		return browserName + " " + system;
	}

	/**
	 * 通过用户名获取user对象
	 */
	private UserEntity queryUserByName(String username){
		QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("username",username);

		return userMapper.selectOne(queryWrapper);
	}

	/**
	 * 通过用户名获取userId
	 */
	public int queryUserIdByName(String username){
		QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("username",username);

		UserEntity userEntity = userMapper.selectOne(queryWrapper);
		if(userEntity == null){
			throw new BusinessException(Result.FAIL.getValue(), "未获取到用户");
		}
		return userEntity.getId();
	}

	/**
	 * 检查userkey
	 */
	public String checkUserKey(@NotNull HttpServletRequest request){
		String userKey = (String) request.getAttribute(constant.getUserKey());
		if(StringUtils.isEmpty(userKey)){
			throw new BusinessException(Result.FAIL.getValue(), "用户key为空");
		}
		return userKey;
	}


}
