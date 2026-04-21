package com.uneed.common.mybatis.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.uneed.common.mybatis.model.SuperModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 教师信息
 * </p>
 *
 * @author diablo
 * @since 2018-09-20
 */
@EqualsAndHashCode(callSuper = true)
@TableName("ud_teacher")
@Data
public class Teacher extends SuperModel {

    private static final long serialVersionUID = 1L;

    /**
     * 教师编号
     */
    @TableField("tea_code")
    private String teaCode;
    /**
     * 教师姓名
     */
    @TableField("tea_name")
    private String teaName;
    /**
     * 教师教龄
     */
    @TableField("tea_age")
    private Double teaAge;
    /**
     * 教师性别，取值数据字典：gender，0/1/2（未知/男/女）
     */
    @TableField("tea_sex")
    private String teaSex;

}
