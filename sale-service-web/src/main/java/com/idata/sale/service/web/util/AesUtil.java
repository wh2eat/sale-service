package com.idata.sale.service.web.util;

/**
 * Project Name:idata_mdm
 * File Name:AESUtil.java
 * Package Name:com.idata.mdm.util
 * Date:2015年9月7日下午6:03:11
 * Copyright (c) 2015, chenzhou1025@126.com All Rights Reserved.
 *
*/
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author WHD data 2015年8月16日
 */
public class AesUtil {

    private final static Logger LOGGER = LoggerFactory.getLogger(AesUtil.class);

    public static String encrypt(String content, String password) {

        try {
            byte[] encryptedBytes = aes_128_cbc_pkcs5_encrypt(content.getBytes("UTF-8"), password);
            return new Base64().encodeToString(encryptedBytes);
        }
        catch (NullPointerException | UnsupportedEncodingException e) {
            LOGGER.error("encrypt throws " + e.getClass().getName(), e);
        }
        return null;
    }

    /*
     * @param content:
     * 
     * @param password:
     */
    public static byte[] encryptToByte1(String content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(password.getBytes());
            // 使用128 位
            kgen.init(128, secureRandom);
            SecretKey secretKey = kgen.generateKey();
            byte[] encodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(encodeFormat, "AES");
            // Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("AES");
            // 加密内容进行编码
            byte[] byteContent = content.getBytes("utf-8");
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, key);
            // 正式执行加密操作
            byte[] result = cipher.doFinal(byteContent);
            return result;
        }
        catch (NoSuchAlgorithmException e) {
            LOGGER.error("encrypt throws " + e.getClass().getName(), e);
        }
        catch (NoSuchPaddingException e) {
            LOGGER.error("encrypt throws " + e.getClass().getName(), e);
        }
        catch (InvalidKeyException e) {
            LOGGER.error("encrypt throws " + e.getClass().getName(), e);
        }
        catch (UnsupportedEncodingException e) {
            LOGGER.error("encrypt throws " + e.getClass().getName(), e);
        }
        catch (IllegalBlockSizeException e) {
            LOGGER.error("encrypt throws " + e.getClass().getName(), e);
        }
        catch (BadPaddingException e) {
            LOGGER.error("encrypt throws " + e.getClass().getName(), e);
        }
        return null;
    }

    public static String decrypt(String content, String password) {
        try {
            byte[] decoded_encrypted_bytes = new Base64().decode(content);
            byte[] decrypted_bytes = aes_128_cbc_pkcs5_decrypt(decoded_encrypted_bytes, password);
            return new String(decrypted_bytes, "UTF-8");
        }
        catch (Exception e) {
            LOGGER.error("[AesUtil][decrypt][failed]", e);
        }
        return null;
    }

    /**
     * 二进制--》十六进制转化
     * 
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 十六进制--》二进制转化
     * 
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }

        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        // String content = "123456";
        // String password = "idataKey";
        // // 加密
        // System.out.println("加密前：" + content);
        // String encryptResultStr =encrypt(content, password);
        // System.out.println("加密后：" + encryptResultStr);
        //
        // System.err.println(encryptResultStr.length());
        // // 解密
        // //1A35D2BDA7BB293F03009D28053D350B
        // String result = decrypt("dfdsfds", password);
        // System.out.println("解密后：" + result);
        //

        // System.err.println(encrypt("{83fae1b2-2169-478d-8aeb-903206121c22}",MDMConstant.MT_COMMON_SECRETKEY));

        System.err.println(encrypt("A", "aes_test_secret_key"));

    }

    final public static int SECURITY_CRYPTO_BITS = 128;

    public static byte[] sha1Hash(byte[] inputBytes) throws NoSuchAlgorithmException {
        MessageDigest mdInstance = MessageDigest.getInstance("SHA-1");
        mdInstance.update(inputBytes);
        byte[] resultBytes = mdInstance.digest();
        return resultBytes;
    }

    private static SecretKeySpec getSecretKeySpec(String password)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] keyBytes = new byte[SECURITY_CRYPTO_BITS / 8];
        Arrays.fill(keyBytes, (byte) 0x00);

        byte[] passwordBytes = sha1Hash(password.getBytes("UTF-8"));

        int length = passwordBytes.length < keyBytes.length ? passwordBytes.length : keyBytes.length;
        System.arraycopy(passwordBytes, 0, keyBytes, 0, length);

        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

        return keySpec;
    }

    private static IvParameterSpec getIvParameterSpec(String password)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] iv = new byte[16];

        Arrays.fill(iv, (byte) 0x00);

        byte[] passwordBytes = sha1Hash(password.getBytes("UTF-8"));

        int length = passwordBytes.length < iv.length ? passwordBytes.length : iv.length;
        System.arraycopy(passwordBytes, 0, iv, 0, length);

        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        return ivParameterSpec;
    }

    public static byte[] aes_128_cbc_pkcs5_encrypt(byte[] plainBytes, String password) throws NullPointerException {
        if (plainBytes.length == 0 || plainBytes == null) {
            throw new NullPointerException("Invalid parameter - content");
        }

        if (password.length() == 0 || password == null) {
            throw new NullPointerException("Invalid parameter - password");
        }

        try {
            SecretKeySpec secKeySpec = getSecretKeySpec(password);
            IvParameterSpec ivParamSpec = getIvParameterSpec(password);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secKeySpec, ivParamSpec);

            return cipher.doFinal(plainBytes);

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] aes_128_cbc_pkcs5_decrypt(byte[] encryptedBytes, String password) throws NullPointerException {
        if (encryptedBytes.length == 0 || encryptedBytes == null) {
            throw new NullPointerException("Invalid parameter - content");
        }

        if (password.length() == 0 || password == null) {
            throw new NullPointerException("Invalid parameter - password");
        }

        try {
            SecretKeySpec secKeySpec = getSecretKeySpec(password);
            IvParameterSpec ivParamSpec = getIvParameterSpec(password);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secKeySpec, ivParamSpec);

            return cipher.doFinal(encryptedBytes);

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void do_aes_test(String plainText, String password) throws UnsupportedEncodingException {
        System.out.println("原文：|" + plainText + "|");

        byte[] encryptedBytes = aes_128_cbc_pkcs5_encrypt(plainText.getBytes("UTF-8"), password);
        String encodedOutputString = new Base64().encodeToString(encryptedBytes);
        System.out.println("加密：|" + encodedOutputString + "|");

        byte[] decoded_encrypted_bytes = new Base64().decode(encodedOutputString);
        byte[] decrypted_bytes = aes_128_cbc_pkcs5_decrypt(decoded_encrypted_bytes, password);
        String decrypted_string = new String(decrypted_bytes, "UTF-8");
        System.out.println("解密：|" + decrypted_string + "|");
    }

}
