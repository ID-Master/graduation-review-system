package com.uneed.common.core.convert.impl;

import com.uneed.common.core.convert.AbstractConverter;
import com.uneed.common.core.lang.BooleanUtil;
import com.uneed.common.core.lang.StringUtil;

/**
 * 字符转换器
 * <p>
 * 参考:https://gitee.com/loolly/hutool
 *
 * @author diablo
 * @date 2018/1/16
 * @since 1.0.0
 */
public class CharacterConverter extends AbstractConverter<Character> {
    private static final long serialVersionUID = 1L;

    @Override
    protected Character convertInternal(Object value) {
        if (char.class == value.getClass()) {
            return (char) value;
        } else if (value instanceof Boolean) {
            return BooleanUtil.toCharacter((Boolean) value);
        } else if (boolean.class == value.getClass()) {
            return BooleanUtil.toCharacter((boolean) value);
        } else {
            final String valueStr = convertToStr(value);
            if (StringUtil.isNotEmpty(valueStr)) {
                return valueStr.charAt(0);
            }
        }
        return null;
    }

}
