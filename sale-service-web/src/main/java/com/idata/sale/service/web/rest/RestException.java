package com.idata.sale.service.web.rest;

public class RestException extends Exception {

    private static final long serialVersionUID = 1L;

    private RestCode code;

    public RestException(RestCode code, String msg) {
        super(msg);
        this.code = code;
    }

    public RestException(RestCode code) {
        this(code, null);
    }

    public RestCode getCode() {
        return code;
    }

    public void setCode(RestCode code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "RestException [code=" + code + ", Message=" + getMessage() + "]";
    }

}
