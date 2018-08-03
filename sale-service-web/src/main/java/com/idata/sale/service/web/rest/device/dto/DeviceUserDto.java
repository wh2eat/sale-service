package com.idata.sale.service.web.rest.device.dto;

public class DeviceUserDto {

    private String apiVersion;

    private String data;

    private String dataSignature;

    private long requestMillis;

    private String id;

    private String password;

    private String oldPassword;

    public DeviceUserDto() {

    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDataSignature() {
        return dataSignature;
    }

    public void setDataSignature(String dataSignature) {
        this.dataSignature = dataSignature;
    }

    public long getRequestMillis() {
        return requestMillis;
    }

    public void setRequestMillis(long requestMillis) {
        this.requestMillis = requestMillis;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    @Override
    public String toString() {
        return "UserLoginDto [apiVersion=" + apiVersion + ", data=" + data + ", dataSignature=" + dataSignature
                + ", requestMillis=" + requestMillis + "]";
    }

}
