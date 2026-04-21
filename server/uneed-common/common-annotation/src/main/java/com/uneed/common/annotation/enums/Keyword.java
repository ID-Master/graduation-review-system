package com.uneed.common.annotation.enums;

/**
 * 查询条件关键字
 *
 * @author anding.huang@u-need.cn
 * @date 2019/12/15
 */
public enum Keyword {
    /**
     * 用来标记查询条件的关键字
     */
    EQ("=", "= value"),
    NE("<>", "<> value"),
    GT(">", "> value"),
    GE(">=", ">= value"),
    LT("<", "< value"),
    LE("<=", "<= value"),
    LIKE("like", "%value%"),
    LIKE_LEFT("like left", "%value"),
    LIKE_RIGHT("like right", "value%"),
    IN("in", "in (value)"),
    NOT_IN("not in", "not in (value)"),
    IS_NULL("is null", "is null"),
    IS_NOT_NULL("is not null", "is not null");

    private final String code;
    private final String description;

    Keyword(String code, String description) {
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
