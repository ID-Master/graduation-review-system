package com.uneed.common.mybatis.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.uneed.common.annotation.enums.Keyword;
import com.uneed.common.annotation.enums.NestedType;
import com.uneed.common.core.bean.BeanUtil;
import com.uneed.common.core.collection.ArrayUtil;
import com.uneed.common.core.exception.unchecked.UncheckedException;
import com.uneed.common.mybatis.cache.QueryCache;
import com.uneed.common.mybatis.cache.UpdateCache;
import com.uneed.common.mybatis.handler.SuperModelMetaObjectHandler;
import com.uneed.common.mybatis.info.AttributeColumn;
import com.uneed.common.mybatis.info.AttributeProperty;
import com.uneed.common.mybatis.page.PageSearch;
import com.uneed.common.mybatis.page.Sort;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.uneed.common.core.collection.CollectionUtil.getPropertyList;
import static com.uneed.common.core.lang.ObjectUtil.*;
import static com.uneed.common.core.lang.StringUtil.camelToUnderline;
import static com.uneed.common.core.text.StringFormatter.format;
import static com.uneed.common.mybatis.utils.AttributeUtil.*;

/**
 * 条件工具类.
 * <p>
 * 1. 可以用来构建{@link Page}、{@link QueryWrapper}、{@link UpdateWrapper}
 * <p>
 * 2. 同样也可以为Wrapper设置update值、query条件、order条件等
 *
 * @author diablo
 * @date 2020/4/15
 */
@Slf4j
public class Conditions {

    /**
     * in查询的元素最大数量
     */
    public static final int QUERY_IN_MAX_SIZE = 1000;

    /**
     * AttributeColumn中的name属性名称
     */
    public static final String ATTRIBUTE_COLUMN_FIELD_NAME = "name";

