package cn.edu.cuhk.mkt.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 邮件状态（0：待发送，1：成功，2：失败）
 *
 * @author taokai
 */
@Getter
@AllArgsConstructor
public enum EmailStatusEnum {
    WAIT_SEND(0, "待发送"),
    SUCCESS(1, "成功"),
    FAIL(2, "失败"),
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
