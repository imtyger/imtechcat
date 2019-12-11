package com.imtyger.imtygerbed.utils;

import com.imtyger.imtygerbed.Constant;
import lombok.extern.slf4j.Slf4j;
import org.hashids.Hashids;

/**
 * @Author: imtygerx@gmail.com
 * @Date: 2019/12/11
 */
@Slf4j
public class HashidsUtil {

    private static String SALT;
    private static String ALPHABET;
    private static Integer LENGTH;

    public static void setConfigInfo(Constant constant){
        HashidsUtil.ALPHABET = constant.getAlphabet();
        HashidsUtil.SALT = constant.getSalt();
        HashidsUtil.LENGTH = Integer.valueOf(constant.getLength());
    }

    public static String encode(int id){
        Hashids hashids = new Hashids(SALT, LENGTH, ALPHABET);
        return hashids.encode(id);
    }

    public static int decode(String id){
        try{
            Hashids hashids = new Hashids(SALT, LENGTH, ALPHABET);
            return (int) hashids.decode((id))[0];
        }catch (Exception ex){
            throw new IllegalArgumentException("invalid id : " + id);
        }
    }
}
