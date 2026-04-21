package com.uneed.common.support.jwt;

import com.uneed.common.core.collection.map.Maps;
import com.uneed.common.support.constant.AuthConstant;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

import static com.uneed.common.core.lang.ObjectUtil.isNull;

/**
 * <p>
 * rsa加密工具
 * </p>
 *
 * @author hcs
 * @date 2019/12/27
 */
public class RsaTool {

    /**
     * 算法
     */
    private static final String ALGORITHM = "RSA";

    /**
     * key的大小
     */
    private static final int KEY_SIZE = 2048;

    /**
     * 定义加密工具的私有属性
     */
    private static RsaTool instance = null;

    /**
     * 构造方法私有化，外部不能new
     */
    private RsaTool() {

    }

    /**
     * 提供一个公有的静态方法，返回实例对象
     *
     * @return RsaTool
     */
    public static RsaTool getInstance() {
        if (instance == null) {
            instance = new RsaTool();
        }
        return instance;
    }

    /**
     * 文件中获取公钥
     *
     * @param filename 文件名
     * @return PublicKey 公钥
     * @throws Exception 异常信息
     */
    public PublicKey getPublicKey(String filename) throws Exception {
        return getPublicKey(getStreamKey(filename));
    }

    /**
     * 文件中获取私钥
     *
     * @param filename 文件名
     * @return PrivateKey 私钥
     * @throws Exception 异常信息
     */
    public PrivateKey getPrivateKey(String filename) throws Exception {
        return getPrivateKey(getStreamKey(filename));
    }

    /**
     * 获取公钥
     *
     * @param publicKey 公钥的key
     * @return PublicKey 公钥
     * @throws Exception 异常信息
     */
    public PublicKey getPublicKey(byte[] publicKey) throws Exception {
        return KeyFactory.getInstance(ALGORITHM).generatePublic(new X509EncodedKeySpec(publicKey));
    }

    /**
     * 获取密钥
     *
     * @param privateKey 私钥的key
     * @return PrivateKey 私钥
     * @throws Exception 异常信息
     */
    public PrivateKey getPrivateKey(byte[] privateKey) throws Exception {
        return KeyFactory.getInstance(ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(privateKey));
    }

    /**
     * 文件生成rsa公钥和密钥
     *
     * @param publicKeyFilename  公钥存放文件
     * @param privateKeyFilename 私钥存放文件
     * @param secret             密码
     * @throws Exception 异常信息
     */
    public void generateKey(String publicKeyFilename, String privateKeyFilename, String secret) throws Exception {
        KeyPair pair = getKeyPair(secret);
        byte[] publicBytes = pair.getPublic().getEncoded();
        writeKey(publicKeyFilename, publicBytes);
        byte[] privateBytes = pair.getPrivate().getEncoded();
        writeKey(privateKeyFilename, privateBytes);
    }

    /**
     * 生成rsa公钥
     *
     * @param secret 密码
     * @throws NoSuchAlgorithmException 算法异常
     */
    public byte[] generatePublicKey(String secret) throws NoSuchAlgorithmException {
        return getKeyPair(secret).getPublic().getEncoded();
    }

    /**
     * 生存rsa私钥
     *
     * @param secret 密码
     * @throws NoSuchAlgorithmException 算法异常
     */
    public byte[] generatePrivateKey(String secret) throws NoSuchAlgorithmException {
        return getKeyPair(secret).getPrivate().getEncoded();
    }

    /**
     * 生成rsa公钥、私钥，封装到map中，并返回
     *
     * @param secret 密码
     * @return Map<String, byte [ ]>
     * @throws NoSuchAlgorithmException 算法异常
     */
    public Map<String, byte[]> generateKey(String secret) throws NoSuchAlgorithmException {
        //获取钥匙对对象
        KeyPair pair = getKeyPair(secret);
        //构建map对象
        Map<String, byte[]> map = Maps.newHashMap();
        //保存公钥
        map.put(AuthConstant.GENERATE_PUB_KEY, pair.getPublic().getEncoded());
        //保存私钥
        map.put(AuthConstant.GENERATE_PRI_KEY, pair.getPrivate().getEncoded());
        return map;
    }

    /**
     * 文件中获取密钥key
     *
     * @param file 密钥文件
     * @return byte[]
     * @throws Exception 异常信息
     */
    private byte[] getStreamKey(String file) throws Exception {
        InputStream resource = this.getClass().getClassLoader().getResourceAsStream(file);
        if (isNull(resource)) {
            throw new Exception("the resource as stream can't be null");
        }
        DataInputStream dis = new DataInputStream(resource);
        byte[] keys = new byte[resource.available()];
        dis.readFully(keys);
        dis.close();
        return keys;
    }

    /**
     * 将密钥写入文件中
     *
     * @param filename 文件名
     * @param bytes    密钥字节
     * @throws IOException io异常
     */
    private void writeKey(String filename, byte[] bytes) throws IOException {
        FileOutputStream stream = new FileOutputStream(filename);
        stream.write(bytes);
        stream.close();
    }

    /**
     * 获取钥匙对
     *
     * @param secret 密码
     * @return KeyPair
     * @throws NoSuchAlgorithmException 算法异常
     */
    private KeyPair getKeyPair(String secret) throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
        generator.initialize(KEY_SIZE, new SecureRandom(secret.getBytes()));
        return generator.genKeyPair();
    }

}

