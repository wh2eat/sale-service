package com.idata.sale.service.web.base.dao.constant;

public enum Device {

    CurrencyRMB(0), CurrencyUSD(1),

    QuotedPrice(1), QuotedPriceNot(0),

    Charge(1), ChargeNot(0),

    PayNot(0), PayUserRefuse(1), PayFinish(9),

    PayTypeCash(0), PayTypeMonth(1);

    private int code;

    private Device(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

}
