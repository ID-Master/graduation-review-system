package com.uneed.common.dict.constant;

/**
 * 行政区域常量.
 *
 * @author diablo
 * @date 2020/6/29
 */
public class AreaConstant {

    /**
     * 行政区域在redis中的key值
     */
    public static final String AREA_REDIS_KEY = "AREA:KEY";

    /**
     * 行政区域跟节点的key值
     */
    public static final String ROOT_KEY = "0";

    /**
     * 行政区域跟节点的name值
     */
    public static final String ROOT_NAME = "ROOT";

    /**
     * 行政区域字段-code
     */
    public static final String FIELD_CODE = "code";

    /**
     * 行政区域字段-name
     */
    public static final String FIELD_NAME = "name";

    /**
     * 行政区域字段-subset
     */
    public static final String FIELD_SUBSET = "subset";

    /**
     * 行政区域字段-parent
     */
    public static final String FIELD_PARENT = "parent";
}
