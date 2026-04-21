package cn.edu.cuhk.mkt.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务编号前缀
 *
 * @author taokai
 */
@Getter
@AllArgsConstructor
public enum BizCodeEnum {
    TEST("TEST", "测试"),
    COURSE_MASTER("course_master", "课程对象")
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
