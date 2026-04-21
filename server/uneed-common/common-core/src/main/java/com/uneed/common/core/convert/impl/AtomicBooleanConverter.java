package com.uneed.common.core.convert.impl;

import com.uneed.common.core.convert.AbstractConverter;
import com.uneed.common.core.lang.BooleanUtil;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * {@link AtomicBoolean}转换器
 * <p>
 * 参考:https://gitee.com/loolly/hutool
 *
 * @author diablo
 * @date 2018/1/16
 * @since 1.0.0
 */
public class AtomicBooleanConverter extends AbstractConverter<AtomicBoolean> {
    private static final long serialVersionUID = 1L;

    @Override
    protected AtomicBoolean convertInternal(Object value) {
        if (boolean.class == value.getClass()) {
            return new AtomicBoolean((boolean) value);
        }
        if (value instanceof Boolean) {
            return new AtomicBoolean((Boolean) value);
        }
        final String valueStr = convertToStr(value);
        return new AtomicBoolean(BooleanUtil.toBoolean(valueStr));
    }

}
