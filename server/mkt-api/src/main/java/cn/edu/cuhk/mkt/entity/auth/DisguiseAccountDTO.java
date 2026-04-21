package cn.edu.cuhk.mkt.entity.auth;

import cn.edu.cuhk.mkt.entity.sys.User;
import lombok.Data;

import java.io.Serializable;

/**
 * 伪装账号信息
 *
 * @author taokai
 */
@Data
public class DisguiseAccountDTO implements Serializable {
    /**
     * 伪装账号
     */
    private User disguiseUser;

    /**
     * 真实账号（被代理账号）
     */
    private User agentUser;

    /**
     * 是否伪装登录（是：true，否：false）
     */
    private Boolean isDisguise;

}
