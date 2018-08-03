package com.idata.sale.service.web.base.dao.constant;

public enum RepairDeviceQuotationConfirmStatus {

    New(0), Refused(1), Confirmed(9);

    public int code;

    private RepairDeviceQuotationConfirmStatus(int code) {
        this.code = code;
    }

}
