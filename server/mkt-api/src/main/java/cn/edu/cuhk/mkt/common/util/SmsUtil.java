package cn.edu.cuhk.mkt.common.util;

import cn.edu.cuhk.mkt.common.consts.SystemConst;

/**
 * 短信工具类
 * @author taokai
 */
public class SmsUtil {

    /**
     * 获取短信验证码缓存key
     * @param smsKey
     * @return
     */
    public static String generateSmsKey(String smsKey){
        return SystemConst.CACHE_SMS_PREFIX_KEY + smsKey;
    }
}
