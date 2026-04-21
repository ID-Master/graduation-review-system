package com.uneed.common.core.convert.impl;

import com.uneed.common.core.convert.AbstractConverter;
import com.uneed.common.core.lang.BooleanUtil;

/**
 * 波尔转换器
 * <p>
 * 参考:https://gitee.com/loolly/hutool
 *
 * @author diablo
 * @date 2018/1/16
 * @since 1.0.0
 */
public class BooleanConverter extends AbstractConverter<Boolean> {
    private static final long serialVersionUID = 1L;

    @Override
    protected Boolean convertInternal(Object value) {
        if (boolean.class == value.getClass()) {
            return (boolean) value;
        }
        String valueStr = convertToStr(value);
        return BooleanUtil.toBoolean(valueStr);
    }

}
