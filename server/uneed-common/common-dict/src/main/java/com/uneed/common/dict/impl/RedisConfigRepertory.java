package com.uneed.common.dict.impl;

import com.uneed.common.core.date.TimeMeter;
import com.uneed.common.core.lang.StringUtil;
import com.uneed.common.core.lang.Validate;
import com.uneed.common.dict.api.ConfigRepertory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;

import static com.uneed.common.core.lang.ObjectUtil.*;
import static com.uneed.common.dict.constant.DictConstant.CONFIG_REDIS_KEY;

/**
 * Class description goes here.
 *
 * @author diablo
 * @date 2019/12/18
 */
@Slf4j
public class RedisConfigRepertory implements ConfigRepertory {

    /**
     * redisTemplate对象，需要注入
     */
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 配置数据在redis中的key值，可以注入
     */
    private String redisKey = CONFIG_REDIS_KEY;

    /**
     * 带redisTemplate参数的构造函数
     *
     * @param redisTemplate redisTemplate对象
     */
    public RedisConfigRepertory(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 带redisTemplate参数的构造函数
     *
     * @param redisTemplate redisTemplate对象
     * @param redisKey      配置数据在redis中的key值
     */
    public RedisConfigRepertory(RedisTemplate<String, String> redisTemplate, String redisKey) {
        this(redisTemplate);
        this.redisKey = redisKey;
    }

    //////////////////////////////////////// 接口方法实现 /////////////////////////////////////////////////////////////////////

    /**
     * 根据配置编号从缓存获取配置值
     */
    @Override
    public String get(String key) {
        return isNotEmpty(key) ? getHash().get(key) : StringUtil.EMPTY;
    }

    /**
     * 将配置信息添加至缓存中，允许添加空值
     */
    @Override
    public void add(String key, String value) {
        if (isNotEmpty(key)) {
            getHash().put(key, nullToDefault(value, StringUtil.EMPTY));
        }
    }

    /**
     * 批量添加配置信息至缓存中，允许添加空值
     */
    @Override
    public void add(Map<String, String> configMap) {
        TimeMeter meter = new TimeMeter();
        if (isNotEmpty(configMap)) {
            getHash().putAll(configMap);
        }
        if (log.isDebugEnabled()) {
            long size = isEmpty(configMap) ? 0 : configMap.size();
            log.debug("----------->> <<<新增>>>所有【系统配置】数据完成！配置数量：{}，用时：{}ms", size, meter.sign());
        }
    }

    /**
     * 根据配置编号，从缓存中删除配置信息
     */
    @Override
    public void del(String key) {
        if (isNotEmpty(key)) {
            getHash().delete(key);
        }
    }

    /**
     * 清空所有配置信息
     */
    @Override
    public void clear() {
        TimeMeter meter = new TimeMeter();
        Boolean result = redisTemplate.delete(redisKey);
        if (log.isDebugEnabled()) {
            Long count = getHash().size();
            if (notEqual(result, Boolean.TRUE)) {
                count = 0L;
            }
            log.debug("----------->> <<<清空>>>所有【系统配置】数据完成！配置数量：{}，用时：{}ms", nullToDefault(count, 0L), meter.sign());
        }
    }

    /**
     * 初始化配置信息
     */
    @Override
    public void initialization(Map<String, String> configMap) {
        TimeMeter meter = new TimeMeter();
        if (log.isDebugEnabled()) {
            log.debug("============ 初始化缓存【系统配置】数据【开始】，缓存中配置信息总数：{} ============", getHash().size());
        }
        //先清空配置缓存数据
        clear();
        //批量添加配置信息
        add(configMap);
        if (log.isDebugEnabled()) {
            log.debug("============ 初始化缓存【系统配置】数据<<<完成>>>，缓存中配置信息总数：{}，总耗时{}ms ============", getHash().size(), meter.sign());
        }
    }

    /**
     * 获取RedisTemplate的BoundHashOperations对象，系统会为每一个字典集合构建一个hash对象
     */
    @Override
    public BoundHashOperations<String, String, String> getHash() {
        Validate.notNull(this.redisTemplate, "redisTemplate can't be null!");
        return this.redisTemplate.boundHashOps(redisKey);
    }

}
