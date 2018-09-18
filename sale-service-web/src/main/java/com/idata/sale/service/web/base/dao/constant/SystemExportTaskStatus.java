package com.idata.sale.service.web.base.dao.constant;

public enum SystemExportTaskStatus {

    Created(0), Exporting(1), Completed(9), Failed(-1);

    public int code;

    private SystemExportTaskStatus(int code) {
        this.code = code;
    }
}
