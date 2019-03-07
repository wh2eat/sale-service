package com.idata.sale.service.web.base.wx.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.idata.sale.service.web.base.tool.service.HttpClientService;
import com.idata.sale.service.web.base.wx.service.IWxpnService;

@Service
public class WxpnService implements IWxpnService {

    private final static Logger LOGGER = LoggerFactory.getLogger(WxpnService.class);

    public final static String appId = "wxa7d390e051eaa882";

    private final static String appSecret = "b7f732e12acfe3962aac8c95f9e3ba87";

    private volatile static String jsapiTicket = null;

    private volatile static long jsapiTicketUpdateMillis = 0l;

    private volatile static String accessToken = null;

    private volatile static long accessTokenUpdateMillis = 0l;

    private final static long tokenTimeoutMillis = 2 * 60 * 60 * 1000 - 1 * 60 * 1000;

    public WxpnService() {

    }

    @PostConstruct
    public void init() {

        LOGGER.info("[][WxpnService][init start]");

        getAccessToken();

        getJsapiTicket();

        LOGGER.info("[][WxpnService][init end]");

    }

    @Autowired
    private HttpClientService httpClient;

    public String getAccessToken() {

        if (null != accessToken && (System.currentTimeMillis() - accessTokenUpdateMillis) < tokenTimeoutMillis) {
            return accessToken;
        }

        synchronized (WxpnService.class) {

            if (null != accessToken && (System.currentTimeMillis() - accessTokenUpdateMillis) < tokenTimeoutMillis) {
                return accessToken;
            }

            String requestUrl = "https://api.weixin.qq.com/cgi-bin/token";

            Map<String, Object> params = new HashMap<>();
            params.put("grant_type", "client_credential");
            params.put("appid", appId);
            params.put("secret", appSecret);

            try {
                String response = httpClient.doGet(requestUrl, params);
                JSONObject responseJson = JSONObject.parseObject(response);
                String token = responseJson.getString("access_token");
                if (StringUtils.isNotBlank(token)) {

                    WxpnService.accessToken = token;
                    WxpnService.accessTokenUpdateMillis = System.currentTimeMillis();

                    LOGGER.info("[][getAccessToken][success,token:" + token + "]");

                    return accessToken;
                }

            }
            catch (Exception e) {
                LOGGER.error("[][getAccessToken][failed]", e);
            }
        }

        return null;
    }

    @Override
    public String getJsapiTicket() {

        if (null != WxpnService.jsapiTicket
                && (System.currentTimeMillis() - jsapiTicketUpdateMillis) < tokenTimeoutMillis) {
            return WxpnService.jsapiTicket;
        }

        String accessToken = getAccessToken();
        if (null == accessToken) {
            LOGGER.error("[][getJsapiTicket][failed,]");
            return null;
        }

        synchronized (WxpnService.class) {

            if (null != WxpnService.jsapiTicket
                    && (System.currentTimeMillis() - jsapiTicketUpdateMillis) < tokenTimeoutMillis) {
                return WxpnService.jsapiTicket;
            }

            String requestUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";

            Map<String, Object> params = new HashMap<>();
            params.put("access_token", accessToken);
            params.put("type", "jsapi");

            try {
                String response = httpClient.doGet(requestUrl, params);
                JSONObject responseJson = JSONObject.parseObject(response);
                String ticket = responseJson.getString("ticket");
                if (StringUtils.isNotBlank(ticket)) {

                    WxpnService.jsapiTicket = ticket;
                    WxpnService.jsapiTicketUpdateMillis = System.currentTimeMillis();

                    LOGGER.info("[][getAccessToken][success,token:" + ticket + "]");

                    return WxpnService.jsapiTicket;
                }
            }
            catch (Exception e) {
                LOGGER.error("[][getAccessToken][failed]", e);
            }
        }

        return null;
    }

    @Override
    public String sign(String noncestr, String jsapiTicket, String timestamp, String url) {

        Map<String, String> params = new HashMap<>(4);
        params.put("noncestr", noncestr);
        params.put("jsapi_ticket", jsapiTicket);
        params.put("timestamp", timestamp);
        params.put("url", url);

        List<String> names = new ArrayList<>();
        names.add("noncestr");
        names.add("jsapi_ticket");
        names.add("timestamp");
        names.add("url");

        Collections.sort(names);

        StringBuilder builder = new StringBuilder();
        int size = names.size();
        for (int i = 0; i < size; i++) {
            builder.append(names.get(i)).append("=").append(params.get(names.get(i)));
            if ((i + 1) != size) {
                builder.append("&");
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][sign][builder str:" + builder.toString() + "]");
        }

        String sign = DigestUtils.shaHex(builder.toString());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][sign][sha sign:" + sign + "]");
        }

        return sign;

    }

    public static void main(String[] args) {
        System.out.println(DigestUtils.shaHex(
                "jsapi_ticket=sM4AOVdWfPE4DxkXGEs8VMCPGGVi4C3VM0P37wVUCFvkVAy_90u5h9nbSlYy3-Sl-HhTdfl2fzFy1AOcHKP7qg&noncestr=Wm3WZYTPz0wzccnW&timestamp=1414587457&url=http://mp.weixin.qq.com?params=value"));
    }

}
