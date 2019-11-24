package com.imtyger.imtygerbed.utils;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName CheckUtil
 * @Description TODO
 * @Author Lenovo
 * @Date 2019/7/3 15:37
 * @Version 1.0
 **/
@Slf4j
public class CheckUtil {

	public static final String DEFAULT_REGEX = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";

	public static boolean isNull(String checkStr){
		if(checkStr == null || StringUtils.isEmpty(checkStr) || checkStr.trim() == "null"){
			return true;
		}
		return false;
	}

	/*
	 *@Description //是否包含空格
	 *@Param []
	 *@return boolean
	 **/
	public static boolean isSpaces(String checkStr){
		if(checkStr.indexOf(" ") != -1){
			return true;
		}
		return false;
	}

	/*
	 *@Description //是否包含特殊字符
	 *@Param [checkStr]
	 *@return boolean
	 **/
	public static boolean isSpecial(String checkStr){
		Pattern pattern = Pattern.compile(getRegex());
		Matcher matcher = pattern.matcher(checkStr);
		return matcher.find();
	}

	/*
	 *@Description //是否以特殊字符开头
	 *@Param [checkStr]
	 *@return boolean
	 **/
	public static boolean isSpecialSymbols(String checkStr){
		if(StringUtils.isEmpty(checkStr)){
			return false;
		}

		Pattern pattern = Pattern.compile(getRegex());
		Matcher matcher = pattern.matcher(checkStr);

		char[] specialSymbols = getRegex().toCharArray();

		boolean isStartWithSpecialSymbols = false;
		for(int i = 0; i < specialSymbols.length; i++){
			char c = specialSymbols[i];
			if(checkStr.indexOf(c) == 0){
				isStartWithSpecialSymbols = true;
				break;
			}

		}
		return matcher.find() && isStartWithSpecialSymbols;
	}

	public static int getCharNum(String checkStr){
		return checkStr.length();
	}


	protected static String getRegex(){
		return DEFAULT_REGEX;
	}


	public static String getHtmlBody(String html){
		Document document = Jsoup.parse(html);
		return document.text();
	}

}
