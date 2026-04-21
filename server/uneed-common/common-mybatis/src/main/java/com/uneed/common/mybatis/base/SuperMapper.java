package com.uneed.common.mybatis.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.uneed.common.mybatis.model.IdModel;

/**
 * 数据库持久化接口超类，集成mybatis-plus提供的BaseMapper，这里单独再创建一个类的目的是方便后期针对插件做扩展用
 *
 * @author diablo
 * @date 2020/4/1
 */
public interface SuperMapper<T extends IdModel> extends BaseMapper<T> {

}
