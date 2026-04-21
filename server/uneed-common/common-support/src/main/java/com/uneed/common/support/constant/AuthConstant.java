package com.uneed.common.support.constant;

/**
 * <p>
 * 认证类常量配置
 * </p>
 *
 * @author hcs
 * @since 2019/12/19
 */
public class AuthConstant {

    /**
     * 用户认证私钥
     */
    public static final String REDIS_USER_PRI_KEY = "AUTH:JWT:PRI";
    /**
     * 用户认证公钥
     */
    public static final String REDIS_USER_PUB_KEY = "AUTH:JWT:PUB";

    /**
     * 生成的私钥key
     */
    public static final String GENERATE_PRI_KEY = "PRI";

    /**
     * 生成的公钥key
     */
    public static final String GENERATE_PUB_KEY = "PUB";
}
