package com.uneed.common.mybatis.handler;

import java.time.LocalDateTime;

/**
 * do something in here.
 *
 * @author diablo
 * @date 2020/4/11
 */
public class DefaultCurrentDateFiller implements CurrentDateFiller {

    @Override
    public LocalDateTime currentDate() {
        return LocalDateTime.now();
    }
}
