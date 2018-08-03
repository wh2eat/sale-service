package com.idata.sale.service.web.base.service;

import com.idata.sale.service.web.base.service.constant.ServiceCode;

public class ServiceException extends Exception {

    private static final long serialVersionUID = 1L;

    private ServiceCode code;

    public ServiceException(ServiceCode code, String msg) {
        super(msg);
        this.setCode(code);
    }

    public ServiceCode getCode() {
        return code;
    }

    public void setCode(ServiceCode code) {
        this.code = code;
    }

}
