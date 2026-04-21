package com.uneed.common.core.session;

import java.io.Serializable;

import lombok.Data;

/**
 * 用户信息
 *
 * @author taokai
 */
@Data
public class UserInfo implements Serializable {

    /**
     * 用户id
     */
    private String id;

    /**
     * 学生id
     */
    private String studentId;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 是否为超级管理员
     */
    private Boolean superAdmin;

    /**
     * 账号
     */
    private String loginName;

    /**
     * 中文名称
     */
    private String nameCh;

    /**
     * 英文名称
     */
    private String nameEn;

    /**
     * 主修课程
     */
    private String major;

    /**
     * 选修专业
     */
    private String minor;

    /**
     * 预期毕业年份
     */
    private String expectedYear;

    /**
     * 手机号
     */
    private String contactTel;

    /**
     * 短信认证
     */
    private Boolean smsAuth;

    /**
     * 课程是否可编辑（0：不可编辑，1：可编辑）
     */
    private Integer editable;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 年级
     */
    private String grade;

    /**
     * 是否是国际学生（0：否，1：是）
     */
    private Integer internationalStudent;

}
