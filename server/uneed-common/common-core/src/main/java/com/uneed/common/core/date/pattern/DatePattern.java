package com.uneed.common.core.date.pattern;

/**
 * Class description goes here.
 *
 * @author diablo
 * @date 17/12/27
 */
public enum DatePattern {

    YEAR("year", "年"),
    MONTH("month", "月"),
    DAY("day", "日"),
    HOUR("hour", "时"),
    MINUTE("minute", "分"),
    SECOND("second", "秒");

    private String pattern;
    private String description;

    DatePattern(String pattern, String description) {
        this.pattern = pattern;
        this.description = description;
    }

    public String getPattern() {
        return pattern;
    }

    public String getDescription() {
        return description;
    }
}
