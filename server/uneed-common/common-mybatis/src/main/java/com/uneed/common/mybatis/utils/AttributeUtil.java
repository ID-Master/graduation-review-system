package com.uneed.common.mybatis.utils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.uneed.common.annotation.enums.NestedType;
import com.uneed.common.annotation.param.Condition;
import com.uneed.common.core.bean.BeanUtil;
import com.uneed.common.core.collection.Lists;
import com.uneed.common.core.exception.unchecked.BusinessException;
import com.uneed.common.core.lang.ObjectUtil;
import com.uneed.common.core.lang.Validate;
import com.uneed.common.core.reflect.TypeFunction;
import com.uneed.common.mybatis.cache.GetterCache;
import com.uneed.common.mybatis.cache.UpdateCache;
import com.uneed.common.mybatis.info.AttributeColumn;
import com.uneed.common.mybatis.info.AttributeProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static com.uneed.common.core.lang.ObjectUtil.*;
import static com.uneed.common.mybatis.utils.TableUtil.isSuper;

/**
 * do something in here.
 *
 * @author diablo
 * @date 2020/4/14
 */
@Slf4j
public class AttributeUtil {

    /**
     * 序列化字段名称常量
     */
    private static final String SERIALIZABLE_FIELD_NAME = "serialVersionUID";

