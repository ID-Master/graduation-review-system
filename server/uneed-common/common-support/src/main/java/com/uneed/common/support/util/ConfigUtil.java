package com.uneed.common.support.util;

import com.uneed.common.core.lang.ObjectUtil;
import com.uneed.common.core.lang.Validate;
import com.uneed.common.dict.api.ConfigRepertory;
import org.springframework.data.redis.core.BoundHashOperations;

import static com.uneed.common.core.lang.ObjectUtil.isNotNull;
import static com.uneed.common.core.lang.ObjectUtil.nullToDefault;

/**
 * 系统配置工具类
 *
 * @author diablo
 * @date 2019/12/18
 */
public class ConfigUtil {

    /**
     * 私有化构造函数
     */
    private ConfigUtil() {
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * 通过配置编号获取配置
     *
     * @param key 配置编号
     * @return String 配置值
     */
    public static String get(String key) {
        return nullToDefault(getRepertory().get(key), "");
    }

    /**
     * 通过配置编号获取配置值，会自动将获取的值转换为参数传递的类型，转换失败会抛异常
     *
     * @param key  配置编号
     * @param type 需要转换的值类型
     * @param <T>  值类型泛型参数
     * @return T 转换后的值
     * @since 1.1.0
     */
    public static <T> T get(String key, Class<T> type) {
        return ObjectUtil.convert(get(key), type);
    }

    /**
     * 通过配置编号获取配置值，会自动将获取的值转换默认值的类型，转换失败则返回传递的默认值
     *
     * @param key          配置编号
     * @param defaultValue 默认值
     * @param <T>          默认值泛型参数
     * @return T 转换后的值
     * @since 1.1.0
     */
    public static <T> T get(String key, T defaultValue) {
        return ObjectUtil.convert(get(key), defaultValue);
    }

    /**
     * 获取RedisTemplate的BoundHashOperations对象，系统会为每一个字典集合构建一个hash对象
     *
     * @return BoundHashOperations<String, String, String>
     * @since 1.1.0
     */
    public BoundHashOperations<String, String, String> getHash() {
        return getRepertory().getHash();
    }

    /**
     * 用来缓存系统配置的工具对象
     */
    private static ConfigRepertory repertory;

    /**
     * spring容器中获取系统配置工具对象
     */
    private static ConfigRepertory getRepertory() {
        if (isNotNull(repertory)) {
            return repertory;
        }
        repertory = SpringUtil.getBean(ConfigRepertory.class);
        Validate.notNull(repertory, "Cannot get ConfigRepertory object from spring container!");
        return repertory;
    }
}
