package com.uneed.common.mybatis.mapper;

import com.uneed.common.mybatis.base.SuperMapper;
import com.uneed.common.mybatis.entity.TeacherClasses;

/**
 * 教师班级信息 Mapper接口
 *
 * @author diablo
 * @since 2018-09-20
 */
public interface TeacherClassesMapper extends SuperMapper<TeacherClasses> {

    int deleteAll();

}