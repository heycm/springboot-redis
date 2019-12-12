package com.heycm.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 * @date 2019年12月9日11:04:29
 */
@Component
public class RedisUtil {

    private static final TimeUnit TIME_UNIT = TimeUnit.MINUTES;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // ================= Comm =================
    /**
     * 设置过期时间
     * @param key 键
     * @param time 存活时长，负值则永久存活
     * @param timeUnit 时间单位
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

    /**
     * 设置过期时间(分钟)
     * @param key 键
     * @param time 存活时长，负值则永久存活
     * @return 成功true 失败false
     */
    public boolean expire(String key, long time){
        return expire(key, time, TIME_UNIT);
    }

    /**
     * 获取key的过期时间
     * @param key 键
     * @param timeUnit 时间单位
     * @return 时间(单位:timeUnit)，返回0代表永久有效
     */
    public long getExpire(String key, TimeUnit timeUnit){
        return redisTemplate.getExpire(key, timeUnit);
    }

    /**
     * 获取key的过期时间(秒)
     * @param key 键
     * @return 时间(秒)，返回0代表永久有效
     */
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
        return set(key, value, time, TIME_UNIT);
    }

    /**
     * 递增
     * @param key 键
     * @param delta 递增因子，要增加几(大于0)
     * @return
     */
    public long increment(String key, long delta){
        if (delta < 0){
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     * @param key 键
     * @param delta 递减因子，要减少几(大于0)
     * @return
     */
    public long decrement(String key, long delta){
        if (delta < 0){
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    // ================= Map =================
    /**
     * HashGet
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hGet(String key, String item){
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有项值
     * @param key 键
     * @return 对应的多个项值
     */
    public Map<Object, Object> hmGet(String key){
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     * @param key 键
     * @param map 对应多个项值
     * @return true 成功 false 失败
     */
    public boolean hmSet(String key, Map<String, Object> map){
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet，并设置时间
     * @param key 键
     * @param map 对应项值
     * @param time 时长
     * @param timeUnit 时间单位
     * @return true 成功；false 失败
     */
    public boolean hmSet(String key, Map<String, Object> map, long time, TimeUnit timeUnit){
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0){
                expire(key, time, timeUnit);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet，并设置时间(分钟)
     * @param key 键
     * @param map 对应项值
     * @param time 时长
     * @return true 成功，false 失败
     */
    public boolean hmSet(String key, Map<String, Object> map, long time){
        return hmSet(key, map, time, TIME_UNIT);
    }

    /**
     * 向一张hash表中放入数据，如果不存在将创建
     * @param key 键
     * @param item 项
     * @param value 值
     * @return true 成功，false 失败
     */
    public boolean hSet(String key, String item, Object value){
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据，如果不存在将创建，并设置过期时间
     * @param key 键
     * @param item 项
     * @param value 值
     * @param time 时长
     * @param timeUnit 时间单位
     * @return true 成功，false 失败
     */
    public boolean hSet(String key, String item, Object value, long time, TimeUnit timeUnit){
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0){
                expire(key, time, timeUnit);
            }
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据，如果不存在将创建，并设置过期时间(分钟)
     * @param key 键
     * @param item 项
     * @param value 值
     * @param time 时长
     * @return true 成功，false 失败
     */
    public boolean hSet(String key, String item, Object value, long time){
        return hSet(key, item, value, time, TIME_UNIT);
    }

    /**
     * 删除hash表中的项
     * @param key 键 不能为null
     * @param item 项 可以传多个 不能为null
     * @return 删除数量 成功，-1 失败
     */
    public Long hDel(String key, Object... item){
        try {
            return redisTemplate.opsForHash().delete(key, item);
        } catch (Exception e) {
            e.printStackTrace();
            return -1L;
        }
    }

    /**
     * 判断hash表中是否有该项
     * @param key 键
     * @param item 值
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item){
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在，就会创建一个，并把新增后的值返回
     * @param key 键
     * @param item 项
     * @param by 增量(大于0)
     * @return
     */
    public Double hIncrement(String key, String item, double by){
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     * @param key 键
     * @param item 项
     * @param by 减量(大于0)
     * @return
     */
    public Double hDecrement(String key, String item, double by){
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    // ================= Set =================
}










