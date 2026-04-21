package com.uneed.common.support.rest;

/**
 * do something in here.
 *
 * @author huanganding
 * @date 2020/11/28
 */
public enum ParamType {
    /**
     * url上的参数，"?"后面的
     */
    URL,

    /**
     * 请求体的参数
     */
    BODY,

    /**
     * json参数
     */
    JSON;
}
