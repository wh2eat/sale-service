package com.idata.sale.service.web.rest.device;

public enum DeviceApi {

    v10("v1.0", "iData2018!SaService");

    private String version;

    private String key;

    private DeviceApi(String version, String key) {
        this.version = version;
        this.key = key;
    }

    public static String getKey(String version) {

        if (v10.version.equals(version)) {
            return v10.key;
        }

        return null;
    }

    public String getKey() {
        return key;
    }

}
