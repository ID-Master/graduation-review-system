package com.uneed.common.mybatis.info;

import com.uneed.common.mybatis.model.IdModel;
import com.uneed.common.mybatis.model.SuperModel;
import com.uneed.common.mybatis.model.TenantModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用来封装条件属性映射的数据库列信息.
 *
 * @author diablo
 * @date 2020/4/17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttributeColumn implements Serializable {

    private static final long serialVersionUID = 1919990559461798373L;

    /**
     * 映射的数据库列名称
     */
    private String name;

    /**
     * 数据库列映射的实体类属性名称
     */
    private String fieldName;

    /**
     * 数据库列映射的实体类属性类型
     */
    private Class<?> fieldType;

    /**
     * 是否主键列，用来构建UpdateWrapper的条件
     */
    private Boolean isPrimary;

    /**
     * 是否公共字段数据，公共字段在填充时，系统会进行自动填充。
     * <p>
     * 公共字段参见 {@link IdModel} {@link SuperModel} {@link TenantModel}
     */
    private Boolean isSuper;
}
