package com.uneed.common.mybatis.vo;

import com.uneed.common.mybatis.entity.ExamSubject;
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
@Data
public class ExamSubjectVO extends ExamSubject {

    private static final long serialVersionUID = -879726093358739897L;
    /**
     * 描述
     */
    private String description;

    /**
     * 描述
     */
    private Integer version;
}
