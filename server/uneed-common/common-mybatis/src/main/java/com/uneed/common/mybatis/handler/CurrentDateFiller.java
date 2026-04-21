package com.uneed.common.mybatis.handler;

import java.time.LocalDateTime;

/**
 * 当前时间填充器.
 *
 * @author diablo
 * @date 2018/9/30
 */
public interface CurrentDateFiller {

    /**
     * 当前操作用户id
     *
     * @return long 用户id
     */
    LocalDateTime currentDate();
}
