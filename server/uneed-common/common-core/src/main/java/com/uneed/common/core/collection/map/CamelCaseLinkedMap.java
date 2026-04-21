package com.uneed.common.core.collection.map;

import java.util.HashMap;
import java.util.Map;

/**
 * 驼峰Key风格的LinkedHashMap<br>
 * 对KEY转换为驼峰，get("int_value")和get("intValue")获得的值相同，put进入的值也会被覆盖
 * <p>
 * 参考:https://gitee.com/loolly/hutool
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author diablo
 * @date 2018/1/16
 * @since 1.0.0
 */
public class CamelCaseLinkedMap<K, V> extends CamelCaseMap<K, V> {

    private static final long serialVersionUID = -7209550389355992743L;

    // ------------------------------------------------------------------------- Constructor start

    /**
     * 构造
     */
    public CamelCaseLinkedMap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * 构造
     *
     * @param initialCapacity 初始大小
     */
    public CamelCaseLinkedMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * 构造
     *
     * @param initialCapacity 初始大小
     * @param loadFactor      加载因子
     */
    public CamelCaseLinkedMap(int initialCapacity, float loadFactor) {
        super(new HashMap<>(initialCapacity, loadFactor));
    }

    /**
     * 构造
     *
     * @param map Map
     */
    public CamelCaseLinkedMap(Map<? extends K, ? extends V> map) {
        this(DEFAULT_LOAD_FACTOR, map);
    }

    /**
     * 构造
     *
     * @param loadFactor 加载因子
     * @param map        Map
     */
    public CamelCaseLinkedMap(float loadFactor, Map<? extends K, ? extends V> map) {
        this(map.size(), loadFactor);
        this.putAll(map);
    }
    // ------------------------------------------------------------------------- Constructor end
}
