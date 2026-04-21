package com.uneed.common.core.convert.impl;

import com.uneed.common.core.convert.AbstractConverter;

/**
 * 泛型枚举转换器
 * <p>
 * 参考:https://gitee.com/loolly/hutool
 *
 * @param <E> 枚举类类型
 * @author diablo
 * @date 2018/1/16
 * @since 1.0.0
 */
public class GenericEnumConverter<E extends Enum<E>> extends AbstractConverter<E> {
    private static final long serialVersionUID = 1L;

    private Class<E> enumClass;

    /**
     * 构造
     *
     * @param enumClass 转换成的目标Enum类
     */
    public GenericEnumConverter(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    protected E convertInternal(Object value) {
        return Enum.valueOf(enumClass, convertToStr(value));
    }

    @Override
    public Class<E> getTargetType() {
        return this.enumClass;
    }
}
