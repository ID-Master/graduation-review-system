package com.uneed.common.annotation.constant;

/**
 * 数据有效性常量
 *
 * @author diablo
 * @date 2020/1/31
 */
@Deprecated
public interface Status {

    /**
     * 用来标记在数据字典中的key值
     */
    String ROOT = "ACTIVE";

    /**
     * 有效的
     */
    String ENABLE = "ENABLE";

    /**
     * 无效的
     */
    String DISABLE = "DISABLE";

}
