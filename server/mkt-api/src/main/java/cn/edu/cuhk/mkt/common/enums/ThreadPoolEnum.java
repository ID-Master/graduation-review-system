package cn.edu.cuhk.mkt.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 线程枚举
 *
 * @author taokai
 */
public interface ThreadPoolEnum {

    /**
     * 线程名称前缀
     */
    @Getter
    @AllArgsConstructor
    enum THREAD_NAME_PREFIX {
        BIZ_SERIAL_NUMBER("biz_serial_number", "业务流水号线程池"),
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
