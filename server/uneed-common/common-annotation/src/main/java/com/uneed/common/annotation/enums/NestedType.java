package com.uneed.common.annotation.enums;

/**
 * 是否嵌套条件
 *
 * @author diablo
 * @date 2020/4/16
 * @since 1.1.0
 */
public enum NestedType {

    /**
     * 不嵌套
     */
    NON("non", "不做嵌套条件"),

    /**
     * 嵌套or条件
     */
    OR("or", "例：(name like '%val%' or code like '%val%')"),

    /**
     * 嵌套and条件
     */
    AND("and", "例：(name like '%val%' and code like '%val%')");

    private final String code;
    private final String description;

    NestedType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
