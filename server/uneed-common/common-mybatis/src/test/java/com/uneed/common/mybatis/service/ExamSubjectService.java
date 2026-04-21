package com.uneed.common.mybatis.service;

import com.uneed.common.mybatis.base.SuperService;
import com.uneed.common.mybatis.entity.ExamSubject;

/**
 * do something in here.
 *
 * @author diablo
 * @date 2020/4/25
 */
public interface ExamSubjectService extends SuperService<ExamSubject> {

    int deleteAll();
}
