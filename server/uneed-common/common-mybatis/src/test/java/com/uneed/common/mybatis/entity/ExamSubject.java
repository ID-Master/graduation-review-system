package com.uneed.common.mybatis.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.uneed.common.mybatis.model.TenantModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 考试科目信息
 * </p>
 *
 * @author diablo
 * @date 2020/4/25
 */
@EqualsAndHashCode(callSuper = true)
@TableName("ud_exam_subject")
@Data
public class ExamSubject extends TenantModel {

    private static final long serialVersionUID = -1650551427858188871L;

    /**
     * 科目编号
     */
    @TableField("subject_code")
    private String subjectCode;
    /**
     * 科目名称
     */
    @TableField("subject_name")
    private String subjectName;
}
