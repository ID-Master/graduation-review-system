package com.uneed.common.mybatis.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.uneed.common.mybatis.model.SuperModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 班级信息
 * </p>
 *
 * @author diablo
 * @since 2018-09-20
 */
@EqualsAndHashCode(callSuper = true)
@TableName("ud_classes")
@Data
public class Classes extends SuperModel {

    private static final long serialVersionUID = 1L;

    /**
     * 班级编号
     */
    @TableField("class_code")
    private String classCode;
    /**
     * 班级名称
     */
    @TableField("class_name")
    private String className;
}
