package cn.edu.cuhk.mkt.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ad 域同步数据类型
 *
 * @author taokai
 */
@Getter
@AllArgsConstructor
public enum AdSyncEnum {

    /**
     * 教师组织同步
     */
    ORG_STAFF("ORG_STAFF", "OU=HAB Group,OU=CUHKSZ Group,DC=CUHK,DC=EDU,DC=CN"),

    /**
     * 教师同步
     */
    STAFF("STAFF", "OU=staff,OU=cuhksz,DC=CUHK,DC=EDU,DC=CN"),

    /**
     * 学生组织同步
     */
    ORG_STUDENT("ORG_STUDENT", "OU=students,OU=cuhksz,DC=CUHK,DC=EDU,DC=CN"),

    /**
     * 学生同步
     */
    STUDENT("STUDENT", "OU=students,OU=cuhksz,DC=CUHK,DC=EDU,DC=CN");

    private String code;
    private String adKey;

}
