package com.uneed.common.mybatis.cache;

import com.uneed.common.core.collection.CollectionUtil;
import com.uneed.common.core.collection.map.Maps;
import com.uneed.common.mybatis.info.AttributeProperty;
import com.uneed.common.mybatis.utils.AttributeUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import static com.uneed.common.core.lang.ObjectUtil.isNull;
import static com.uneed.common.core.lang.ObjectUtil.nullToDefault;

/**
 * 查询条件属性处理的缓存类
 *
 * @author diablo
 * @date 2019/12/15
 */
@Slf4j
public class QueryCache {

    /**
     * 用来缓存属性字段
     */
    private static Map<String, List<AttributeProperty>> cache;

    /**
     * 单例初始化对象
     */
    private static QueryCache instance;

    /**
     * 私有化构造函数
     */
    private QueryCache() {

    }

    /**
     * 使用单例构造缓存类
     *
     * @return ConditionCache
     */
    public static QueryCache getInstance() {
        if (instance == null) {
            instance = new QueryCache();
            cache = Maps.newConcurrentMap();
        }
        return instance;
    }

    /**
     * 根据条件的java类型，与映射实体类java类型，获取缓存的条件属性集合
     *
     * @param beanClass   条件类类型
     * @param entityClass 实体类类型
     * @return List<ConditionAttribute> 条件属性集合
     */
    public List<AttributeProperty> get(Class<?> beanClass, Class<?> entityClass) {
        List<AttributeProperty> list = cache.get(getKey(beanClass, entityClass));
        if (isNull(list)) {
            if (log.isDebugEnabled()) {
                log.debug(" ==> {}缓存获取条件属性集合为空，开始重新构造条件属性", QueryCache.class);
            }
            list = AttributeUtil.buildAttributeProperties(beanClass, entityClass);
            put(beanClass, entityClass, list);
        }
        return list;
    }

    /**
     * 缓存中设置条件属性集合
     *
     * @param beanClass   条件类类型
     * @param entityClass 实体类类型
     * @param attributes  属性集合
     */
    public void put(Class<?> beanClass, Class<?> entityClass, List<AttributeProperty> attributes) {
        cache.put(getKey(beanClass, entityClass), nullToDefault(attributes, CollectionUtil.newArrayList()));
    }

    /**
     * 缓存中删除条件属性集合
     *
     * @param beanClass   条件类类型
     * @param entityClass 实体类类型
     */
    public void remove(Class<?> beanClass, Class<?> entityClass) {
        cache.remove(getKey(beanClass, entityClass));
    }

    /**
     * 构建key名称
     *
     * @param beanClass   条件类类型
     * @param entityClass 实体类类型
     * @return String 缓存key名称
     */
    private String getKey(Class<?> beanClass, Class<?> entityClass) {
        return beanClass.getName() + "#" + entityClass.getName();
    }

}
