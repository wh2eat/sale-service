package com.idata.sale.service.web.rest.browser.dto;

public class WxpnSignResultDto {

    private String sign;

    private String appId;

    private String noncestr;

    private String timestamp;

    public WxpnSignResultDto() {
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "WxpnSignResultDto [sign=" + sign + ", appId=" + appId + ", noncestr=" + noncestr + ", timestamp="
                + timestamp + "]";
    }

}
