package com.uneed.common.mybatis.service;

import com.uneed.common.mybatis.base.SuperService;
import com.uneed.common.mybatis.entity.Classes;

import java.util.List;

/**
 * do something in here.
 *
 * @author diablo
 * @date 2020/4/13
 */
public interface ClassesService extends SuperService<Classes> {

    int delete(List<Long> ids);

    int deleteAll();
}
