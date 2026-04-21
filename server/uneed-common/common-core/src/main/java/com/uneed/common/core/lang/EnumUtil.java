package com.uneed.common.core.lang;

import com.uneed.common.core.bean.PropertyUtil;

/**
 * 枚举处理的工具类。
 * 该类提供了根据name、ordinal、自定义属性来获取枚举元素的常用方法。
 *
 * @author diablo
 * @date 2018/1/16
 * @since 1.0.0
 */
public final class EnumUtil {

    /**
     * 私有化构造函数，禁止实例化该类
     */
    private EnumUtil() {
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * 根据枚举的name值获取枚举元素，并且会忽略name的大小写，若枚举中不存在name对应的元素，返回null。
     * <p>
     * 同样，使用{@link Enum#valueOf(Class, String)}也可以获取到枚举元素。
     * 区别就是，{@link Enum#valueOf(Class, String)}不会忽略大小写，并且在枚举中找不到name对应的元素，会抛{@link IllegalArgumentException}异常。
     *
     * @param clazz 枚举的class类型
     * @param name  枚举name值
     * @param <E>   用来约束class类型必须为枚举的泛型
     * @return name对应的枚举元素
     */
    public static <E extends Enum<E>> E getEnum(Class<E> clazz, String name) {
        for (E e : clazz.getEnumConstants()) {
            if (e.name().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }

    /**
     * 根据枚举的ordinal值获取枚举元素，若枚举中不存在ordinal对应的元素，返回null。
     *
     * @param clazz   枚举的class类型
     * @param ordinal 枚举ordinal值
     * @param <E>     用来约束class类型必须为枚举的泛型
     * @return ordinal对应的枚举元素
     */
    public static <E extends Enum<E>> E getEnum(Class<E> clazz, int ordinal) {
        for (E e : clazz.getEnumConstants()) {
            if (e.ordinal() == ordinal) {
                return e;
            }
        }
        return null;
    }

    /**
     * 根据枚举的定制属性与定制值获取枚举元素。若对应的属性不存在，会抛出{@link NoSuchMethodException}异常，若对应的值不存在，会返回null。
     *
     * @param clazz    枚举的class类型
     * @param property 枚举的定制属性
     * @param value    枚举定制的值
     * @param <E>      用来约束class类型必须为枚举的泛型
     * @return 定制属性与定制值对应的枚举元素
     */
    public static <E extends Enum<E>> E getEnum(Class<E> clazz, String property, Object value) {
        for (E e : clazz.getEnumConstants()) {
            Object object = PropertyUtil.getProperty(e, property);
            if (ObjectUtil.equal(object, value)) {
                return e;
            }
        }
        return null;
    }

    /**
     * 根据枚举的定制属性与定制值获取枚举元素。若对应的属性不存在，会抛出{@link NoSuchMethodException}异常，若对应的值不存在，会返回默认值。
     *
     * @param clazz        枚举的class类型
     * @param property     枚举的定制属性
     * @param value        枚举定制的值
     * @param defaultValue 枚举默认值
     * @param <E>          用来约束class类型必须为枚举的泛型
     * @return 定制属性与定制值对应的枚举元素
     */
    public static <E extends Enum<E>> E getEnum(Class<E> clazz, String property, Object value, E defaultValue) {
        E e = getEnum(clazz, property, value);
        return ObjectUtil.nullToDefault(e, defaultValue);
    }
}
