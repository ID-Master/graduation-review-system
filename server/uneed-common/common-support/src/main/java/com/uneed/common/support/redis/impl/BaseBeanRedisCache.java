package com.uneed.common.support.redis.impl;

import com.uneed.common.core.bean.BeanUtil;
import com.uneed.common.core.collection.CollectionUtil;
import com.uneed.common.core.collection.Lists;
import com.uneed.common.core.collection.map.Maps;
import com.uneed.common.core.exception.unchecked.BusinessException;
import com.uneed.common.core.lang.ObjectUtil;
import com.uneed.common.core.lang.StringUtil;
import com.uneed.common.support.redis.BeanRedisCache;
import com.uneed.common.support.util.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.uneed.common.core.lang.ObjectUtil.*;
import static com.uneed.common.support.constant.KeyConstant.BEAN_ENTITY;

/**
 * Class description goes here.
 *
 * @author diablo
 * @date 2020/1/16
 */
public abstract class BaseBeanRedisCache<T, ID extends Serializable, V> implements BeanRedisCache<T, ID, V> {

    //************************************************* 常量 *******************************************************************************/

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    //************************************************* 私有属性 *******************************************************************************/

    /**
     * redisTemplate对象，私有属性，用做缓存，使用必须通过getRedisTemplate()方法获取
     */
    private RedisTemplate<String, V> redisTemplate;

    /**
     * id的获取方法，私有属性，用来缓存，使用必须通过getIdGetterMethod()方法获取
     */
    private Method idGetterMethod;

    /**
     * bean类型，私有属性，用来缓存，使用必须通过getBeanClass()方法获取
     */
    private Class<T> beanClass;

    /**
     * id的class类型，私有属性，用来缓存，使用必须通过getIdClass()方法获取
     */
    private Class<ID> idClass;

    //************************************************* 公用方法 *******************************************************************************/

    @Override
    public void add(T bean) {
        String key = getKey(getId(bean));
        if (StringUtil.isEmpty(key)) {
            return;
        }
        V value = getValue(bean, key);
        if (isNull(value)) {
            return;
        }
        getHash().put(key, value);
    }

    @Override
    public void add(Collection<T> beans) {
        Map<String, V> map = buildBeanMap(beans);
        if (map.isEmpty()) {
            return;
        }
        getHash().putAll(map);
    }

    @Override
    public V get(ID id) {
        String key = getKey(id);
        V value = StringUtil.isEmpty(key) ? null : getHash().get(key);
        return filterValue(value);
    }

    @Override
    public List<V> list() {
        return filterValues(getHash().values());
    }

    @Override
    public List<V> list(Collection<ID> ids) {
        Set<String> keys = getKeys(ids);
        return keys.isEmpty() ? Lists.newArrayList() : filterValues(getHash().multiGet(keys));
    }

    @Override
    public void del(ID id) {
        String key = getKey(id);
        if (StringUtil.isEmpty(key)) {
            return;
        }
        getHash().delete(key);
    }

    @Override
    public void del(Collection<ID> ids) {
        Set<String> keys = getKeys(ids);
        if (keys.isEmpty()) {
            return;
        }
        getHash().delete(keys.toArray());
    }

    @Override
    public void delBean(T bean) {
        del(getId(bean));
    }

    @Override
    public void delBean(Collection<T> beans) {
        del(CollectionUtil.getPropertySet(beans, idField()));
    }

    @Override
    public void init(Collection<T> beans) {
        clear();
        add(beans);
    }

    @Override
    public void clear() {
        getRedisTemplate().delete(getHashKey());
    }

    //************************************************* redis操作工具 *******************************************************************************/

