package com.idata.sale.service.web.rest.browser.dto;

import java.util.Map;

public class RequestPageDto {

    private int page;

    private int limit;

    private Map<String, String> params;

    public RequestPageDto() {
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "RequestPageDto [page=" + page + ", limit=" + limit + ", params=" + params + "]";
    }

}
