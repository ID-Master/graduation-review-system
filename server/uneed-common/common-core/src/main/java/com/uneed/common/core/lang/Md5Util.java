package com.uneed.common.core.lang;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5工具类。
 *
 * @author diablo
 * @date 2020/5/13
 * @since 1.1.0
 */
@Slf4j
public final class Md5Util {

    /**
     * 私有化构造函数，禁止实例化该类
     */
    private Md5Util() {
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * 标准字符串MD5加密
     *
     * @param plaintext 明文字符串
     * @return MD5加密后的32位字符
     */
    public static String encrypt(String plaintext) {
        try {
            //创建一个MD5算法对象，并获得MD5字节数组,16*8=128位
            byte[] hash = MessageDigest.getInstance("MD5").digest(plaintext.getBytes(StandardCharsets.UTF_8));
            //转换为十六进制字符串
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & 0xFF) < 0x10) {
                    hex.append("0");
                }
                hex.append(Integer.toHexString(b & 0xFF));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("MD5 encryption is not supported", e);
        }
        return "";
    }
}
