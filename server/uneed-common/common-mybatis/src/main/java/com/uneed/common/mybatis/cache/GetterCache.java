package com.uneed.common.mybatis.cache;

import com.uneed.common.core.bean.BeanUtil;
import com.uneed.common.core.collection.map.Maps;
import com.uneed.common.core.lang.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static com.uneed.common.core.lang.ObjectUtil.*;

/**
 * 条件对象的getter方法缓存类，用来存放解析的缓存方法
 *
 * @author diablo
 * @date 2020/4/14
 */
@Slf4j
public class GetterCache {

    /**
     * 用来缓存条件对象的getter方法
     */
    private static Map<String, List<Method>> cache;

    /**
     * 单例初始化对象
     */
    private static GetterCache instance;

    /**
     * 私有化构造函数
     */
    private GetterCache() {

    }

    /**
     * 使用单例构造缓存类
     *
     * @return ConditionCache
     */
    public static GetterCache getInstance() {
        if (instance == null) {
            instance = new GetterCache();
            cache = Maps.newConcurrentMap();
        }
        return instance;
    }

    /**
     * 根据条件类java类型，获取缓存的条件类getter方法集合
     *
     * @param beanClass 条件对象java类型
     * @return List<Method> 条件类getter方法集合
     */
    public List<Method> get(Class<?> beanClass) {
        List<Method> list = cache.get(getKey(beanClass));
        if (isNull(list)) {
            if (log.isDebugEnabled()) {
                log.debug(" ==> {}缓存获取条件对象getter方法集合为空，开始重新构造条件对象getter方法集合", GetterCache.class);
            }
            list = BeanUtil.getReadMethod(beanClass);
            put(beanClass, list);
        }
        return list;
    }

    /**
     * 根据条件类java类型、条件类字段获取条件类getter方法
     *
     * @param beanClass 条件对象java类型
     * @param field     条件类字段
     * @return Method 条件类getter方法
     */
    public Method get(Class<?> beanClass, Field field) {
        return isNotNull(field) ? get(beanClass, field.getName(), field.getType()) : null;
    }

    /**
     * 根据条件类java类型、条件类字段名称、条件类字段类型获取条件类getter方法
     *
     * @param beanClass 条件对象java类型
     * @param fieldName 条件对象字段名称
     * @param fieldType 条件对象字段类型
     * @return Method 条件类的getter方法
     */
    public Method get(Class<?> beanClass, String fieldName, Class<?> fieldType) {
        String name = StringUtil.getterName(fieldName, fieldType);
        for (Method method : get(beanClass)) {
            if (equal(name, method.getName()) && equal(fieldType, method.getReturnType())) {
                return method;
            }
        }
        return null;
    }

    /**
     * 缓存中设置条件对象类getter集合
     *
     * @param beanClass 条件对象类类型
     * @param getters   条件对象getter方法集合
     */
    public void put(Class<?> beanClass, List<Method> getters) {
        cache.put(getKey(beanClass), getters);
    }

    /**
     * 缓存中删除条件类getter方法集合
     *
     * @param beanClass 条件对象类型
     */
    public void remove(Class<?> beanClass) {
        cache.remove(getKey(beanClass));
    }

    /**
     * 构建key名称
     *
     * @param beanClass 条件对象类型
     * @return String 缓存key名称
     */
    private String getKey(Class<?> beanClass) {
        return beanClass.getName();
    }
}
