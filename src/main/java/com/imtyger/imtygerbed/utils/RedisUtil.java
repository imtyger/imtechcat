package com.imtyger.imtygerbed.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Author: imtygerx@gmail.com
 * @Date: 2019/12/4
 */

@Slf4j
@Component
public class RedisUtil {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public void save(String key, Object value) {
        stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(value));
        log.info("save key: {} to redis cache", key);
    }

    public String get(String key) {
        String value = stringRedisTemplate.opsForValue().get(key);
        if(!StringUtils.isEmpty(value)){
            log.info("get key:{} from redis cache.", key);
        }
        return value;
    }

    public boolean delete(String key) {
        Boolean delete = stringRedisTemplate.delete(key);
        if(delete != null && delete){
            log.info("delete key:{} from redis cache.", key);
        }
        return delete != null && delete;
    }

    public Long incr(String key) {
        try {
            long incr = stringRedisTemplate.opsForValue().increment(key);
            log.info("incr key: {} incr: {} to redis cache.", key, incr);
            return incr;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return 0l;
        }
    }

    public void expire(String key, final int seconds) {
        try {
            if (seconds > 0) {
                stringRedisTemplate.expire(key, seconds, TimeUnit.SECONDS);
                log.info("expire key: {} seconds: {} to redis cache.", key, seconds);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}