package com.idata.sale.service.web.base.service.dto;

public class SystemUserTokenDto {

    private String loginName;

    private String token;

    private String key;

    private long expiredMillis;

    private long getMillis;

    public SystemUserTokenDto(String token, String key) {
        this.token = token;
        this.key = key;
    }

    public SystemUserTokenDto() {
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpiredMillis() {
        return expiredMillis;
    }

    public void setExpiredMillis(long expiredMillis) {
        this.expiredMillis = expiredMillis;
    }

    public long getGetMillis() {
        return getMillis;
    }

    public void setGetMillis(long getMillis) {
        this.getMillis = getMillis;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "SystemUserTokenDto [loginName=" + loginName + ", token=" + token + ", key=" + key + ", expiredMillis="
                + expiredMillis + ", getMillis=" + getMillis + "]";
    }

}