    /**
     * 私有化构造函数
     */
    private Conditions() {
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * 根据分页条件参数，构建mybatis-plus的{@link Page}分页对象
     * <p>
     * 1. 如果sort属性不为空，会根据排序参数{@link Sort}的field、asc属性构建排序条件
     * <p>
     * 2. 注意：映射的排序列名称是{@link Sort#getField()}转下划线后的结果，会有与映射列不匹配的风险，推荐使用{@link Conditions#page(PageSearch, Class)}方法
     *
     * @param search 分页参数
     * @return Page<T> mybatis-plus的{@link Page}分页对象
     */
    public static <T> Page<T> page(PageSearch<?> search) {
        return page(search, null);
    }

    /**
     * 根据分页条件参数、实体类类型，构建mybatis-plus的{@link Page}分页对象
     * <p>
     * 1. 如果sort属性不为空，会根据排序参数{@link Sort}的field、asc属性构建排序条件
     * <p>
     * 2. 当前方法会根据实体类类型去匹配{@link Sort#getField()}映射的字段列，匹配不成功，会直接过滤排序条件
     *
     * @param search      分页参数
     * @param entityClass 实体类类型
     * @return Page<T> mybatis-plus的{@link Page}分页对象
     */
    public static <T> Page<T> page(@NonNull PageSearch<?> search, Class<T> entityClass) {
        Page<T> page = new Page<>(search.getCurrent(), search.getSize());
        //构建分页信息
        nullToDefault(search.getSorts(), new ArrayList<Sort>()).forEach(sort -> {
            String column = getSortColumn(sort.getField(), entityClass);
            if (isNotEmpty(column)) {
                page.addOrder(sort.isAsc() ? OrderItem.asc(column) : OrderItem.desc(column));
            }
        });
        return page;
    }

    /**
     * 将实体类包装成{@link UpdateWrapper}方法
     * <p>
     * 1. 会强制封装修改的条件为数据的id，id值为空会抛异常
     * <p>
     * 2. 该方法会解析实体对象entity的参数，将字段对应的值包装成wrapper的set的值，若对应的值为null，则wrapper会设置对应的数据库列为null
     * <p>
     * 3. 如果公共字段为空，将会交给自动填充对象{@link SuperModelMetaObjectHandler}来处理，不受fields限制
     * <p>
     * 4. 参数fields是用来限制需要给UpdateWrapper赋值的指定字段集合
     * <p>
     *
     * @param entity 实体类对象
     * @param fields 指定需要赋值的字段集合
     * @param <T>    实体类泛型参数
     * @return UpdateWrapper
     */
    public static <T> UpdateWrapper<T> updateWrapper(@NonNull T entity, String... fields) {
        //构建UpdateWrapper对象
        UpdateWrapper<T> wrapper = new UpdateWrapper<>();
        //避免mybatis-plus底层会根据实体类再构造条件的情况，这里会构造一个新的实体对象
        wrapper.setEntity(BeanUtil.newInstance(getEntityClass(entity)));
        //缓存中获取属性集合
        List<AttributeProperty> properties = UpdateCache.getInstance().get(entity.getClass());
        //获取主键，若不能
        AttributeProperty primary = AttributeUtil.getPrimary(properties);
        //设置主键查询条件
        setKeyCondition(wrapper, entity, primary);
        //UpdateWrapper赋值
        properties.stream().filter(p -> inSetFilters(p, primary, fields)).forEach(p -> {
            AttributeColumn column = p.getColumn();
            setValue(wrapper, column.getName(), BeanUtil.executeGetter(entity, p.getGetter()), column.getIsSuper());
        });
        return wrapper;
    }

    /**
     * 设置主键条件
     *
     * @param wrapper UpdateWrapper
     * @param entity  实体对象
     * @param <T>     泛型参数
     */
    public static <T> void setKeyCondition(@NonNull UpdateWrapper<T> wrapper, @NonNull T entity) {
        setKeyCondition(wrapper, entity, AttributeUtil.getPrimary(entity.getClass()));
    }

    /**
     * 设置UpdateWrapper需要修改的值信息
     * <p>
     * 注意：映射的查询列名称是field名称转下划线后的结果，会有与映射列不匹配的风险
     *
     * @param wrapper UpdateWrapper
     * @param field   字段名称，也可以是映射的数据库列名称
     * @param value   值信息
     * @param <T>     泛型参数
     */
    public static <T> void setValue(@NonNull UpdateWrapper<T> wrapper, String field, Object value) {
        setValue(wrapper, camelToUnderline(field), value, TableUtil.isSuper(field));
    }

    /**
     * 根据分页参数获取{@link QueryWrapper}对象
     * <p>
     * 1. 会获取{@link PageSearch#getCondition()}的条件对象，如果条件对象为空，直接返回一个empty的wrapper对象
     * <p>
     * 2. 否则，就会调用{@link this#queryWrapper(Object, Class, String...)}来构造queryWrapper对象
     *
     * @param search      分页参数
     * @param entityClass 映射的实体类对象类型
     * @param fields      指定需要查询的字段集合
     * @param <T>         实体类泛型参数
     * @return QueryWrapper
     */
    public static <T> QueryWrapper<T> queryWrapper(@NonNull PageSearch<?> search, Class<T> entityClass, String... fields) {
        return isNotNull(search.getCondition()) ? queryWrapper(search.getCondition(), entityClass, fields) : Wrappers.query();
    }

    /**
     * 将javaBean包装成{@link QueryWrapper}方法
     * <p>
     * 1. 会获取javaBean字段的Condition配置，并封装成条件属性
     * <p>
     * 2. 会解析entityClass映射的字段，对不存在的字段进行过滤
     * <p>
     * 3. 支持嵌套条件，如：and (name like '%val%' or code like '%val%')
     * <p>
     * 4. 参数fields用来限制需要给QueryWrapper设置条件的指定字段集合
     *
     * @param bean        javaBean对象
     * @param entityClass 映射的实体类对象类型
     * @param fields      指定需要查询的字段集合
     * @param <T>         实体类泛型参数
     * @return QueryWrapper
     */
    public static <T> QueryWrapper<T> queryWrapper(@NonNull Object bean, Class<T> entityClass, String... fields) {
        //构建QueryWrapper对象
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        //避免mybatis-plus底层会根据实体类再构造条件的情况，这里会构造一个新的实体对象
        wrapper.setEntity(BeanUtil.newInstance(entityClass));
        //缓存中获取查询条件属性集合
        List<AttributeProperty> properties = QueryCache.getInstance().get(bean.getClass(), entityClass);
        properties.forEach(p -> {
            Object value = BeanUtil.executeGetter(bean, p.getGetter());
            if (isNested(p.getNestedType())) {
                List<String> columns = getPropertyList(p.getNestedColumns(), ATTRIBUTE_COLUMN_FIELD_NAME, true);
                setColumnNestedCondition(wrapper, p.getKeyword(), columns, value, p.getNestedType());
                return;
            }
            if (inFilters(p.getColumn(), fields)) {
                setColumnCondition(wrapper, p.getKeyword(), p.getColumn().getName(), value);
            }
        });
        return wrapper;
    }

    /**
     * 设置嵌套的查询条件
     *
     * @param wrapper    QueryWrapper 对象
     * @param keyword    条件关键字
     * @param fields     字段名称集合，也可以是映射的数据库列名称集合
     * @param value      需要设置的条件值
     * @param nestedType 嵌套类型
     * @param <T>        实体类泛型参数
     */
    public static <T> void setNestedCondition(@NonNull QueryWrapper<T> wrapper, Keyword keyword, List<String> fields,
                                              Object value, NestedType nestedType) {
        setColumnNestedCondition(wrapper, keyword, camelToUnderline(fields), value, nestedType);
    }

    /**
     * 设置查询条件
     *
     * @param wrapper QueryWrapper 对象
     * @param keyword 条件关键字
     * @param field   字段名称，也可以是映射的数据库列名称
     * @param value   需要设置的条件值
     * @param <T>     实体类泛型参数
     */
    public static <T> void setCondition(@NonNull QueryWrapper<T> wrapper, Keyword keyword, String field, Object value) {
        setColumnCondition(wrapper, keyword, camelToUnderline(field), value);
    }

    /**
     * 设置排序条件
     *
     * @param wrapper QueryWrapper 对象
     * @param sorts   排序集合
     * @param <T>     实体类泛型参数
     */
    public static <T> void setSorting(QueryWrapper<T> wrapper, Collection<Sort> sorts) {
        setSorting(wrapper, nullToDefault(sorts, new ArrayList<Sort>()).toArray(new Sort[0]));
    }

    /**
     * 设置排序条件
     *
     * @param wrapper QueryWrapper 对象
     * @param sorts   排序集合
     * @param <T>     实体类泛型参数
     */
    public static <T> void setSorting(QueryWrapper<T> wrapper, Sort... sorts) {
        Arrays.stream(sorts).forEach(sort -> setSorting(wrapper, sort.getField(), sort.isAsc()));
    }

    /**
     * 设置排序条件
     *
     * @param wrapper QueryWrapper 对象
     * @param field   排序字段，也可以是映射的数据库列名称
     * @param isAsc   是否升序
     * @param <T>     实体类泛型参数
     */
    public static <T> void setSorting(@NonNull QueryWrapper<T> wrapper, String field, boolean isAsc) {
        wrapper.orderBy(true, isAsc, camelToUnderline(field));
    }

    /////////////////////////////////////////////////////// 私有方法 /////////////////////////////////////////////////////////////

    /**
     * 根据字段名称，实体类类型获取映射的数据库列
     */
    private static <T> String getSortColumn(String field, Class<T> entityClass) {
        if (isNotNull(entityClass)) {
            AttributeProperty property = AttributeUtil.getProperty(entityClass, field);
            return isNotNull(property) ? property.getColumn().getName() : null;
        }
        return camelToUnderline(field);
    }

    /**
     * 设置主键条件
     */
    private static <T> void setKeyCondition(UpdateWrapper<T> wrapper, T entity, AttributeProperty primary) {
        if (isNull(primary) || isNull(primary.getColumn())) {
            throw new UncheckedException(format("{}执行updateWrapper方法异常，不能获取到主键属性！对象[{}]", Conditions.class, entity.getClass()));
        }
        //获取主键值
        Object value = BeanUtil.executeGetter(entity, primary.getGetter());
        if (isNull(value)) {
            throw new UncheckedException(format("{}执行updateWrapper方法异常，主键值为空！对象[{}]", Conditions.class, entity.getClass()));
        }
        wrapper.eq(primary.getColumn().getName(), value);
    }

    /**
     * 设置UpdateWrapper需要修改的值信息
     */
    private static <T> void setValue(UpdateWrapper<T> wrapper, String column, Object value, boolean isSuper) {
        //过滤列为空的情况
        if (isEmpty(column)) {
            return;
        }
        //值不为空，或者不是公共字段列的情况
        if (isNotNull(value) || !isSuper) {
            wrapper.set(column, value);
        }
    }

    /**
     * 设置嵌套的查询条件
     */
    private static <T> void setColumnNestedCondition(QueryWrapper<T> wrapper, Keyword keyword, List<String> columns, Object value,
                                                     NestedType nestedType) {
        //过滤关键字为空的，或是字段列为空的数据
        if (isNull(keyword) || isEmpty(columns)) {
            return;
        }
        //判断如果值为空，但是条件不为isNull或是isNotNull的情况
        if(isEmpty(value) && !isNullCondition(keyword)){
            return;
        }
        //嵌套查询
        wrapper.nested(w -> {
            //循环列信息
            for (int i = 0; i < columns.size(); i++) {
                if (i > 0 && isNestedOr(nestedType)) {
                    w.or();
                }
                setColumnCondition(w, keyword, columns.get(i), value);
            }
        });
    }

    /**
     * 设置查询条件
     */
    private static <T> void setColumnCondition(QueryWrapper<T> wrapper, Keyword keyword, String column, Object value) {
        //过滤关键字为空的，或是字段列为空的数据
        if (isNull(keyword) || isEmpty(column)) {
            return;
        }
        //如果是条件是isNull或是isNotNull
        if (isNullCondition(keyword)) {
            setColumnIsNullCondition(wrapper, column, keyword == Keyword.IS_NULL);
            return;
        }
        //过滤值为空的数据
        if (isEmpty(value)) {
            return;
        }
        //如果条件是in或是notIn
        if (isInCondition(keyword)) {
            setColumnInCondition(wrapper, column, value, keyword == Keyword.IN);
            return;
        }
        //设置基本条件
        setColumnBasicCondition(wrapper, keyword, column, value);
    }

    /**
     * 是否在赋值的过滤范围内
     */
    private static boolean inSetFilters(AttributeProperty p, AttributeProperty primary, String[] fields) {
        return notEqual(p, primary) && inFilters(p.getColumn(), fields);
    }

    /**
     * 是否在过滤范围内
     */
    private static boolean inFilters(AttributeColumn column, String[] fields) {
        return isNotNull(column) && (isEmpty(fields) || ArrayUtil.contains(fields, column.getFieldName()) || ArrayUtil
                .contains(fields, column.getName()));
    }

    /**
     * 获取实体类的类型
     */
    @SuppressWarnings("unchecked")
    private static <T> Class<T> getEntityClass(T entity) {
        return (Class<T>) entity.getClass();
    }

    /**
     * 设置isNull或是isNotNull条件
     */
    private static <T> void setColumnIsNullCondition(QueryWrapper<T> wrapper, String column, boolean isNull) {
        if (isNull) {
            wrapper.isNull(column);
            return;
        }
        wrapper.isNotNull(column);
    }

    /**
     * 设置in或是notIn条件，只对Collection、Array、String类型的条件进行设置
     */
    private static <T> void setColumnInCondition(QueryWrapper<T> wrapper, String column, Object value, boolean isIn) {
        //如果是集合
        if (value instanceof Collection<?>) {
            //判断数组的长度是否超过1000
            if (((Collection<?>) value).size() <= QUERY_IN_MAX_SIZE) {
                setWrapperInCollection(wrapper, column, (Collection<?>) value, isIn);
                return;
            }
            //将集合转为ArrayList，在进行分组
            setNestedInCondition(wrapper, column, group(new ArrayList<>((Collection<?>) value)), isIn);
            return;
        }
        //如果是数组
        if (ArrayUtil.isArray(value)) {
            if (ArrayUtil.length(value) <= QUERY_IN_MAX_SIZE) {
                setWrapperInArray(wrapper, column, value, isIn);
                return;
            }
            //将数组转换为ArrayList，在进行分组
            setNestedInCondition(wrapper, column, group(Arrays.asList(ArrayUtil.wrap(value))), isIn);
            return;
        }
        //如果是字符串
        if (value instanceof String) {
            String inValue = (String) value;
            if (isIn) {
                wrapper.inSql(column, inValue);
                return;
            }
            wrapper.notInSql(column, inValue);
        }
        //其他不符合条件的的不处理
    }

    /**
     * 设置嵌套的in或是notIn条件，当Collection、Array的元素大于1000时，对元素按照1000的大小分组，在嵌套or进行查询
     */
    private static <T> void setNestedInCondition(QueryWrapper<T> wrapper, String column, List<List<Object>> group, boolean isIn) {
        wrapper.nested(w -> {
            for (int i = 0; i < group.size(); i++) {
                List<Object> list = group.get(i);
                if (isEmpty(list)) {
                    continue;
                }
                if (i > 0) {
                    w.or();
                }
                setWrapperInCollection(w, column, list, isIn);
            }
        });
    }

    /**
     * 设置wrapper的in或是notIn集合条件
     */
    private static <T> void setWrapperInCollection(QueryWrapper<T> wrapper, String column, Collection<?> coll, boolean isIn) {
        if (isIn) {
            wrapper.in(column, coll);
            return;
        }
        wrapper.notIn(column, coll);
    }

    /**
     * 设置wrapper的in或是notIn数组条件
     */
    private static <T> void setWrapperInArray(QueryWrapper<T> wrapper, String column, Object array, boolean isIn) {
        if (isIn) {
            wrapper.in(column, ArrayUtil.wrap(array));
            return;
        }
        wrapper.notIn(column, ArrayUtil.wrap(array));
    }

    /**
     * 设置基本条件，基本条件有：
     * <p>
     * {@link Keyword#EQ}，{@link Keyword#NE}，{@link Keyword#GT}，{@link Keyword#GE}，{@link Keyword#LT}，{@link Keyword#LE}
     * {@link Keyword#LIKE}，{@link Keyword#LIKE_LEFT}，{@link Keyword#LIKE_RIGHT}
     */
    private static <T> void setColumnBasicCondition(QueryWrapper<T> wrapper, Keyword keyword, String column, Object value) {
        switch (keyword) {
            case EQ:
                wrapper.eq(column, value);
                break;
            case NE:
                wrapper.ne(column, value);
                break;
            case GT:
                wrapper.gt(column, value);
                break;
            case GE:
                wrapper.ge(column, value);
                break;
            case LT:
                wrapper.lt(column, value);
                break;
            case LE:
                wrapper.le(column, value);
                break;
            case LIKE:
                wrapper.like(column, value);
                break;
            case LIKE_LEFT:
                wrapper.likeLeft(column, value);
                break;
            case LIKE_RIGHT:
                wrapper.likeRight(column, value);
                break;
            default:
                break;
        }
    }

    /**
     * 判断是否isNull或是isNotNull条件
     */
    private static boolean isNullCondition(Keyword keyword) {
        return keyword == Keyword.IS_NULL || keyword == Keyword.IS_NOT_NULL;
    }

    /**
     * 判断是否in或是notIn条件
     */
    private static boolean isInCondition(Keyword keyword) {
        return keyword == Keyword.IN || keyword == Keyword.NOT_IN;
    }
}
