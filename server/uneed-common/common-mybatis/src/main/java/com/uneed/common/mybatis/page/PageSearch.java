package com.uneed.common.mybatis.page;

import com.uneed.common.core.bean.BeanUtil;
import com.uneed.common.core.collection.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import static com.uneed.common.core.lang.ObjectUtil.isNull;

/**
 * 分页条件参数类，用来承载带分页参数的条件，主要作用在controller层的方法上，自带swagger的注解信息
 * <p>
 * 1. 包含了当前查询页、查询页数量、排序信息、业务搜索条件条件4个参数
 * <p>
 * 2. 页码和每页大小会有默认值，分别为1和10
 * <p>
 * 3. 目前只支持单字段的升、降序参数承载
 * <p>
 * 4. 参数condition为泛型，创建本类型实例时在类上声明具体的参数类型，可以是任意POJO类
 *
 * @author diablo
 * @date 2020/4/21
 */
@Data
@ApiModel(value = "PageParam", description = "分页参数信息")
public class PageSearch<POJO> implements Serializable {

    private static final long serialVersionUID = -5684309014358010441L;

    /**
     * 当前查询页
     */
    @ApiModelProperty(value = "当前查询页", name = "current", position = 1)
    private int current;

    /**
     * 查询页数量
     */
    @ApiModelProperty(value = "查询页数量", name = "size", position = 2)
    private int size;

    /**
     * 排序信息，支持多字段：[{'field':'code','asc':true},'field':'name','asc':false}]
     */
    @ApiModelProperty(value = "排序信息，支持多字段：[{'field':'code','asc':true},'field':'name','asc':false}]", name = "sorts", position = 3)
    private List<Sort> sorts;

    /**
     * 业务搜索条件，泛型参数
     */
    @ApiModelProperty(value = "业务搜索条件，泛型参数", name = "condition", position = 4)
    private POJO condition;

    /**
     * 获取当前页码，默认为 1
     *
     * @return int
     */
    public int getCurrent() {
        return current > 0 ? current : 1;
    }

    /**
     * 获取当前页数量，默认为 10
     *
     * @return int
     */
    public int getSize() {
        return size > 0 ? size : 10;
    }

    /**
     * 获取业务条件参数对象，默认会返回一个泛型类型的实例对象
     *
     * @return search
     */
    public POJO getCondition(@NonNull Class<POJO> clazz) {
        if (isNull(condition)) {
            condition = BeanUtil.newInstance(clazz);
        }
        return condition;
    }

    /**
     * 增加排序条件
     *
     * @param field 排序字段
     * @param asc   是否升序
     * @return PageSearch<S>
     */
    public PageSearch<POJO> addSort(String field, boolean asc) {
        if (isNull(sorts)) {
            sorts = Lists.newArrayList();
        }
        sorts.add(new Sort(field, asc));
        return this;
    }

    /**
     * 增加升序条件集
     *
     * @param fields 字段集合
     * @return PageSearch<S>
     */
    public PageSearch<POJO> addAsc(String... fields) {
        Arrays.stream(fields).forEach(s -> addSort(s, true));
        return this;
    }

    /**
     * 增加降序条件集
     *
     * @param fields 字段集合
     * @return PageSearch<S>
     */
    public PageSearch<POJO> addDesc(String... fields) {
        Arrays.stream(fields).forEach(s -> addSort(s, false));
        return this;
    }
}