    /**
     * 私有化构造函数
     */
    private AttributeUtil() {
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * 构建条件属性集合，接收俩个类型的参数：
     * <p>
     * 1. beanClass，用来封装条件对象的类型，可以是任何POJO对象
     * <p>
     * 2. entityClass，条件对象映射的实体对象类型，必须是数据库映射的实体对象类型
     *
     * @param beanClass   取值的条件类类型
     * @param entityClass 赋值的实体类类型
     * @return List<ConditionAttribute> 条件属性集合
     */
    public static List<AttributeProperty> buildAttributeProperties(Class<?> beanClass, Class<?> entityClass) {
        List<AttributeProperty> list = Lists.newArrayList();
        //获取实体类对应的表信息
        TableInfo info = TableUtil.getTableInfo(entityClass);
        //循环条件条件类型
        Class<?> clazz = beanClass;
        while (clazz != Object.class) {
            //根据条件对象类型，获取所有字段信息，排除serialVersionUID
            List<Field> fields = BeanUtil.getFields(clazz, true, SERIALIZABLE_FIELD_NAME);
            //获取条件属性配置
            fields.stream().map(f -> buildAttributeProperties(buildAttributeConfig(beanClass, f), info)).forEach(list::addAll);
            clazz = clazz.getSuperclass();
        }
        return list;
    }

    /**
     * 构建条件属性配置
     *
     * @param beanClass 条件对象类型
     * @param field     条件对象字段信息
     * @return AttributeProperty 条件属性配置
     */
    public static AttributeProperty buildAttributeConfig(Class<?> beanClass, Field field) {
        //根据条件类对象、字段获取取值方法
        Method getter = GetterCache.getInstance().get(beanClass, field);
        if (isNotNull(getter)) {
            AttributeProperty config = new AttributeProperty(field.getAnnotation(Condition.class));
            config.setName(field.getName());
            config.setType(field.getType());
            config.setGenericType(field.getGenericType());
            config.setGetter(getter);
            return config;
        }
        return null;
    }

    /**
     * 根据条件属性配置、实体映射表信息构建条件属性集合
     *
     * @param config 条件属性配置
     * @param info   实体映射的表信息
     */
    public static List<AttributeProperty> buildAttributeProperties(AttributeProperty config, TableInfo info) {
        List<AttributeProperty> list = Lists.newArrayList();
        if (isNotNull(config)) {
            config.fieldNames(false).stream().map(f -> buildAttributeProperty(f, config, info)).filter(ObjectUtil::isNotNull)
                  .forEach(list::add);
        }
        return list;
    }

    /**
     * 根据实体类类型、字段名称获取映射的条件属性对象
     *
     * @param entityClass 实体类类型
     * @param field       字段名称
     * @return AttributeProperty 条件属性
     */
    public static AttributeProperty getProperty(Class<?> entityClass, String field) {
        return UpdateCache.getInstance().get(entityClass).stream().filter(p -> equalColumn(p.getColumn(), field)).findFirst()
                          .orElse(null);
    }

    /**
     * 根据实体类类型获取主键属性，获取不到会抛异常
     *
     * @param entityClass 实体类类型
     * @return AttributeProperty 主键属性
     */
    public static AttributeProperty getPrimary(Class<?> entityClass) {
        AttributeProperty property = getPrimary(UpdateCache.getInstance().get(entityClass));
        Validate.notNull(property, "error: can not execute. because can not find cache of AttributeProperty for entity!");
        return property;
    }

    /**
     * 从条件属性集合中获取主键属性
     *
     * @param properties 条件属性集合
     * @return AttributeProperty 主键属性
     */
    public static AttributeProperty getPrimary(List<AttributeProperty> properties) {
        return isNotEmpty(properties) ? properties.stream().filter(p -> isPrimaryColumn(p.getColumn())).findFirst().orElse(null)
                : null;
    }

    /**
     * 根据实体类类型获取主键列属性，获取不到会抛异常
     *
     * @param entityClass 实体对象类型
     * @return AttributeColumn 主键列属性
     */
    public static AttributeColumn getPrimaryColumn(Class<?> entityClass) {
        AttributeColumn column = getPrimary(entityClass).getColumn();
        Validate.notNull(column, "error: can not execute. because can not find cache of AttributeColumn for entity!");
        return column;
    }

    /**
     * 根据实体类类型获取主键字段名称，获取不到会抛异常
     *
     * @param entityClass 实体对象类型
     * @return Object 主键字段名称
     */
    public static String getPrimaryFieldName(Class<?> entityClass) {
        return getPrimaryColumn(entityClass).getFieldName();
    }

    /**
     * 从属性集合中获取主键属性值
     *
     * @param entity 实体对象属性
     * @return Object 主键属性值
     */
    public static <T> Long getPrimaryValue(T entity) {
        return getPrimaryValue(entity, getPrimary(entity.getClass()).getGetter());
    }

    /**
     * 从属性集合中获取主键属性值
     *
     * @param entity 实体对象属性
     * @return Object 主键属性值
     */
    public static <T> Long getPrimaryValue(T entity, Method getter) {
        return (Long) BeanUtil.executeGetter(entity, getter);
    }

    /**
     * 判断是否嵌套属性
     *
     * @param type 嵌套类型
     * @return static
     */
    public static boolean isNested(NestedType type) {
        return notEqual(type, NestedType.NON);
    }

    /**
     * 判断是否嵌套或属性
     *
     * @param type 嵌套类型
     * @return static
     */
    public static boolean isNestedOr(NestedType type) {
        return equal(type, NestedType.OR);
    }

    /**
     * 获取@TableField注解值
     * @param fn
     * @param <T>
     * @return
     */
    public static <T> String getTableFieldValue(SFunction<T, ?> fn) {
        Field field = TypeFunction.getLambdaField(fn);
        // 从field取出字段名，可以根据实际情况调整
        TableField tableField = field.getAnnotation(TableField.class);
        if (tableField == null) {
            log.error("获取注解@TableField失败");
            throw new BusinessException("没有获取到数据");
        }else if (!tableField.exist()){
            log.warn("注解@TableField, exist = false");
            return null;
        }
        return tableField.value();
    }

    /**
     * 获取属性
     * @param fn
     * @param <T>
     * @return
     */
    public static <T> Field getTableField(SFunction<T, ?> fn) {
        Field field = TypeFunction.getLambdaField(fn);
        return field;
    }

    /////////////////////////////////////////////////////// 私有方法 /////////////////////////////////////////////////////////////

    /**
     * 构建条件属性
     */
    private static AttributeProperty buildAttributeProperty(String field, AttributeProperty config, TableInfo info) {
        //判断是否嵌套属性
        if (isNested(config.getNestedType())) {
            List<AttributeColumn> columns = buildAttributeColumns(config.fieldNames(true), config, info);
            return isEmpty(columns) ? null : buildAttributeProperty(config, p -> p.setNestedColumns(columns));
        }
        //不是嵌套的情况下
        AttributeColumn column = buildAttributeColumn(field, config, info);
        return isNull(column) ? null : buildAttributeProperty(config, p -> p.setColumn(column));
    }

    /**
     * 根据表信息、字段名称集合、属性配置，构建属性列信息集合
     */
    private static List<AttributeColumn> buildAttributeColumns(Set<String> fields, AttributeProperty config, TableInfo info) {
        List<AttributeColumn> list = Lists.newArrayList();
        if (isNotEmpty(fields)) {
            fields.stream().map(f -> buildAttributeColumn(f, config, info)).filter(ObjectUtil::isNotNull).forEach(list::add);
        }
        return list;
    }

    /**
     * 根据表信息、属性名称、属性配置，构建属性列信息
     */
    private static AttributeColumn buildAttributeColumn(String field, AttributeProperty config, TableInfo info) {
        //根据字段名称获取映射的列名称
        AttributeColumn column = buildAttributeColumn(field, info);
        if (isNull(column)) {
            return null;
        }
        //判断条件属性字段类型是否和数据库列映射的实体类字段类型一致
        if (equal(config.getType(), column.getFieldType())) {
            return column;
        }
        //类型不一致的情况，判断字段类型是否为数组，并且数组的组件类型是否和目标实体类型一致
        if (config.getType().isArray() && equal(config.getType().getComponentType(), column.getFieldType())) {
            return column;
        }
        //类型不一致的情况，判断字段类型的泛型，是否和目标实体类型一致
        Class<?> parameterizedType = BeanUtil.getParameterizedType(config.getGenericType(), 0);
        return equal(parameterizedType, column.getFieldType()) ? column : null;
    }

    /**
     * 根据表信息、字段名称，构建属性列信息
     */
    private static AttributeColumn buildAttributeColumn(String field, TableInfo info) {
        //根据字段名称获取字段信息
        TableFieldInfo fInfo = TableUtil.getTableFieldInfo(info, field);
        if (isNotNull(fInfo)) {
            return new AttributeColumn(fInfo.getColumn(), fInfo.getProperty(), fInfo.getPropertyType(), false, isSuper(field));
        }
        //判断是否主键
        if (TableUtil.isPrimary(field, info)) {
            return new AttributeColumn(info.getKeyColumn(), info.getKeyProperty(), info.getKeyType(), true, isSuper(field));
        }
        return null;
    }

    /**
     * 根据属性配置、消费函数，获取属性信息
     */
    private static AttributeProperty buildAttributeProperty(AttributeProperty config, Consumer<AttributeProperty> consumer) {
        AttributeProperty property = new AttributeProperty();
        BeanUtils.copyProperties(config, property);
        consumer.accept(property);
        return property;
    }

    /**
     * 判断字段是否与属性列一致
     */
    private static boolean equalColumn(AttributeColumn column, String field) {
        return isNotNull(column) && (equal(column.getFieldName(), field) || equal(column.getName(), field));
    }

    /**
     * 判断是否主键列
     */
    private static boolean isPrimaryColumn(AttributeColumn column) {
        return isNotNull(column) && column.getIsPrimary();
    }
}
