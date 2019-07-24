package com.website.imtechcat.util;

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


	public static void main(String[] args){
		String pwd = "MaXiaoHu~2019";
		System.out.println(getSHA256(pwd));
//1040019d258fc3f7976dd5015a5a1e3d6c4569cda63b5b8c169ff50e6508c91e
	}
}
