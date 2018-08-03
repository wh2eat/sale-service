package com.idata.sale.service.web.base.dao;

public class BaseDaoException extends Exception {

    private BaseDaoCode code;

    public BaseDaoException(BaseDaoCode code, String message) {
        super(message);
        this.setCode(code);
    }

    public BaseDaoCode getCode() {
        return code;
    }

    public void setCode(BaseDaoCode code) {
        this.code = code;
    }

}
