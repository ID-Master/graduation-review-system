package com.uneed.common.mybatis.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.uneed.common.mybatis.model.SuperModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 教师班级信息
 * </p>
 *
 * @author diablo
 * @since 2018-09-20
 */
@EqualsAndHashCode(callSuper = true)
@TableName("ud_teacher_classes")
@Data
public class TeacherClasses extends SuperModel {

    private static final long serialVersionUID = 1L;

    /**
     * 教师id
     */
    @TableField("teacher_id")
    private Long teacherId;
    /**
     * 班级id
     */
    @TableField("classes_id")
    private Long classesId;

}
