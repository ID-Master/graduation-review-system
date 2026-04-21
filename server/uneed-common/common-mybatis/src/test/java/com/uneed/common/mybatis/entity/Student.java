package com.uneed.common.mybatis.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.uneed.common.mybatis.model.SuperModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 学生信息
 * </p>
 *
 * @author diablo
 * @since 2018-09-20
 */
@EqualsAndHashCode(callSuper = true)
@TableName("ud_student")
@Data
public class Student extends SuperModel {

    private static final long serialVersionUID = 1L;

    /**
     * 学生编号
     */
    @TableField("stu_code")
    private String stuCode;
    /**
     * 学生姓名
     */
    @TableField("stu_name")
    private String stuName;
    /**
     * 学生姓名拼音
     */
    @TableField("stu_name_py")
    private String stuNamePy;
    /**
     * 学生年龄
     */
    @TableField("stu_age")
    private Integer stuAge;
    /**
     * 学生性别，取值数据字典：gender，0/1/2（未知/男/女）
     */
    @TableField("stu_sex")
    private String stuSex;
    /**
     * 入学时间
     */
    @TableField("enrolment_time")
    private Date enrolmentTime;
    /**
     * 所属班级
     */
    @TableField("classes_id")
    private Long classesId;

}
