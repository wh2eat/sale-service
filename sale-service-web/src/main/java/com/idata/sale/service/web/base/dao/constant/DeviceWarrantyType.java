package com.idata.sale.service.web.base.dao.constant;

public enum DeviceWarrantyType {

    OutWarranty(0, "OutWarranty"),

    UnderWarranty(10, "UnderWarranty"), AgreementWarranty(20, "AgreementWarranty"), AgreementWholeWarranty(30,
            "AgreementWholeWarranty");

    int code;

    String name;

    private DeviceWarrantyType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

}
