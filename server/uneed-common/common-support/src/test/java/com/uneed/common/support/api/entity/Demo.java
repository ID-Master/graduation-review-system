package com.uneed.common.support.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.uneed.common.mybatis.model.SuperModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * demo实体类
 *
 * @author diablo
 * @date 2020/4/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Demo extends SuperModel {

    private static final long serialVersionUID = 9029334805579829026L;
    /**
     * 编号
     */
    @TableField(value = "code")
    private String code;

    /**
     * 名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 分数
     */
    @TableField(value = "score")
    private BigDecimal score;

    /**
     * 有效性
     */
    @TableField(value = "enable")
    private Boolean enable;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;
}
