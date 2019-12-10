package com.heycm.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 * @date 2019年12月9日11:04:29
 */
@Component
public class RedisUtil {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置过期时间
     * @param key 键
     * @param time 存活时长，负值则永久存活
     * @param timeUnit 时间单位，默认min
     * @return 成功true 失败false
     */
    public boolean expire(String key, long time, TimeUnit timeUnit){
        try {
            if(time>0){
                redisTemplate.expire(key, time, timeUnit);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean expire(String key, long time){
        return expire(key, time, TimeUnit.MINUTES);
    }

    /**
     * 获取key的过期时间
     * @param key 键
     * @param timeUnit 时间单位，默认sec
     * @return 时间(单位:timeUnit)，返回0代表永久有效
     */
    public long getExpire(String key, TimeUnit timeUnit){
        return redisTemplate.getExpire(key, timeUnit);
    }
    public long getExpire(String key){
        return getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     * @param key 键
     * @return 存在 true 不存在 false
     */
    public boolean hasKey(String key){
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     * @param key 可以传一个或多个键
     * @return 成功返回删除的数量，失败返回-1L
     */
    @SuppressWarnings("unchecked")
    public Long del(String... key){
        if (key!=null && key.length>0){
            if (key.length == 1){
                redisTemplate.delete(key[0]);
                return 1L;
            }else {
                return redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
        return -1L;
    }

    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     * @return 成功true 失败false
     */
    public boolean set(String key, Object value){
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    public Object get(String key){
        return key==null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间 若time<0则无限期
     * @param timeUnit 时间单位
     * @return
     */
    public boolean set(String key, Object value, long time, TimeUnit timeUnit){
        try {
            if (time > 0){
                redisTemplate.opsForValue().set(key, value, time, timeUnit);
            }else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间(分钟)
     * @param key 键
     * @param value 值
     * @param time 时间，若time<0则无限期
     * @return
     */
    public boolean set(String key, Object value, long time){
        return set(key, value, time, TimeUnit.MINUTES);
    }
}
