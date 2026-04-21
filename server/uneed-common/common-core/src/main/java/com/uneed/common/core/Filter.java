package com.uneed.common.core;

/**
 * 过滤器接口
 * <p>
 * 参考:https://gitee.com/loolly/hutool
 *
 * @param <T> 被过滤对象类型
 * @author diablo
 * @date 2019/9/7
 * @since 1.0.0
 */
public interface Filter<T> {
    /**
     * 是否接受对象
     *
     * @param t 检查的对象
     * @return 是否接受对象
     */
    boolean accept(T t);
}