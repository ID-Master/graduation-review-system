package com.uneed.common.support.api.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * demo类 DTO对象
 *
 * @author diablo
 * @date 2020/4/26
 */
@Data
public class DemoDTO implements Serializable {

    private static final long serialVersionUID = -4780749868991393898L;
    /**
     * 主键
     */
    private Long id;

    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 分数
     */
    private BigDecimal score;

    /**
     * 有效性
     */
    private Boolean enable;

    /**
     * 备注
     */
    private String remark;
}
