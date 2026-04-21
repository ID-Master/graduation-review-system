package com.uneed.common.support.jwt;

import io.jsonwebtoken.*;
import org.joda.time.DateTime;

import java.io.IOException;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

/**
 * <p>
 * jwt工具类
 * </p>
 *
 * @author hcs
 * @date 2019/12/19
 */
public class JwtUtil {

    /**
     * 密钥加密token
     *
     * @param jwtInfo    jwt载体数据
     * @param priKeyPath 私钥
     * @param expire     过期时间(秒)
     * @return 生成的token
     * @throws Exception 异常
     */
    public static String generateToken(IJwtInfo jwtInfo, String priKeyPath, int expire) throws Exception {
        return getJwtBuilder(jwtInfo, RsaTool.getInstance().getPrivateKey(priKeyPath), expire).compact();
    }

    /**
     * 密钥加密token
     *
     * @param jwtInfo jwt载体数据
     * @param priKey  私钥
     * @param expire  过期时间
     * @return 生成的token
     * @throws Exception 异常
     */
    public static String generateToken(IJwtInfo jwtInfo, byte[] priKey, int expire) throws Exception {
        return getJwtBuilder(jwtInfo, RsaTool.getInstance().getPrivateKey(priKey), expire).compact();
    }

    /**
     * 公钥解析token
     *
     * @param token      token信息
     * @param pubKeyPath 公钥
     * @return Jws<Claims>
     * @throws Exception 异常
     */
    public static Jws<Claims> parserToken(String token, String pubKeyPath) throws Exception {
        return Jwts.parserBuilder().setSigningKey(RsaTool.getInstance().getPublicKey(pubKeyPath)).build().parseClaimsJws(token);
    }

    /**
     * 公钥解析token
     *
     * @param token  token信息
     * @param pubKey 公钥
     * @return Jws<Claims>
     * @throws Exception 异常
     */
    public static Jws<Claims> parserToken(String token, byte[] pubKey) throws Exception {
        return Jwts.parserBuilder().setSigningKey(RsaTool.getInstance().getPublicKey(pubKey)).build().parseClaimsJws(token);
    }

    /**
     * 获取token中的用户信息
     *
     * @param token      token信息
     * @param pubKeyPath 公钥
     * @return IJwtInfo
     * @throws Exception 异常
     */
    public static IJwtInfo getInfoFromToken(String token, String pubKeyPath) throws Exception {
        return buildJwtInfo(parserToken(token, pubKeyPath).getBody());
    }

    /**
     * 获取token中的用户信息
     *
     * @param token  token信息
     * @param pubKey 公钥
     * @return IJwtInfo
     * @throws Exception 异常
     */
    public static IJwtInfo getInfoFromToken(String token, byte[] pubKey) throws Exception {
        return buildJwtInfo(parserToken(token, pubKey).getBody());
    }

    /**
     * 获取jwt构建对象
     *
     * @param jwtInfo jwt实体信息
     * @param key     密钥
     * @param expire  过期时间(秒)
     * @return JwtBuilder
     */
    private static JwtBuilder getJwtBuilder(IJwtInfo jwtInfo, Key key, int expire) {
        return Jwts.builder().setSubject(jwtInfo.getUniqueName())
                .setIssuedAt(DateTime.now().toDate())
                .setExpiration(DateTime.now().plusSeconds(expire).toDate())
                .signWith(key, SignatureAlgorithm.RS256);
    }

    /**
     * 构建jwt实体类信息
     *
     * @param claims jwt claims对象
     * @return IJwtInfo 解析后的jwt信息
     */
    private static IJwtInfo buildJwtInfo(Claims claims) {
        JwtInfo info = new JwtInfo(claims.getSubject());
        info.setExpireTime(claims.get(Claims.EXPIRATION, Date.class));
        return info;
    }

    /**
     * 字节数组转16进制字符串
     *
     * @param bytes 字节数组
     * @return String
     */
    public static String toHexString(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 16进制字符串转字节
     *
     * @param hex 字符串
     * @return byte[]
     * @throws IOException io异常
     */
    public static byte[] toBytes(String hex) throws IOException {
        return Base64.getDecoder().decode(hex);
    }
}
