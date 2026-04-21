package com.uneed.common.mybatis.mapper;

import com.uneed.common.mybatis.base.SuperMapper;
import com.uneed.common.mybatis.entity.Classes;

import java.util.List;

/**
 * 班级信息 Mapper接口
 *
 * @author diablo
 * @since 2018-09-20
 */
public interface ClassesMapper extends SuperMapper<Classes> {

    int delete(List<Long> ids);

    int deleteAll();
}