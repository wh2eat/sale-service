package com.idata.sale.service.web.rest.device;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.codec.digest.DigestUtils;

import com.alibaba.fastjson.JSONObject;
import com.idata.sale.service.web.util.AesUtil;

public class UserDeviceServiceTest {

    public static void main(String[] args) throws UnsupportedEncodingException {

        Integer userId = 2;

        String udid = "bcceb108-56ac-3967-9a9a-bd30db69461a";

        String password = "admin";

        String eps = getEncryptPassword(userId, udid, password);
        System.out.println(eps);
        eps = URLEncoder.encode(eps, "utf-8");
        System.out.println(eps);
        System.out.println("--------------------");

        de();

        System.out.println("=========================");
        System.out.println("login data:");
        generateLoginData();

        System.out.println("=========================");

        System.out.println("");
        System.out.println("=========================");
        generateGetTokenData();

        System.out.println("=========================");
    }

    public static void de() {
        String data = "qiv6408AL97b8XlKB5ekHPSzOvz1RBxyI5l2WEoByuQ=\n";

        String deData = AesUtil.decrypt(data, "iData2018!SaService");

        System.out.println(deData);

    }

    public static void generateLoginData() {
        String loginName = "admin";
        System.out.println("loginName:" + loginName);
        String password = "admin";
        System.out.println("password:" + password);
        long millis = System.currentTimeMillis();
        System.out.println("requestMillis:" + millis);

        String encodeStr = null;
        try {
            encodeStr = URLEncoder.encode(loginName, "utf-8") + "&" + URLEncoder.encode(password, "utf-8") + "&"
                    + URLEncoder.encode(millis + "", "utf-8");
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println(encodeStr);

        String dataSign = DigestUtils.md5Hex(encodeStr);

        String dataEncrypt = AesUtil.encrypt(encodeStr, "iData2018!SaService");

        JSONObject jsonObject = new JSONObject();
        String apiVersion = "v1.0";
        jsonObject.put("apiVersion", apiVersion);
        jsonObject.put("data", dataEncrypt);
        jsonObject.put("dataSignature", dataSign);
        jsonObject.put("requestMillis", millis);

        System.out.println(jsonObject.toJSONString());

        String token = "fEIkH85qsVNXlRC0+7hH4FPbGUJjGklGZMmATWATHnG+oX/OREK5EfsIKRKTdw3p\r\n";
        token = AesUtil.decrypt(token, "iData2018!SaService");
        System.out.println(token);

    }

    public static void generateGetTokenData() {
        String loginName = "admin";
        long millis = System.currentTimeMillis();
        String tokenKey = "HA98q2";

        String encodeStr = null;
        try {
            encodeStr = URLEncoder.encode(loginName, "utf-8") + "&" + URLEncoder.encode(millis + "", "utf-8");
        }
        catch (UnsupportedEncodingException e) {

        }

        System.out.println(encodeStr);
        String dataEncrypt = AesUtil.encrypt(encodeStr, tokenKey);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("apiVersion", "v1.0");
        jsonObject.put("data", dataEncrypt);
        jsonObject.put("requestMillis", millis);

        System.out.println(jsonObject.toJSONString());

        String newToken = "c3O9rIg4GFFpD4tWZuU+d//67HB4H48H57BMrXHaBp8LQmPdpv0W9Gs6MQItgfpD\r\n";
        System.out.println(AesUtil.decrypt(newToken, "iData2018!SaService"));
    }

    private static String getUserEncryptKey(Integer userId, String loginName) {

        String all = userId + "&" + loginName;
        all = DigestUtils.md5Hex(all);
        return all.substring(0, 4) + all.substring(all.length() - 4, all.length());
    }

    private final static String userEncryptSalt = "iDtS2UserA";

    private static String encryptUserPassword(String loginName, String password, String userKey) {
        try {
            String str = URLEncoder.encode(loginName, "utf-8") + "&" + URLEncoder.encode(password, "utf-8");
            String strMd5 = DigestUtils.md5Hex(str) + userEncryptSalt;

            return AesUtil.encrypt(strMd5, userKey);
        }
        catch (UnsupportedEncodingException e) {

        }
        return null;
    }

    private static String getEncryptPassword(Integer userId, String udid, String password) {
        String userKey = getUserEncryptKey(userId, udid);
        return encryptUserPassword(udid, password, userKey);
    }

}
