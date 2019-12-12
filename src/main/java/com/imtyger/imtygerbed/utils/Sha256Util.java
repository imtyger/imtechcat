package com.imtyger.imtygerbed.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName Sha256Util
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/6/5 15:00
 * @Version 1.0
 **/
@Slf4j
public class Sha256Util {

	/*
	 *@Description //原生java实现SHA256加密
	 *@Param [password]
	 *@return java.lang.String
	 **/
	public static String getSHA256(String password){
		MessageDigest messageDigest;
		String encode = "";
		try{
			messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(password.getBytes("UTF-8"));
			encode = byte2Hex(messageDigest.digest());
		}catch (NoSuchAlgorithmException ex){
			ex.printStackTrace();
		}catch (UnsupportedEncodingException ex){
			ex.printStackTrace();
		}
		return encode;
	}

	/*
	 *@Description //byte 转16进制
	 *@Param [bytes]
	 *@return java.lang.String
	 **/
	private static String byte2Hex(byte[] bytes){
		StringBuffer stringBuffer = new StringBuffer();
		String temp = null;
		for(int i = 0; i < bytes.length; i ++){
			temp = Integer.toHexString(bytes[i] & 0xFF);
			if(temp.length()==1){
				stringBuffer.append("0");
			}
			stringBuffer.append(temp);
		}
		return stringBuffer.toString();
	}

	/**
	 * 验证密码是否一致
	 * @param password
	 * @param savePassword
	 * @return
	 */
	public static boolean validatePassword(String password, String savePassword){
		String encode = getSHA256(password);
		//密码错误
		if(encode == null || !savePassword.trim().equals(encode)){
			return false;
		}
		return true;
	}

}
