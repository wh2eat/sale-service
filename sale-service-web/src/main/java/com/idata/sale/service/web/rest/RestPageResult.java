package com.idata.sale.service.web.rest;

import java.util.List;

public class RestPageResult {

    private int code;

    private String msg;

    private long count;

    private List data;

    public RestPageResult() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RestPageResult [code=" + code + ", msg=" + msg + ", count=" + count + ", data=" + data + "]";
    }

}
