package com.uneed.common.annotation.dict;

import com.uneed.common.annotation.enums.DictProperty;

import java.lang.annotation.*;

/**
 * 字典字段标识
 *
 * @author anding.huang@u-need.cn
 * @date 2019/12/16
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DictField {

    /**
     * 指定字典key值的来源字段名称，若为空，会取被注解的字典值作为字典的key
     */
    String value() default "";

    /**
     * 指定字典的key值，若key值存在时，会忽略映射取值
     */
    String key() default "";

    /**
     * 指定字典取值的属性
     * <p>
     * {@link DictProperty}
     */
    DictProperty property() default DictProperty.NAME;

    /**
     * 用来指定字典的根级编码，若为空，则取根字典
     */
    String root() default "";
}
