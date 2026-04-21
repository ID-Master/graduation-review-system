package com.uneed.common.annotation.dict;

import java.lang.annotation.*;

/**
 * 行政区域字段标识
 *
 * @author anding.huang@u-need.cn
 * @date 2019/12/16
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AreaField {

    /**
     * 指定行政区域编号
     */
    String value() default "";

    /**
     * 指定是否显示行政区域全链名称
     */
    boolean full() default false;

    /**
     * 指定显示行政区域全链名称时，中间的间隔符号，默认为""
     */
    String space() default "";
}
