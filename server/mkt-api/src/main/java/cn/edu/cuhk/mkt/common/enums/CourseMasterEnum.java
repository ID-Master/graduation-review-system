package cn.edu.cuhk.mkt.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 课程主数据状态枚举
 *
 * @author taokai
 */
public interface CourseMasterEnum {

    /**
     * 状态（0：草稿，1：待审核，2、已驳回，3、已完成）
     */
    @Getter
    @AllArgsConstructor
    enum STATUS {
        DRAFT(0, "草稿"),
        REVIEWED(1, "待审核"),
        REJECTED(2, "已驳回"),
        OFFICE_CHECKED(3, "已完成"),
        DISCARD(4, "废弃"),
        ;

        /**
         * 编码
         */
        private Integer code;
        /**
         * 描述
         */
        private String desc;

    }

    /**
     * 审核操作
     */
    @Getter
    @AllArgsConstructor
    enum ACTIOIN {
        AGREE("agree", "同意"),
        REJECT("reject", "驳回"),
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
