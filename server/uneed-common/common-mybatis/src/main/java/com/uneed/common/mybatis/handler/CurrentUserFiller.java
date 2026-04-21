package com.uneed.common.mybatis.handler;

/**
 * 当前用户填充器.
 *
 * @author diablo
 * @date 2018/9/30
 */
public interface CurrentUserFiller {

    /**
     * 当前操作用户id
     *
     * @return ID 用户id
     */
    String currentId();
}
