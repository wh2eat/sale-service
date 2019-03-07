package com.idata.sale.service.web.rest.browser.dto;

public class WxpnSignDto {

    private String url;

    public WxpnSignDto() {

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "WxpnSignDto [ url=" + url + "]";
    }

}
