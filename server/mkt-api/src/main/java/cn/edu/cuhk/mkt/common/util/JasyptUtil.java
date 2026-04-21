package cn.edu.cuhk.mkt.common.util;

import org.apache.commons.lang3.StringUtils;
import org.jasypt.util.text.BasicTextEncryptor;
import org.jasypt.util.text.StrongTextEncryptor;

/**
 * jasypt生成密文工具
 *
 * @author taokai
 */
public class JasyptUtil {

    public static final String SECRET_KEY = "cuhk-mkt@2021";

    public static void main(String[] args) {
        String encPwd = "q7S1OgiiZNy5UY717ZDvrmLaWMkrfhrt";
        // 加密
        //String encPwd = encryptPbeWithMD5AndDES(SECRET_KEY, pwd);
        // 解密
        String decPwd = decryptPbeWithMD5AndDES(SECRET_KEY, encPwd);

        System.out.println(encPwd);
        System.out.println(decPwd);

//        System.out.println("---------------------------------------------");
//
//        // 加密
//        String encTriplePwd = encryptPbeWithMD5AndTripleDES(SECRET_KEY, pwd);
//        // 解密
//        String decTriplePwd = decryptPbeWithMD5AndTripleDES(SECRET_KEY, encTriplePwd);
//
//        System.out.println(encTriplePwd);
//        System.out.println(decTriplePwd);

    }

    /**
     * 加密
     * @param secretKey 秘钥
     * @param password 要加密的字符串
     * @return 加密字符串
     */
    public static String encryptPbeWithMD5AndDES(String secretKey, String password) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        if(StringUtils.isBlank(secretKey)){
            secretKey = SECRET_KEY;
        }
        textEncryptor.setPassword(secretKey);
        return textEncryptor.encrypt(password);
    }

    /**
     * 解密
     * @param secretKey 秘钥
     * @param value 加密后的字符串
     * @return 解密字符串
     */
    public static String decryptPbeWithMD5AndDES(String secretKey, String value) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        if(StringUtils.isBlank(secretKey)){
            secretKey = SECRET_KEY;
        }
        //秘钥
        textEncryptor.setPassword(secretKey);
        //解密
        return textEncryptor.decrypt(value);
    }

    /**
     * 加密
     * @param secretKey 秘钥
     * @param password 要加密的字符串
     * @return 加密字符串
     */
    public static String encryptPbeWithMD5AndTripleDES(String secretKey, String password) {
        StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
        if(StringUtils.isBlank(secretKey)){
            secretKey = SECRET_KEY;
        }
        textEncryptor.setPassword(secretKey);
        return textEncryptor.encrypt(password);
    }

    /**
     * 解密
     * @param secretKey 秘钥
     * @param value 加密后的字符串
     * @return 解密字符串
     */
    public static String decryptPbeWithMD5AndTripleDES(String secretKey, String value) {
        StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
        if(StringUtils.isBlank(secretKey)){
            secretKey = SECRET_KEY;
        }
        textEncryptor.setPassword(secretKey);
        return textEncryptor.decrypt(value);
    }

}
