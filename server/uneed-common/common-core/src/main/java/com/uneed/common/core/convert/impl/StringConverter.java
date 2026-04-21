package com.uneed.common.core.convert.impl;

import com.uneed.common.core.convert.AbstractConverter;

/**
 * 字符串转换器
 * <p>
 * 参考:https://gitee.com/loolly/hutool
 *
 * @author diablo
 * @date 2018/1/16
 * @since 1.0.0
 */
public class StringConverter extends AbstractConverter<String> {
    private static final long serialVersionUID = 1L;

    @Override
    protected String convertInternal(Object value) {
        return convertToStr(value);
    }

}
