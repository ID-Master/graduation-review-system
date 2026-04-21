package com.uneed.common.mybatis.service.impl;

import com.uneed.common.mybatis.base.SuperServiceImpl;
import com.uneed.common.mybatis.entity.TeacherClasses;
import com.uneed.common.mybatis.mapper.TeacherClassesMapper;
import com.uneed.common.mybatis.service.TeacherClassesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * do something in here.
 *
 * @author diablo
 * @date 2020/4/13
 */
@Service
public class TeacherClassesServiceImpl extends SuperServiceImpl<TeacherClassesMapper, TeacherClasses>
        implements TeacherClassesService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteAll() {
        return mapper.deleteAll();
    }
}
