package com.uneed.common.annotation.enums;

/**
 * 字典的属性常量
 *
 * @author anding.huang@u-need.cn
 * @date 2019/12/16
 */
public enum DictProperty {
    /**
     * 记录字典的属性枚举
     */
    KEY("key", "字典键", String.class),
    NAME("name", "字典名称", String.class),
    VALUE("value", "字典值", String.class),
    DESCRIPTION("description", "字典描述", String.class),
    SORT("sort", "排序值", Double.class),
    ENABLE("enable", "标记是否有效", Boolean.class),
    PARENT("parent", "父级字典键", String.class),

    /**
     * 当前字典对象
     *
     * @since 1.1.0
     */
    DICT("dict", "当前字典对象", Object.class),

    /**
     * 父级字典对象
     *
     * @since 1.1.0
     */
    PARENT_DICT("parentDict", "父级字典对象", Object.class);

    private final String code;
    private final String description;
    private final Class<?> type;

    DictProperty(String code, String description, Class<?> type) {
        this.code = code;
        this.description = description;
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public Class<?> getType() {
        return type;
    }
}
