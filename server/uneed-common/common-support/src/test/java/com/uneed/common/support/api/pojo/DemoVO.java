package com.uneed.common.support.api.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * demo类 VO对象
 *
 * @author diablo
 * @date 2020/4/26
 */
@Data
public class DemoVO implements Serializable {

    private static final long serialVersionUID = -7494690872397519580L;
    /**
     * 主键
     */
    private Long id;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 最后更新人
     */
    private Long updateBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后更新时间
     */
    private Date updateTime;

    /**
     * 逻辑删除标记，取值0/1，1表示被删除的数据
     */
    private Integer removeFlag;

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
