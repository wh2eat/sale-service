package com.idata.sale.service.web.rest;

public class RestResult {

    private String sid;

    private int code;

    private String message;

    private long useMillis;

    private Object rtn;

    public RestResult() {

    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getRtn() {
        return rtn;
    }

    public void setRtn(Object rtn) {
        this.rtn = rtn;
    }

    public long getUseMillis() {
        return useMillis;
    }

    public void setUseMillis(long useMillis) {
        this.useMillis = useMillis;
    }

    @Override
    public String toString() {
        return "RestResult [sid=" + sid + ", code=" + code + ", message=" + message + ", useMillis=" + useMillis
                + ", rtn=" + rtn + "]";
    }

}
