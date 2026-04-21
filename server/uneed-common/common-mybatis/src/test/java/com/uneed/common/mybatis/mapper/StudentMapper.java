package com.uneed.common.mybatis.mapper;

import com.uneed.common.mybatis.base.SuperMapper;
import com.uneed.common.mybatis.entity.Student;

/**
 * 学生信息 Mapper接口
 *
 * @author diablo
 * @since 2018-09-20
 */
public interface StudentMapper extends SuperMapper<Student> {

    int deleteAll();

}