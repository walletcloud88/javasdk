package com.wallet.api.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.stream.Collectors;

public class SignUtils {
    private static String KEY_RSA_TYPE = "RSA";

    /**
     * 获取私钥
     *
     * @param privateKey 私钥字符串
     * @return
     */
    public static PrivateKey getPrivateKey(String privateKey) throws Exception {

        privateKey = privateKey
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        KeyFactory keyFactory = KeyFactory.getInstance(KEY_RSA_TYPE);
        byte[] decodedKey = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 获取公钥
     *
     * @param publicKey 公钥字符串
     * @return
     */
    public static PublicKey getPublicKey(String publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_RSA_TYPE);
        byte[] decodedKey = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 签名
     *
     * @param data       待签名数据
     * @param privateKey 私钥
     * @return 签名
     */
    public static String sign(String data, PrivateKey privateKey) throws Exception {
        byte[] keyBytes = privateKey.getEncoded();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_RSA_TYPE);
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(key);
        signature.update(data.getBytes());
        return Base64.getEncoder().encodeToString(signature.sign());
    }

    /**
     * 验签
     *
     * @param data      map数据
     * @param publicKey 公钥
     * @param sign      签名
     * @return 是否验签通过
     */
    public static boolean verify(Map data, String publicKey, String sign) {
        try {
            if (sign == null) {
                throw new IllegalArgumentException("签名参数为空");
            }
            byte[] keyBytes = getPublicKey(publicKey).getEncoded();
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_RSA_TYPE);
            PublicKey key = keyFactory.generatePublic(keySpec);
            Signature signature = Signature.getInstance("MD5withRSA");
            signature.initVerify(key);
            signature.update(map2Str(data).getBytes());
            return signature.verify(Base64.getDecoder().decode(sign));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 参数签名方法
     *
     * @param param
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String sign(Map param, String privateKey) {
        try {
            String paramStr = map2Str(param);
            System.out.println(paramStr);
            return sign(paramStr, getPrivateKey(privateKey));
        } catch (Exception e) {
            throw new RuntimeException("签名失败", e);
        }
    }

    /**
     * map对象转 key自然排序的 key=val&key=val 字符串
     *
     * @param param
     * @return
     */
    private static String map2Str(Map param) {
        return new TreeMap<String, Object>(param).entrySet().stream().filter(en -> !en.getKey().equals("sign")).map(en -> en.getKey() + "=" + obj2str(en.getValue())).collect(Collectors.joining("&"));
    }

    /**
     * 对象转换 字符串
     *
     * @param o
     * @return
     */
    private static String obj2str(Object o) {
        if (o instanceof Collection) {
            return ((Collection<Object>) o).stream().map(x -> obj2str(x)).collect(Collectors.joining(""));
        }
        if (o != null && o.getClass().isArray()) {
            return Arrays.stream((Object[]) o).map(x -> obj2str(x)).collect(Collectors.joining(""));
        }
        if (o instanceof Map) {
            return ((Map<? extends String, ?>) o).entrySet().stream().filter(en -> !en.getKey().equals("sign")).map(en -> obj2str(en.getValue())).collect(Collectors.joining(""));
        }
        return Objects.toString(o);
    }


}