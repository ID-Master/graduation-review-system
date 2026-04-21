package com.uneed.common.mybatis.handler;

import com.uneed.common.core.session.UserSession;
import com.uneed.common.core.util.SessionUtil;

/**
 * do something in here.
 *
 * @author diablo
 * @date 2020/4/11
 */
public class DefaultCurrentUserFiller implements CurrentUserFiller {

    @Override
    public String currentId() {
        if (SessionUtil.getSession() == null) {
            return "0";
        }
        return UserSession.id();
    }
}
