package com.uneed.common.core.convert.impl;

import com.uneed.common.core.convert.AbstractConverter;
import com.uneed.common.core.lang.CharsetUtil;

import java.nio.charset.Charset;

/**
 * 编码对象转换器
 * <p>
 * 参考:https://gitee.com/loolly/hutool
 *
 * @author diablo
 * @date 2018/1/16
 * @since 1.0.0
 */
public class CharsetConverter extends AbstractConverter<Charset> {
    private static final long serialVersionUID = 1L;

    @Override
    protected Charset convertInternal(Object value) {
        return CharsetUtil.charset(convertToStr(value));
    }

}
