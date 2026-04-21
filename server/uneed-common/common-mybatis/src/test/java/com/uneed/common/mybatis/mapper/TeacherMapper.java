package com.uneed.common.mybatis.mapper;

import com.uneed.common.mybatis.base.SuperMapper;
import com.uneed.common.mybatis.entity.Teacher;

/**
 * 教师信息 Mapper接口
 *
 * @author diablo
 * @since 2018-09-20
 */
public interface TeacherMapper extends SuperMapper<Teacher> {

    int deleteAll();

}