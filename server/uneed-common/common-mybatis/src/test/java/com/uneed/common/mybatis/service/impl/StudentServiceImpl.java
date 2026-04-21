package com.uneed.common.mybatis.service.impl;

import com.uneed.common.mybatis.base.SuperServiceImpl;
import com.uneed.common.mybatis.entity.Student;
import com.uneed.common.mybatis.mapper.StudentMapper;
import com.uneed.common.mybatis.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * do something in here.
 *
 * @author diablo
 * @date 2020/4/13
 */
@Service
public class StudentServiceImpl extends SuperServiceImpl<StudentMapper, Student> implements StudentService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteAll() {
        return mapper.deleteAll();
    }
}
