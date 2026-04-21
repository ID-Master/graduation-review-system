package com.uneed.common.dict.api;

import org.springframework.data.redis.core.BoundHashOperations;

import java.util.Map;

/**
 * 配置缓存操作接口，包含了在缓存中对字典数据进行增、删、改、查等操作
 *
 * @author anding.huang@u-need.cn
 * @date 2019/12/18
 */
public interface ConfigRepertory {

    /**
     * 根据配置编号从缓存获取配置值
     *
     * @param key 配置编号
     * @return String 配置值
     */
    String get(String key);

    /**
     * 将配置信息添加至缓存中，允许添加空值
     *
     * @param key   配置编号
     * @param value 配置值
     */
    void add(String key, String value);

    /**
     * 批量添加配置信息至缓存中，允许添加空值
     *
     * @param configMap 配置信息map集合
     * @since 1.1.0
     */
    void add(Map<String, String> configMap);

    /**
     * 根据配置编号，从缓存中删除配置信息
     *
     * @param key 配置编号
     */
    void del(String key);

    /**
     * 清空所有配置信息
     *
     * @since 1.1.0
     */
    void clear();

    /**
     * 初始化配置信息
     *
     * @param configMap 配置信息map集合
     */
    void initialization(Map<String, String> configMap);

    /**
     * 获取RedisTemplate的BoundHashOperations对象
     *
     * @return BoundHashOperations<String, String, String>
     * @since 1.1.0
     */
    BoundHashOperations<String, String, String> getHash();

}
