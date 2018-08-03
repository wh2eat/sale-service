package com.idata.sale.service.web.base.dao.constant;

public enum RepairResult {

    Success(100, "Repair Success"), FailUserRefusePay(1, "Fail User Refuse Pay");

    public int code;

    public String name;

    private RepairResult(int code, String name) {
        this.code = code;
        this.name = name;
    }

}
