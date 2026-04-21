package com.uneed.common.core.bean.closure;

import com.uneed.common.core.bean.PropertyUtil;
import com.uneed.common.core.lang.ObjectUtil;
import com.uneed.common.core.lang.Validate;
import org.apache.commons.collections4.Closure;

/**
 * @author diablo
 * @date 2017/12/26
 */
public class BeanPropertyChangeClosure<T> implements Closure<T> {

    /**
     * 属性名
     */
    private final String property;

    /**
     * 属性值
     */
    private final Object value;

    public BeanPropertyChangeClosure(String property, Object value) {
        Validate.notEmpty(property, "property can't be blank!");
        this.property = property;
        this.value = value;
    }

    @Override
    public void execute(T bean) {
        if (ObjectUtil.isEmpty(bean)) {
            return;
        }
        PropertyUtil.setProperty(bean, property, value);
    }
}
