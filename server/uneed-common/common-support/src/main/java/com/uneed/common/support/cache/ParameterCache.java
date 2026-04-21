package com.uneed.common.support.cache;

import com.uneed.common.support.util.SpringUtil;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

import static com.uneed.common.core.lang.ObjectUtil.isNull;
import static com.uneed.common.support.constant.KeyConstant.PARAMETER_CACHE;

/**
 * 参数缓存器，用来缓存导出操作的参数.
 *
 * @author diablo
 * @date 2020/4/28
 */
public class ParameterCache {

    /**
     * 默认失效时间，单位(秒)，默认为半小时
     */
    public static final long DEFAULT_EXPIRE = 60 * 30;

    /**
     * redisTemplate对象
     */
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 参数缓存对象
     */
    private static ParameterCache cache;

    /**
     * 私有化构造函数，并初始化redisTemplate的值
     */
    @SuppressWarnings("unchecked")
    private ParameterCache() {
        redisTemplate = (RedisTemplate<String, Object>) SpringUtil.getBean("redisTemplate");
    }

    /**
     * 外界通过instance方法来获取参数缓存对象
     */
    public static ParameterCache instance() {
        if (isNull(cache)) {
            cache = new ParameterCache();
        }
        return cache;
    }

    /**
     * redis中保存参数信息，默认时长为30分钟
     *
     * @param key   键
     * @param value 值
     */
    public void put(String key, Object value) {
        put(key, value, DEFAULT_EXPIRE);
    }

    /**
     * redis缓存中设置参数信息
     *
     * @param key    键
     * @param value  值
     * @param expire 失效时间(秒)
     */
    public void put(String key, Object value, long expire) {
        redisTemplate.opsForValue().set(String.join(":", PARAMETER_CACHE, key), value, expire, TimeUnit.SECONDS);
    }

    /**
     * 根据key获取缓存对象，转换失败会异常
     *
     * @param key key
     * @param <P> 参数泛型
     * @return 参数值
     */
    @SuppressWarnings("all")
    public <P> P get(String key) {
        String redisKey = String.join(":", PARAMETER_CACHE, key);
        if (redisTemplate.hasKey(redisKey)) {
            return (P) redisTemplate.opsForValue().get(redisKey);
        }
        return null;
    }

    /**
     * 根据key删除缓存对象
     *
     * @param key 键
     */
    @SuppressWarnings("unused")
    public void del(String key) {
        redisTemplate.delete(String.join(":", PARAMETER_CACHE, key));
    }
}
