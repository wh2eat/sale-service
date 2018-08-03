package com.idata.sale.service.web.rest;

public enum RestHeader {

    DeviceToken("idata-ss-device-token");

    private String name;

    private RestHeader(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
