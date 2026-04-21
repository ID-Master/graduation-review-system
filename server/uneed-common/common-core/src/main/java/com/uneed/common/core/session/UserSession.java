package com.uneed.common.core.session;

import com.alibaba.fastjson.JSON;
import com.uneed.common.core.lang.StringUtil;
import com.uneed.common.core.util.SessionUtil;

/**
 * 登录用户session信息
 * @author taokai
 */
public class UserSession {
    /**
     * 用户信息
     */
    public static final String SESSION_KEY_USER_INFO = "USER_INFO";

    /**
     * 获取当前登录用户id
     * @return
     */
    public static String id(){
        return getUser().getId();
    }

    /**
     * 从session中获取用户信息
     * @return
     */
    public static UserInfo getUser(){
        String userInfoJson = (String) SessionUtil.getSessionAttribute(SESSION_KEY_USER_INFO);
        if(StringUtil.isEmpty(userInfoJson)){
            return new UserInfo();
        }
        UserInfo userInfo = JSON.parseObject(userInfoJson, UserInfo.class);
        return userInfo;
    }

    /**
     * 将用户信息设置到session
     * @param userInfo
     */
    public static void setUser(UserInfo userInfo){
        // 将用户数据转成json格式存储到session
        String userInfoJson = JSON.toJSONString(userInfo);
        SessionUtil.setSessionAttribute(UserSession.SESSION_KEY_USER_INFO, userInfoJson);
    }

    /*
     * 模拟自定义学生账号/用户信息
     */
    public static UserInfo initStudentTest(){
        UserInfo userInfo = new UserInfo();
        userInfo.setId("23f1b3133c9d4d9ea3426d4cdb590381");
        userInfo.setLoginName("studenttest01");
        userInfo.setUserType("STUDENT");
        userInfo.setStudentId("studenttest01");
        userInfo.setMajor("Professional Accountancy");
        userInfo.setMinor("Finance");
        userInfo.setGrade("2020");
        userInfo.setExpectedYear("2320");
        return userInfo;
    }

}
