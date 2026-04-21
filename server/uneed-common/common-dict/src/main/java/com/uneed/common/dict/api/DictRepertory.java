package com.uneed.common.dict.api;

import com.uneed.common.dict.entity.Dict;
import org.springframework.data.redis.core.BoundHashOperations;

import java.util.Collection;
import java.util.List;

/**
 * 字典缓存操作接口，包含了在缓存中对字典数据进行增、删、改、查等操作
 *
 * @author anding.huang@u-need.cn
 * @date 2019/12/17
 */
public interface DictRepertory {

    /**
     * 根据字典的key获取单条数据字典记录
     *
     * @param key 字典key
     * @return Dict 数据字典对象
     */
    Dict get(String key);

    /**
     * 根据字典key集合获取key对应的字典记录集合
     *
     * @param keys 字典key集合
     * @return List<Dict> 数据字典集合
     */
    List<Dict> list(Collection<String> keys);

    /**
     * 根据字典key，获取其对应的子集，获取的结果集会按照sort值进行升序排列
     *
     * @param key 字典key
     * @return List<Dict> 数据字典集合
     */
    List<Dict> subset(String key);

    /**
     * 根据字典key，获取其对应的子集，isEnabled参数用来标记是否只取有效的数据，获取的结果集会按照sort值进行升序排列
     *
     * @param key       字典key
     * @param isEnabled 标记是否只获取有效的数据
     * @return List<Dict> 数据字典集合
     */
    List<Dict> subset(String key, boolean isEnabled);

    /**
     * 新增数据字典
     * <p>
     * 该方法接收一个封装后的数据字典对象，并将该字典数据加入到缓存中
     *
     * @param dict 数据字典对象
     */
    void add(Dict dict);

    /**
     * 批量新增数据字典
     * <p>
     * 该方法接收一个封装后的数据字典对象集合，并将集合中的字典数据加入到缓存中
     *
     * @param beans 数据字典集合
     */
    void add(Collection<Dict> beans);

    /**
     * 根据字典的key删除key对应的字典数据及其子集
     *
     * @param key 字典key
     */
    void del(String key);

    /**
     * 根据传入的集合删除其对应的字典数据，并删除其对应的子集
     *
     * @param beans 字典的key集合
     */
    void del(Collection<String> beans);

    /**
     * 清空所有字典数据
     *
     * @since 1.1.0
     */
    void clear();

    /**
     * 初始化缓存中的字典数据
     * <p>
     * 该方法会做俩件事：
     * <p>
     * 1.先清空所有字典的缓存数据
     * <p>
     * 2.将字典数据集合添加到缓存中
     *
     * @param beans 数据字典集合
     */
    void initialization(Collection<Dict> beans);

    /**
     * 获取RedisTemplate的BoundHashOperations对象
     *
     * @return BoundHashOperations<String, String, Dict>
     * @since 1.1.0
     */
    BoundHashOperations<String, String, Dict> getHash();

}
