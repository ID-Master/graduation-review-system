package cn.edu.cuhk.mkt.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 状态
 *
 * @author taokai
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {
    EFFECTIVE(1, "有效"),
    INVALID(0, "无效"),
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
