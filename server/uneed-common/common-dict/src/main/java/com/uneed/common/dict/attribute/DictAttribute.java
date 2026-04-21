package com.uneed.common.dict.attribute;

import com.uneed.common.annotation.enums.DictProperty;
import com.uneed.common.core.lang.ObjectUtil;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;
import java.lang.reflect.Method;

import static com.uneed.common.core.bean.BeanUtil.executeGetter;
import static com.uneed.common.core.lang.ObjectUtil.isNotEmpty;
import static com.uneed.common.core.lang.ObjectUtil.isNotNull;

/**
 * 数据字典属性对象
 *
 * @author diablo
 * @date 2019/12/17
 */
@Data
public class DictAttribute implements Serializable {

    private static final long serialVersionUID = 5102748669015871084L;

    /**
     * 字典注解映射的字段名称
     */
    private String fileName;

    /**
     * 字典注解上配置的字典key值来源字段名称
     */
    private String keyField;

    /**
     * 字典注解上配置的字典key值
     */
    private String key;

    /**
     * 字典注解上配置的获取字典属性类型
     */
    private DictProperty property;

    /**
     * 字典的根级编码
     */
    private String root;

    /**
     * 用来取字典key值的类方法
     */
    private Method getter;

    /**
     * 用来赋值字典的方法
     */
    private Method setter;

    /**
     * 构建数据字典的key值
     *
     * @param bean 数据对象
     * @param <T>  数据对象泛型
     * @return String 数据字典key
     */
    public <T extends Serializable> String buildKey(@NonNull T bean) {
        // 优先取注解配置的key值
        if (isNotEmpty(key)) {
            return key;
        }
        // 其次通过获取的getter方法获取key值
        if (isNotNull(getGetter())) {
            return ObjectUtil.toString(executeGetter(bean, getGetter()));
        }
        return null;
    }
}
