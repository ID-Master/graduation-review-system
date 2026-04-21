package com.uneed.common.dict.entity;

import com.uneed.common.core.collection.Lists;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

import static com.uneed.common.core.lang.ObjectUtil.*;

/**
 * 行政区域实体对象
 *
 * @author anding.huang@u-need.cn
 * @date 2019/12/19
 */
@Getter
@Setter
@ToString
public class Area implements Serializable, Comparable<Area> {

    private static final long serialVersionUID = 2141222566966420144L;
    /**
     * 区域编号
     */
    private String code;

    /**
     * 区域名称
     */
    private String name;

    /**
     * 简称
     */
    private String shortName;

    /**
     * 全称
     */
    private String fullName;

    /**
     * 区域名称拼音
     */
    private String pinyin;

    /**
     * 短拼音
     */
    private String shortPinyin;

    /**
     * 父级ID
     */
    private String parent;

    /**
     * 区域深度，记录字典的层级关系，1/省，2/市，3/区县，4/乡镇
     */
    private Integer depth;

    /**
     * 区域排序
     */
    private Double sort;

    /**
     * 区域路径，用来记录当前类别的id路径，用“.”分隔
     */
    private String path;

    /**
     * 子集
     */
    private List<String> subset;

    /**
     * 获取排序的时候赋默认值
     *
     * @return Double
     */
    public Double getSort() {
        if (isNull(this.sort)) {
            this.sort = convert(this.code, 1D);
        }
        return this.sort;
    }

    /**
     * 根据分割符，获取行政区域全名称
     *
     * @param space 分隔符
     * @return 行政区域全称
     */
    public String getFullName(String space) {
        return isNotEmpty(this.fullName) ? this.fullName.replaceAll("\\.", nullToDefault(space, "")) : null;
    }

    /**
     * 添加子集
     *
     * @param code 行政区域编号
     */
    public void addSubset(String code) {
        if (isNull(this.subset)) {
            this.subset = Lists.newArrayList();
        }
        if (!this.subset.contains(code)) {
            this.subset.add(code);
        }
    }

    @Override
    public int compareTo(@NonNull Area o) {
        return this.getSort().compareTo(o.getSort());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Area)) {
            return false;
        }
        return equal(((Area) obj).getCode(), this.code);
    }
}
