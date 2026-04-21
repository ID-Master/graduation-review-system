package cn.edu.cuhk.mkt.common.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * redis序列号
 * @author taokai
 */
@Slf4j
@DependsOn(value = {"redisTemplate"})
@Component
public class RedisSequenceFactory {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     *
     * @param key
     * @param value
     * @param expireTime
     */
    public void set(String key, long value, Date expireTime) {
        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        counter.set(value);
        counter.expireAt(expireTime);
    }

    /**
     *
     * @param key
     * @param value
     * @param timeout
     * @param unit
     */
    public void set(String key, int value, long timeout, TimeUnit unit) {
        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        counter.set(value);
        counter.expire(timeout, unit);
    }

    /**
     *
     * @param key
     * @return
     */
    public long generate(String key) {
        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        return counter.incrementAndGet();
    }

    /**
     *
     * @param key
     * @param expireTime
     * @return
     */
    public long generate(String key, Date expireTime) {
        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        Boolean expireResult = counter.expireAt(expireTime);
        // 1629647999999
        log.info("=====>设置失效时间: {}, 时间戳: {}", expireResult, expireTime.getTime());
        return counter.incrementAndGet();
    }

    /**
     *
     * @param key
     * @param increment
     * @return
     */
    public long generate(String key, int increment) {
        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        return counter.addAndGet(increment);
    }

    /**
     *
     * @param key
     * @param increment
     * @param expireTime
     * @return
     */
    public long generate(String key, int increment, Date expireTime) {
        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        counter.expireAt(expireTime);
        return counter.addAndGet(increment);
    }

    /**
     *
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    public long generate(String key, long timeout, TimeUnit unit) {
        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        counter.expire(timeout, unit);
        return counter.incrementAndGet();
    }

    /**
     *
     * @param key
     * @param increment
     * @param timeout
     * @param unit
     * @return
     */
    public long generate(String key, int increment, long timeout, TimeUnit unit) {
        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        counter.expire(timeout, unit);
        return counter.addAndGet(increment);
    }

    /**
     *
     * @param key
     * @param increment
     * @param size
     * @return
     */
    public List<Long> generateBatch(String key, int increment, int size) {
        RedisAtomicLong counter = new RedisAtomicLong(key, this.redisTemplate.getConnectionFactory());
        long max = counter.addAndGet(increment * size);
        long min = max - (increment * (size - 1));
        List<Long> list = new ArrayList<>();
        list.add(min);
        for(int i = 1; i < size; ++i) {
            list.add(min + (long)(increment * i));
        }
        return list;
    }

    /**
     *
     * @param key
     * @return
     */
    public long getExpire(String key) {
        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        return counter.getExpire();
    }

}
