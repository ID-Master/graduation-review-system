package com.uneed.common.core.bean.transformer;

import com.uneed.common.core.convert.Convert;
import com.uneed.common.core.lang.Validate;
import org.apache.commons.collections4.Transformer;

import java.io.Serializable;

/**
 * 简单对象转成指定类型的转换器
 *
 * @author diablo
 * @date 17/12/27
 */
public class SimpleClassTransformer<T, V> implements Transformer<T, V>, Serializable {

    private static final long serialVersionUID = -5212215989519836668L;
    /**
     * 需要被转成的目标类型.
     */
    private final Class<V> type;

    public SimpleClassTransformer(Class<V> type) {
        Validate.notNull(type, "type can't be null!");
        this.type = type;
    }

    @Override
    public V transform(final T t) {
        return Convert.convert(type, t);
    }
}
