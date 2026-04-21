package com.uneed.common.support.util;

import com.uneed.common.annotation.dict.DictField;
import com.uneed.common.annotation.enums.DictProperty;
import com.uneed.common.core.lang.Validate;
import com.uneed.common.dict.api.DictRepertory;
import com.uneed.common.dict.attribute.DictAttribute;
import com.uneed.common.dict.cache.DictAttributeCache;
import com.uneed.common.dict.entity.Dict;
import org.springframework.data.redis.core.BoundHashOperations;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.uneed.common.core.bean.BeanUtil.executeSetter;
import static com.uneed.common.core.bean.BeanUtil.getProperty;
import static com.uneed.common.core.lang.ObjectUtil.*;
import static com.uneed.common.core.lang.StringUtil.EMPTY;

/**
 * 数据字典工具类
 * <p>
 * 为了方便在业务系统中获取数据字典相关属性、值，提供的一个工具类，用法同{@link DictRepertory}类
 * <p>
 * 该工具类方法可以直接使用，不需再注入
 *
 * @author diablo
 * @date 2020/5/4
 * @since 1.1.0
 */
public class DictUtil {

    /**
     * 私有化构造函数
     */
    private DictUtil() {
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * 根据数据字典的key，获取对应字典对象
     *
     * @param key 字典key
     * @return Dict 字典对象
     */
    public static Dict get(String key) {
        return getRepertory().get(key);
    }

    /**
     * 根据数据字典的key，获取对应字典名称
     *
     * @param key 字典key
     * @return String 字典名称
     */
    public static String getName(String key) {
        Dict dict = getRepertory().get(key);
        return isNotNull(dict) ? nullToDefault(dict.getName(), EMPTY) : EMPTY;
    }

    /**
     * 根据数据字典的key，获取对应字典值
     *
     * @param key 字典key
     * @return String 字典值
     */
    public static String getValue(String key) {
        Dict dict = getRepertory().get(key);
        return isNotNull(dict) ? nullToDefault(dict.getValue(), EMPTY) : EMPTY;
    }

    /**
     * 根据字典的key，获取对应字典的属性值，需要获取字典属性值由参数property指定
     *
     * @param key      字典key
     * @param property 字典属性，详情请见：{@link DictProperty}
     * @param <V>      字典属性值所对应java数据类型
     * @return V 字典属性值
     */
    @SuppressWarnings("unchecked")
    public static <V> V getPropertyValue(String key, DictProperty property) {
        Dict dict = getRepertory().get(key);
        if (isNull(dict) || isNull(property) || equal(property, DictProperty.DICT)) {
            return (V) dict;
        }
        return equal(property, DictProperty.PARENT_DICT) ? (V) getRepertory().get(dict.getParent()) : getProperty(dict, property.getCode());
    }

    /**
     * 获取字典key值集合获取其对应的字典数据集合
     *
     * @param keys 字段的key值集合
     * @return List<Dict> key值集合对应的字典数据集合
     */
    public static List<Dict> list(Collection<String> keys) {
        return getRepertory().list(keys);
    }

    /**
     * 根据字典key，获取其对应的子集，获取的结果集会按照sort值进行升序排列
     *
     * @param key 字典key
     * @return List<Dict> 数据字典集合
     */
    public static List<Dict> subset(String key) {
        return getRepertory().subset(key);
    }

    /**
     * 根据字典key，获取其对应的子集，isEnabled参数用来标记是否只取有效的数据，获取的结果集会按照sort值进行升序排列
     *
     * @param key       字典key
     * @param isEnabled 标记是否只获取有效的数据
     * @return List<Dict> 数据字典集合
     */
    public static List<Dict> subset(String key, boolean isEnabled) {
        return getRepertory().subset(key, isEnabled);
    }

    /**
     * 获取字典key值集合获取其对应的字典数据集合，并封装成map对象
     *
     * @param keys 字段的key值集合
     * @return Map<String, Dict> key值集合对应的字典数据map集合
     */
    public static Map<String, Dict> map(Collection<String> keys) {
        return buildMap(list(keys));
    }

    /**
     * 根据字典key，获取其对应的子集，并封装成map对象
     *
     * @param key 字典key
     * @return Map<String, Dict> key值子集对应的字典数据map集合
     */
    public static Map<String, Dict> subMap(String key) {
        return buildMap(subset(key));
    }

    /**
     * 根据字典key，获取其对应的子集，isEnabled参数用来标记是否只取有效的数据，并封装成map对象
     *
     * @param key       字典key
     * @param isEnabled 标记是否只获取有效的数据
     * @return Map<String, Dict> key值子集对应的字典数据map集合
     */
    public static Map<String, Dict> subMap(String key, boolean isEnabled) {
        return buildMap(subset(key, isEnabled));
    }

    /**
     * 给实体对象赋值数据字典的值，接收一个实体对象。
     * <p>
     * 该方法会根据对象类属性字段上的{@link DictField}注解信息，取得对应的数据字典值后，赋给注解对应的字段
     * <p>
     *
     * @param bean 需要赋值数据字典值得实体对象。
     * @param <T>  实体类泛型标识
     */
    public static <T extends Serializable> void set(T bean) {
        if (isNull(bean)) {
            return;
        }
        //缓存获取属性集合
        List<DictAttribute> attributes = DictAttributeCache.getInstance().get(bean.getClass());
        if (isEmpty(attributes)) {
            return;
        }
        setDictProperty(bean, attributes);
    }

    /**
     * 给实体对象集合赋值数据字典的值，接收一个实体对象集合。
     * <p>
     * 该方法会根据对象类属性字段上的{@link DictField}注解信息，取得对应的
     * 数据字典值后，赋给注解对应的字段
     * <p>
     *
     * @param beans 需要赋值的对象集合
     * @param <T>   实体类泛型标识
     */
    public static <T extends Serializable> void set(Collection<T> beans) {
        if (isEmpty(beans)) {
            return;
        }
        //缓存获取属性集合
        List<DictAttribute> attributes = DictAttributeCache.getInstance().get(beans.iterator().next().getClass());
        if (isEmpty(attributes)) {
            return;
        }
        //为实体对象集合赋值数据字典值
        beans.forEach(bean -> setDictProperty(bean, attributes));
    }

    /**
     * 获取RedisTemplate的BoundHashOperations对象
     * <p>
     * 如果key为空，则获取所有根级字典的集合的hash对象，否则获取对应父级key值的hash对象
     *
     * @return BoundHashOperations<String, String, Dict>
     */
    public BoundHashOperations<String, String, Dict> getHash() {
        return getRepertory().getHash();
    }

    private static Map<String, Dict> buildMap(List<Dict> list) {
        return list.stream().collect(Collectors.toMap(Dict::getKey, dict -> dict));
    }

    /**
     * 给实体对象设置数据字典值
     *
     * @param bean       实体对象
     * @param attributes 实体对象映射的字典属性集合
     * @param <T>        实体对象泛型
     */
    private static <T extends Serializable> void setDictProperty(T bean, List<DictAttribute> attributes) {
        attributes.forEach(attribute -> {
            //获取字典的值
            Object value = getPropertyValue(attribute.buildKey(bean), attribute.getProperty());
            if (isNotNull(value) && isNotNull(attribute.getSetter())) {
                executeSetter(bean, attribute.getSetter(), value);
            }
        });
    }

    /**
     * 用来缓存字典的工具对象
     */
    private static DictRepertory repertory;

    private static DictRepertory getRepertory() {
        if (isNull(repertory)) {
            repertory = SpringUtil.getBean(DictRepertory.class);
            Validate.notNull(repertory, "Cannot get DictRepertory object from spring container!");
        }
        return repertory;
    }
}
