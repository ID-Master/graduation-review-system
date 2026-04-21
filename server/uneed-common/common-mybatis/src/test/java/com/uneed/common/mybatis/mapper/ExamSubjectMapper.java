package com.uneed.common.mybatis.mapper;

import com.uneed.common.mybatis.base.SuperMapper;
import com.uneed.common.mybatis.entity.ExamSubject;

/**
 * 考试科目信息 Mapper接口
 *
 * @author diablo
 * @date 2020/4/25
 */
public interface ExamSubjectMapper extends SuperMapper<ExamSubject> {

    int deleteAll();
}