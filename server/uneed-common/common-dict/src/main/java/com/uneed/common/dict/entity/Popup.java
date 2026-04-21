package com.uneed.common.dict.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * do something in here.
 *
 * @author diablo
 * @date 2020/2/27
 */
@Data
public class Popup implements Serializable {

    private static final long serialVersionUID = -5941209701488521707L;

    /**
     * 弹窗编号
     */
    private String code;

    /**
     * 弹窗名称
     */
    private String name;

    /**
     * 弹窗内容
     */
    private String content;

    /**
     * 显示终端，pc、小程序等
     */
    private String terminal;

    /**
     * 描述
     */
    private String description;

}
