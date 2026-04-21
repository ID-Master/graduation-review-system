package com.uneed.common.support.convert;

import com.uneed.common.core.bean.BeanUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.uneed.common.core.lang.ObjectUtil.isNull;

/**
 * 数据对象转换器工具.
 *
 * @author diablo
 * @date 2020/4/26
 */
@Slf4j
public class Converters {

    /**
     * 用来缓存数据对象转换器
     */
    @SuppressWarnings("all")
    private static final Map<String, AbstractModelConverter> CACHE_CONVERTER = new ConcurrentHashMap<>();

    /**
     * 私有化构造函数
     */
    private Converters() {
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * 根据转换器类型参数，获取一个对象数据转换器，会做缓存处理
     *
     * @param clazz 转换器类型
     * @param <T>   泛型参数
     * @return T extends AbstractModelConverter 转换器
     */
    @SuppressWarnings("all")
    public static <T extends AbstractModelConverter> T get(@NonNull Class<T> clazz) {
        if (isNull(CACHE_CONVERTER.get(clazz.getName()))) {
            CACHE_CONVERTER.put(clazz.getName(), BeanUtil.newInstance(clazz));
        }
        return (T) CACHE_CONVERTER.get(clazz.getName());
    }
}