    /**
     * 依靠子类注入redisTemplate对象
     *
     * @param redisTemplate redisTemplate对象
     */
    public void setRedisTemplate(RedisTemplate<String, V> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 获取redisTemplate对象，如果注入的为空，则从spring容器中获取
     *
     * @return RedisTemplate<String, T> redisTemplate对象
     */
    @SuppressWarnings("ALL")
    protected RedisTemplate<String, V> getRedisTemplate() {
        if (ObjectUtil.isNull(redisTemplate)) {
            redisTemplate = (RedisTemplate<String, V>) SpringUtil.getBean("redisTemplate");
        }
        return redisTemplate;
    }

    /**
     * 通过redisTemplate对象获取HashOperation对象
     *
     * @return BoundHashOperations<String, ID, V> HashOperation对象
     */
    protected BoundHashOperations<String, String, V> getHash() {
        return getRedisTemplate().boundHashOps(getHashKey());
    }

    //************************************************* redis键值 *******************************************************************************/

    /**
     * 获取hash的key值，组成：BEAN_ENTITY + ":" + prefix()
     *
     * @return String hash key 值
     */
    protected String getHashKey() {
        return BEAN_ENTITY + ":" + prefix();
    }

    /**
     * redis key 的前缀，默认为实体类名
     *
     * @return redis的前缀
     */
    protected String prefix() {
        return getBeanClass().getName();
    }

    /**
     * 反射获取bean对象的主键id
     *
     * @param id bean对象
     * @return String 主键id
     */
    protected String getKey(ID id) {
        return StringUtil.toString(id);
    }

    /**
     * 获取节点node的key值集合
     *
     * @param ids 节点id集合
     * @return Set<String> node节点的hashKey值集合
     */
    protected Set<String> getKeys(Collection<ID> ids) {
        Set<String> set = Lists.newHashSet();

        if (isNull(ids)) {
            return set;
        }

        for (ID id : ids) {
            String key = getKey(id);
            if (StringUtil.isEmpty(key)) {
                continue;
            }
            set.add(key);
        }
        return set;
    }

    //************************************************* bean对象的class *******************************************************************************/

    /**
     * 获取泛型的java类型
     *
     * @return Class<T> 泛型类型
     */
    @SuppressWarnings("ALL")
    protected Class<T> getBeanClass() {
        if (ObjectUtil.isNull(beanClass)) {
            beanClass = (Class<T>) BeanUtil.getSuperClassActualType(getClass(), 0);
        }
        return beanClass;
    }

    /**
     * 获取泛型的java类型
     *
     * @return Class<T> 泛型类型
     */
    @SuppressWarnings("ALL")
    protected Class<ID> getIdClass() {
        if (ObjectUtil.isNull(idClass)) {
            idClass = (Class<ID>) BeanUtil.getSuperClassActualType(getClass(), 1);
        }
        return idClass;
    }

    //************************************************* 获取 id *******************************************************************************/

    /**
     * 主键字段名称
     *
     * @return 主键字段
     */
    protected String idField() {
        return "id";
    }

    /**
     * 获取id的反射方法
     *
     * @return Method 反射方法
     */
    protected Method getIdGetterMethod() {
        if (isNull(idGetterMethod)) {
            idGetterMethod = BeanUtil.getReadMethod(getBeanClass(), idField());
        }
        return idGetterMethod;
    }

    /**
     * 获取主键值
     *
     * @param bean bean对象
     * @return ID 主键值
     */
    @SuppressWarnings("ALL")
    protected ID getId(T bean) {
        try {
            return isNotNull(bean) ? (ID) getIdGetterMethod().invoke(bean) : null;
        } catch (Exception e) {
            log.error("操作缓存数据异常！从[" + getBeanClass().getName() + "]中获取主键id失败：" + e.getMessage(), e);
            throw new BusinessException("操作缓存数据异常！从[" + getBeanClass().getName() + "]中获取主键id失败：" + e.getMessage());
        }
    }

    /**
     * 将key转换成id
     *
     * @param key 键值
     * @return ID id
     */
    protected ID getId(String key) {
        return convert(key, getIdClass());
    }

    //************************************************* 获取 value *******************************************************************************/

    /**
     * 根据bean对象获取需要返回的值
     *
     * @param bean bean对象
     * @return V 需要返回的值
     */
    @SuppressWarnings("ALL")
    protected V getValue(T bean, String key) {
        return (V) bean;
    }

    /**
     * 根据bean对象获取需要返回的值
     *
     * @param bean bean对象
     * @return V 需要返回的值
     */
    @SuppressWarnings("ALL")
    protected V getValue(T bean, String key, Map<String, V> map) {
        return (V) bean;
    }

    //************************************************* 其他 *******************************************************************************/

    /**
     * 根据bean对象集合，构建方便批量操作的map集合
     *
     * @param beans 要缓存的bean集合
     * @return Map<String, V> ma集合
     */
    protected Map<String, V> buildBeanMap(Collection<T> beans) {
        Map<String, V> map = Maps.newHashMap();

        if (isNull(beans)) {
            return map;
        }

        for (T bean : beans) {
            String key = getKey(getId(bean));
            if (StringUtil.isEmpty(key)) {
                continue;
            }
            V value = getValue(bean, key, map);
            if (isNull(value)) {
                continue;
            }
            map.put(key, value);
        }
        return map;
    }

    /**
     * 过滤返回对象
     *
     * @param value 返回的对象
     * @return V 过滤之后的对象
     */
    protected V filterValue(V value) {
        return value;
    }

    /**
     * 过滤空的返回对象
     *
     * @param values 返回对象集合
     * @return List<V> 过滤空之后的集合
     */
    protected List<V> filterValues(Collection<V> values) {
        List<V> list = Lists.newArrayList();

        if (isNull(values)) {
            return list;
        }

        for (V value : values) {
            if (isNull(value)) {
                continue;
            }
            list.add(value);
        }
        return list;
    }
}
