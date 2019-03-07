package com.idata.sale.service.web.base.wx.service;

public interface IWxpnService {

    public String getJsapiTicket();

    public String sign(String noncestr, String jsapiTicket, String timestamp, String url);

}
