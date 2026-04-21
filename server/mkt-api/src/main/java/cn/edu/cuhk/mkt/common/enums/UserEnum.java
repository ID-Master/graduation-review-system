package cn.edu.cuhk.mkt.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户枚举
 *
 * @author taokai
 */
public interface UserEnum {

    /**
     * 用户类型
     */
    @Getter
    @AllArgsConstructor
    enum USER_TYPE {
        DISGUISE("DISGUISE", "伪装登录"),
        STUDENT("STUDENT", "学生"),
        TEACHER("TEACHER", "老师"),
        SYSTEM("SYSTEM", "系统用户"),
        ;

        /**
         * 编码
         */
        private String code;
        /**
         * 描述
         */
        private String desc;

    }

}
