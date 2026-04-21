package com.uneed.common.dict.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.uneed.common.core.lang.ObjectUtil.isNotNull;

/**
 * 数据字典实体对象
 *
 * @author anding.huang@u-need.cn
 * @date 2019/12/16
 */
@Getter
@Setter
@ToString
public class Dict implements Serializable, Comparable<Dict> {

    private static final long serialVersionUID = -1954983345108979387L;

    /**
     * 字典编号，字典的唯一标记
     */
    private String key;

    /**
     * 字典名称
     */
    private String name;

    /**
     * 字典值
     */
    private String value;

    /**
     * 描述
     */
    private String description;

    /**
     * 排序
     */
    private Double sort;

    /**
     * 用来标记该字典对象是否有效
     */
    private Boolean enable;

    /**
     * 父级字典
     */
    private String parent;

    /**
     * 字典子集的key集合
     */
    private List<String> subset;

    public Double getSort() {
        return isNotNull(sort) ? sort : 1D;
    }

    public Boolean getEnable() {
        return isNotNull(enable) ? enable : Boolean.TRUE;
    }

    @Override
    public int compareTo(@NonNull Dict o) {
        return this.getSort().compareTo(o.getSort());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Dict)) {
            return false;
        }
        return Objects.equals(((Dict) obj).getKey(), this.key);
    }
}
