package com.uneed.common.mybatis.service;

import com.uneed.common.mybatis.base.SuperService;
import com.uneed.common.mybatis.entity.TeacherClasses;

/**
 * do something in here.
 *
 * @author diablo
 * @date 2020/4/13
 */
public interface TeacherClassesService extends SuperService<TeacherClasses> {

    int deleteAll();
}
