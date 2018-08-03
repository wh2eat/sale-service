package com.idata.sale.service.web.base.dao;

import java.util.Map;

public class PageInfo {

    private Integer sEcho;

    private int pageSize;

    private int pageNum;

    private long total;

    private int pageTotal = -1;

    private Map<String, String> conditions;

    private Map<String, String> sorts;

    public PageInfo() {

    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public Map<String, String> getConditions() {
        return conditions;
    }

    public void setConditions(Map<String, String> conditions) {
        this.conditions = conditions;
    }

    /**
     * sEcho.
     *
     * @return the sEcho
     */
    public Integer getsEcho() {
        return sEcho;
    }

    /**
     * sEcho.
     *
     * @param sEcho the sEcho to set
     */
    public void setsEcho(Integer sEcho) {
        if (null != sEcho) {
            sEcho++;
        }
        else {
            sEcho = 1;
        }
    }

    public Map<String, String> getSorts() {
        return sorts;
    }

    public void setSorts(Map<String, String> sorts) {
        this.sorts = sorts;
    }

}
