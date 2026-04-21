package com.uneed.common.annotation.param;

import com.uneed.common.annotation.enums.Keyword;
import com.uneed.common.annotation.enums.NestedType;

import java.lang.annotation.*;

/**
 * 条件标识符
 *
 * @author anding.huang@u-need.cn
 * @date 2019/12/15
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Condition {

    /**
     * 条件关键字，默认为Keyword.EQ
     * <p>
     * {@link Keyword}
     */
    Keyword value() default Keyword.EQ;

    /**
     * 映射的字段名称
     *
     * @return String[] 字段名称数组
     */
    String[] fields() default {};

    /**
     * 嵌套条件，用来标记
     *
     * @return {@link Keyword}枚举
     * @since 1.1.0，支持嵌套的多个条件的查询条件，如：(name like '%val%' or code like '%val%')
     */
    NestedType nestedType() default NestedType.NON;
}
