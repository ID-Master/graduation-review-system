package com.uneed.common.mybatis.service.impl;

import com.uneed.common.mybatis.base.SuperServiceImpl;
import com.uneed.common.mybatis.entity.Teacher;
import com.uneed.common.mybatis.mapper.TeacherMapper;
import com.uneed.common.mybatis.service.TeacherService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * do something in here.
 *
 * @author diablo
 * @date 2020/4/13
 */
@Service
public class TeacherServiceImpl extends SuperServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteAll() {
        return mapper.deleteAll();
    }
}
