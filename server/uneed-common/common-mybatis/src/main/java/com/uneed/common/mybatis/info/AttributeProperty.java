package com.uneed.common.mybatis.info;

import com.uneed.common.annotation.enums.Keyword;
import com.uneed.common.annotation.enums.NestedType;
import com.uneed.common.annotation.param.Condition;
import com.uneed.common.core.collection.Lists;
import com.uneed.common.core.exception.unchecked.ValidateException;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.uneed.common.core.lang.ObjectUtil.*;

/**
 * 用来封装条件类的属性信息
 *
 * @author diablo
 * @date 2020/4/17
 */
@Data
public class AttributeProperty implements Serializable {

    private static final long serialVersionUID = -3180273985278685478L;

    /**
     * 条件annotation的关键字 {@link Keyword}
     */
    private Keyword keyword;

    /**
     * 条件annotation的字段名称数组
     */
    private String[] fields;

    /**
     * 条件annotation的嵌套类型
     */
    private NestedType nestedType;

    /**
     * 条件属性的字段名称
     */
    private String name;

    /**
     * 条件属性的字段类型
     */
    private Class<?> type;

    /**
     * 条件属性字段的泛型类型
     */
    private Type genericType;

    /**
     * 条件属性的取值方法
     */
    private Method getter;

    /**
     * 条件属性映射的数据库列信息
     */
    private AttributeColumn column;

    /**
     * 条件属性映射的嵌套数据库列信息集合
     */
    private List<AttributeColumn> nestedColumns;

    /**
     * 无参构造函数
     */
    public AttributeProperty() {
        this(null);
    }

    /**
     * Condition条件的构造函数
     */
    public AttributeProperty(Condition annotation) {
        this.keyword = isNotNull(annotation) ? annotation.value() : Keyword.EQ;
        this.fields = isNotNull(annotation) ? annotation.fields() : new String[]{};
        this.nestedType = isNotNull(annotation) ? annotation.nestedType() : NestedType.NON;
    }

    /**
     * 根据条件的annotation与字段获取映射的字段名称集合
     * <p>
     * 1. 一定会添加当前属性的字段名称
     * <p>
     * 2. 如果是忽略嵌套属性的情况下，会添加annotation映射的fields值
     * <p>
     * 3. 如果不忽略嵌套属性，那么在否开启嵌套属性的情况下，会添加annotation映射的fields值
     *
     * @param ignoreNested 是否忽略嵌套属性
     * @return Set<String> 字段名称集合
     */
    public Set<String> fieldNames(boolean ignoreNested) {
        if (isEmpty(name)) {
            throw new ValidateException("条件属性name值为空，请先初始化条件属性信息！");
        }
        Set<String> set = Lists.newHashSet(name);
        //如果不是嵌套属性，或者在需要忽略嵌套属性的情况下
        if (equal(nestedType, NestedType.NON) || ignoreNested) {
            Collections.addAll(set, fields);
        }
        return set;
    }
}
