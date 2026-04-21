package com.uneed.common.support.redis;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Class description goes here.
 *
 * @author diablo
 * @date 2020/1/16
 */
public interface BeanRedisCache<T, ID extends Serializable, V> {

    /**
     * 将单个bean对象添加至redis缓存中
     *
     * @param bean bean对象
     */
    void add(T bean);

    /**
     * 批量添加bean对象至redis缓存中
     *
     * @param beans bean对象集合
     */
    void add(Collection<T> beans);

    /**
     * 根据数据id从redis缓存中获取数据对象
     *
     * @param id 数据主键id
     * @return V 数据对象
     */
    V get(ID id);

    /**
     * 从redis缓存中获取所有数据对象集合
     *
     * @return List<V> 数据对象集合
     */
    List<V> list();

    /**
     * 根据数据id集合从redis缓存中获取数据对象集合
     *
     * @param ids 数据主键id集合
     * @return List<V> 数据对象集合
     */
    List<V> list(Collection<ID> ids);

    /**
     * 根据数据id从redis缓存中删除对应的数据对象
     *
     * @param id 数据主键id
     */
    void del(ID id);

    /**
     * 根据数据id集合从redis缓存中删除对应的数据对象集合
     *
     * @param ids 数据主键id集合
     */
    void del(Collection<ID> ids);

    /**
     * 根据bean对象，从redis中删除对应的缓存数据
     *
     * @param bean bean对象
     */
    void delBean(T bean);

    /**
     * 根据bean对象集合，从redis中删除对应的缓存数据
     *
     * @param beans bean对象集合
     */
    void delBean(Collection<T> beans);

    /**
     * 初始化bean数据，会先清空对应的数据集合，再将数据对象集合缓存到redis中
     *
     * @param beans 初始化的数据对象集合
     */
    void init(Collection<T> beans);

    /**
     * 清空redis中的bean对象
     */
    void clear();

}
