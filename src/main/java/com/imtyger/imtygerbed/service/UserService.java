package com.imtyger.imtygerbed.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.imtyger.imtygerbed.bean.user.LoginRequest;
import com.imtyger.imtygerbed.common.Constant;
import com.imtyger.imtygerbed.common.Result;
import com.imtyger.imtygerbed.common.exception.BusinessException;
import com.imtyger.imtygerbed.entity.LoginInfoEntity;
import com.imtyger.imtygerbed.entity.UserEntity;
import com.imtyger.imtygerbed.mapper.LoginInfoMapper;
import com.imtyger.imtygerbed.mapper.UserMapper;
import com.imtyger.imtygerbed.model.User;
import com.imtyger.imtygerbed.utils.IpAddressUtil;
import com.imtyger.imtygerbed.utils.JwtTokenUtil;
import com.imtyger.imtygerbed.utils.Sha256Util;
import lombok.extern.slf4j.Slf4j;
import nl.bitwalker.useragentutils.UserAgent;
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

	/**
	 * 根据用户名和密码登录
	 */
	public Map<String,Object> login(LoginRequest loginRequest, HttpServletRequest request) throws Exception {
		//1. 检查用户名是否存在
		UserEntity userEntity = checkUserExist(loginRequest.getUsername());
		//2. 检查密码是否正确
		checkPassword(loginRequest, userEntity.getPassword());
		//3. 生成此次登录token
		String token = createAndSaveToken(userEntity);
		//4. 此处将用户信息写入login_info表
		saveLoginInfo(request, userEntity);

		Map result = loginResult(userEntity,token);
		return result;
	}

	/**
	 * 检查用户名是否存在
	 * @param username
	 * @return
	 */
	private UserEntity checkUserExist(String username){
		UserEntity userEntity = queryUserByName(username);
		if(userEntity == null){
			throw new BusinessException(Result.userNotFound().getCode(),Result.userNotFound().getMsg());
		}
		return userEntity;
	}

	/**
	 * 检查密码是否正确
	 * @param loginRequest
	 * @param savePassword
	 */
	private void checkPassword(LoginRequest loginRequest, String savePassword){
		Boolean flag = Sha256Util.validatePassword(loginRequest.getPassword(), savePassword);
		if(!flag){
			throw new BusinessException(Result.userNotFound().getCode(),
					"密码错误");
		}
	}

	/**
	 * 创建token
	 * @param userEntity
	 * @return
	 */
	private String createAndSaveToken(UserEntity userEntity){
		String token = jwtTokenUtil.createToken(userEntity);
		if(!StringUtils.isEmpty(token)){
			return token;
		}
		throw new BusinessException(Result.unAuth().getCode(),Result.unAuth().getMsg());
	}

	/**
	 * 登陆返回
	 * @param userEntity
	 * @param token
	 * @return
	 */
	private Map loginResult(UserEntity userEntity, String token){

		Map result = new HashMap(){
			{
				put(constant.getAccessToken(),token);
				put(constant.getRefreshToken(),"");
				put(constant.getExpiresIn(),Long.parseLong(constant.getExpire()) / 1000);
				put(constant.getUserKey(),getUserResult(userEntity));
			}
		};
		return result;
	}

	/**
	 * 解析User返回
	 * @param userEntity
	 * @return
	 */
	private User getUserResult(UserEntity userEntity){
        User user = new User();
        user.setId(userEntity.getId());
        user.setUsername(userEntity.getUsername());
        user.setNickName(userEntity.getNickName());
        user.setAvatar(userEntity.getAvatar());
        return user;
    }


	/**
	 * 验证用户
	 * @param username
	 * @return
	 */
	public User account(String username){
		UserEntity userEntity = queryUserByName(username);
		if(userEntity == null){
			throw new BusinessException(Result.userNotFound().getCode(),
					Result.userNotFound().getMsg());
		}
		User user = getUserResult(userEntity);
		return user;
	}

	/**
	 * 保存登陆信息到login_info
	 * @param request
	 * @param userEntity
	 */
	private void saveLoginInfo(HttpServletRequest request, UserEntity userEntity){
		LoginInfoEntity loginInfoEntity = new LoginInfoEntity();
		loginInfoEntity.setUserId(userEntity.getId());
		loginInfoEntity.setLastLoginIp(IpAddressUtil.getIpAddr(request));
		loginInfoEntity.setUserAgent(getUserAgent(request));
//		loginInfoEntity.setLastLoginAt(new Date());
		loginInfoMapper.insert(loginInfoEntity);
	}

	/**
	 * 获取用户登陆客户端和系统名称
	 * @param request
	 * @return
	 */
	private String getUserAgent(HttpServletRequest request){
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
	 * @param username
	 * @return
	 */
	private UserEntity queryUserByName(String username){
		QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("username",username);
		UserEntity userEntity = userMapper.selectOne(queryWrapper);
		return userEntity;
	}

	/**
	 * 通过用户名获取userId
	 * @param username
	 * @return
	 */
	public Integer queryUserIdByName(String username){
		QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("username",username);
		UserEntity userEntity = userMapper.selectOne(queryWrapper);
		return userEntity.getId();
	}

	/**
	 * 检查userkey
	 * @param request
	 * @return
	 */
	public String checkUserKey(HttpServletRequest request){
		String userKey = (String) request.getAttribute(constant.getUserKey());
		if(StringUtils.isEmpty(userKey)){
			throw new BusinessException(Result.unValid().getCode(),Result.unValid().getMsg());
		}
		return userKey;
	}


}
