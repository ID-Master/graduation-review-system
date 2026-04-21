package com.uneed.common.mybatis.service.impl;

import com.uneed.common.mybatis.base.SuperServiceImpl;
import com.uneed.common.mybatis.entity.ExamSubject;
import com.uneed.common.mybatis.mapper.ExamSubjectMapper;
import com.uneed.common.mybatis.service.ExamSubjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * do something in here.
 *
 * @author diablo
 * @date 2020/4/25
 */
@Service
public class ExamSubjectServiceImpl extends SuperServiceImpl<ExamSubjectMapper, ExamSubject> implements ExamSubjectService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteAll() {
        return mapper.deleteAll();
    }
}
