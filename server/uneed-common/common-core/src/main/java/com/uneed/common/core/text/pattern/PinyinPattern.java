package com.uneed.common.core.text.pattern;

/**
 * Class description goes here.
 *
 * @author diablo
 * @date 17/12/27
 */
public enum PinyinPattern {

    NONE_TONE(1, "WITHOUT_TONE","不带音调"),
    NONE_TONE_FU(2, "WITHOUT_TONE","不带音调，且转换后首字母大写"),
    TONE_MARK(3, "WITH_TONE_MARK","带音调"),
    TONE_MARK_FU(4, "WITH_TONE_MARK","带音调，且转换后首字母大写"),
    TONE_NUMBER(5, "WITH_TONE_NUMBER","用数字表示音调"),
    TONE_NUMBER_FU(6, "WITH_TONE_NUMBER","用数字表示音调，且转换后首字母大写");

    private Integer flag;
    private String format;
    private String description;

    PinyinPattern(Integer flag, String format, String description) {
        this.flag = flag;
        this.format=format;
        this.description = description;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
