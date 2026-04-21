package com.uneed.common.mybatis.service.impl;

import com.uneed.common.mybatis.base.SuperServiceImpl;
import com.uneed.common.mybatis.entity.Classes;
import com.uneed.common.mybatis.mapper.ClassesMapper;
import com.uneed.common.mybatis.service.ClassesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.uneed.common.core.lang.ObjectUtil.isNotEmpty;

/**
 * do something in here.
 *
 * @author diablo
 * @date 2020/4/13
 */
@Service
public class ClassesServiceImpl extends SuperServiceImpl<ClassesMapper, Classes> implements ClassesService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int delete(List<Long> ids) {
        return isNotEmpty(ids) ? mapper.delete(ids) : 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteAll() {
        return mapper.deleteAll();
    }
}
