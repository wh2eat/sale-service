package com.idata.sale.service.web.rest.browser.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.idata.sale.service.web.base.service.ServiceException;
import com.idata.sale.service.web.base.tool.service.HttpClientService;
import com.idata.sale.service.web.base.tool.service.HttpClientService.HttpResponse;
import com.idata.sale.service.web.base.wx.service.IWxpnService;
import com.idata.sale.service.web.base.wx.service.impl.WxpnService;
import com.idata.sale.service.web.rest.RestException;
import com.idata.sale.service.web.rest.browser.dto.WxpnSignDto;
import com.idata.sale.service.web.rest.browser.dto.WxpnSignResultDto;

@RestController
@RequestMapping(path = "/api/browser/wxpn/", produces = { MediaType.APPLICATION_JSON_VALUE })
public class WxpnRestService {

    private final static Logger LOGGER = LoggerFactory.getLogger(WxpnRestService.class);

    public WxpnRestService() {

    }

    @Autowired
    private IWxpnService wxpnService;

    @Autowired
    private HttpClientService httpClient;

    @RequestMapping(path = "get/sign", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE })
    public Object getSign(@RequestBody WxpnSignDto signDto) throws ServiceException, RestException {

        String url = "http://120.76.27.230:8580/web/api/browser/wxpn/get/sign";

        Map<String, Object> maps = new HashMap<>();
        maps.put("url", signDto.getUrl());

        HttpResponse response = null;
        try {
            response = httpClient.doPost(url, maps);
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String body = response.getBody();
        LOGGER.info("[][][" + body + "]");
        JSONObject jsonBody = JSONObject.parseObject(body);
        JSONObject rtnJson = jsonBody.getJSONObject("rtn");
        LOGGER.info("[][][" + rtnJson + "]");

        // if (LOGGER.isDebugEnabled()) {
        // LOGGER.debug("[][getSign][" + signDto + "]");
        // }
        //
        // if (StringUtils.isEmpty(signDto.getUrl())) {
        // LOGGER.error("[][getSign][failed,url is empty]");
        // throw new RestException(RestCode.FieldIsEmpty, "url");
        // }
        //
        // String jsapiTicket = wxpnService.getJsapiTicket();
        // if (null == jsapiTicket) {
        // LOGGER.error("[][getSign][get jsapiTicket failed]");
        // throw new RestException(RestCode.ServiceException, "jsapiTicket get failed");
        // }
        //
        // String noncestr = RandomStringUtils.randomAlphabetic(8);
        //
        // String timestamp = System.currentTimeMillis() + "";
        //
        // String sign = wxpnService.sign(noncestr, jsapiTicket, timestamp,
        // signDto.getUrl());
        //
        WxpnSignResultDto resultDto = new WxpnSignResultDto();

        resultDto.setAppId(WxpnService.appId);

        resultDto.setNoncestr(rtnJson.getString("noncestr"));
        resultDto.setTimestamp(rtnJson.getString("timestamp"));
        resultDto.setSign(rtnJson.getString("sign"));

        return resultDto;

    }

    // @RequestMapping(path = "get", method = RequestMethod.GET)
    // public Object getUser(@RequestParam("fieldName") String fieldName,
    // @RequestParam("fieldValue") String fieldValue)
    // throws ServiceException, RestException {
    //
    // }

    public static void main(String[] args) {
        System.out.println(RandomStringUtils.randomAlphabetic(8));
    }

}
