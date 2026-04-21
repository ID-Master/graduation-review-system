package cn.edu.cuhk.mkt.common.consts;

/**
 * 系统常量
 *
 * @author taokai
 */
public class SystemConst {
    /**
     * 伪装登录符号
     */
    public static final String DISGUISE_LOGIN_SYMBOL = ">";

    /**
     * session 会话最大存活时间（单位：秒）
     * 30天 = 60 * 60 * 24 * 30 = 2592000
     */
    public static final int SESSION_MAX_SECONDS = 2592000;

    /**
     * 缓存短信验证码key前缀
     */
    public static final String CACHE_SMS_PREFIX_KEY = "SMS:";

    /**
     * 缓存用户信息key前缀
     */
    public static final String CHACHE_USER_PREFIX_KEY = "USER:";

    /**
     * 用户缓存最大存活时间（单位：秒）
     * 3小时零11分钟 = (60 * 60 * 3) + 660 = 11460
     */
    public static final int CHACHE_USER_MAX_SECONDS = 11460;

    /**
     * 管理员账号：admin
     */
    public static final String SYS_ADMIN_NAME = "admin";

    /**
     * 系统管理员密码
     */
    public static final String SYS_ADMIN_PASSWORD = "cuhk@mkt2021";

    /**
     * minio 默认bucket name
     */
    public static final String MINIO_BUCKET_NAME = "default";

    /**
     * url分隔符
     */
    public static final String URI_DELIMITER = "/";

    /**
     * 本地环境
     */
    public static final String ENV_LOCAL = "local";

    /**
     * 测试环境
     */
    public static final String ENV_DEV = "dev";

    /**
     * 正式环境
     */
    public static final String ENV_PROD = "prod";

}
