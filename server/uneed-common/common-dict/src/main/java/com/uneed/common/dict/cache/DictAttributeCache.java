package com.uneed.common.dict.cache;

import com.uneed.common.core.collection.Lists;
import com.uneed.common.core.collection.map.Maps;
import com.uneed.common.dict.attribute.DictAttribute;
import com.uneed.common.dict.util.DictAttributeUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import static com.uneed.common.core.lang.ObjectUtil.isNotNull;
import static com.uneed.common.core.lang.ObjectUtil.isNull;

/**
 * 字典属性缓存类，单例模式
 *
 * @author anding.huang@u-need.cn
 * @date 2019/12/16
 */
@Slf4j
public class DictAttributeCache {

    /**
     * 本地缓存
     */
    private static Map<String, List<DictAttribute>> cache;

    /**
     * 字典属性缓存类
     */
    private static DictAttributeCache instance;

    /**
     * 私有化构造函数
     */
    private DictAttributeCache() {
        cache = Maps.newConcurrentMap();
    }

    /**
     * 对外暴露获取实例的方法，懒汉模式
     *
     * @return DictAttributeCache
     */
    public static DictAttributeCache getInstance() {
        if (isNull(instance)) {
            instance = new DictAttributeCache();
        }
        return instance;
    }

    /**
     * 根据类型获取其映射的字典缓存属性集合
     *
     * @param beanClass bean对象类型
     * @return List<DictAttribute>
     */
    public List<DictAttribute> get(Class<?> beanClass) {
        List<DictAttribute> list = isNotNull(beanClass) ? cache.get(beanClass.getName()) : Lists.newArrayList();
        if (isNull(list)) {
            if (log.isDebugEnabled()) {
                log.debug(" ==> {}缓存获取字典属性集合为空，开始重新构造条件属性", DictAttributeCache.class);
            }
            list = DictAttributeUtil.buildAttributes(beanClass);
            put(beanClass, list);
        }
        return list;
    }

    /**
     * 缓存中设置字典属性缓存集合
     *
     * @param beanClass  bean对象类型
     * @param attributes 字典属性集合
     */
    public void put(Class<?> beanClass, List<DictAttribute> attributes) {
        cache.put(beanClass.getName(), attributes);
    }

    /**
     * 根据类型删除本地缓存
     *
     * @param clazz bean对象类型
     */
    public void remove(Class<?> clazz) {
        cache.remove(clazz.getName());
    }
}
