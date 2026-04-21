package cn.edu.cuhk.mkt.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 短信枚举类
 *
 * @author taokai
 */
public interface SmsEnum {

    /**
     * 短信验证码业务域
     */
    @Getter
    @AllArgsConstructor
    enum BIZ_SCOPE {
        AD_CAPTCHA("AD_CAPTCHA", "AD域学生登录验证码"),
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
