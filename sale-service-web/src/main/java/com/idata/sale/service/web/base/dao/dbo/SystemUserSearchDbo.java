package com.idata.sale.service.web.base.dao.dbo;

import java.util.Date;

import com.idata.sale.service.web.base.dao.annotation.Column;
import com.idata.sale.service.web.base.dao.annotation.Table;

@Table("system_user_search")
public class SystemUserSearchDbo {

    private Integer id;

    @Column
    private Integer searchType;

    @Column
    private String searchContent;

    @Column
    private Date searchTime;

    @Column
    private String ip;

    @Column
    private String ipPosition;

    @Column
    private Integer accessType;

    @Column
    private Date createTime;

    public SystemUserSearchDbo() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSearchType() {
        return searchType;
    }

    public void setSearchType(Integer searchType) {
        this.searchType = searchType;
    }

    public String getSearchContent() {
        return searchContent;
    }

    public void setSearchContent(String searchContent) {
        this.searchContent = searchContent;
    }

    public Date getSearchTime() {
        return searchTime;
    }

    public void setSearchTime(Date searchTime) {
        this.searchTime = searchTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIpPosition() {
        return ipPosition;
    }

    public void setIpPosition(String ipPosition) {
        this.ipPosition = ipPosition;
    }

    public Integer getAccessType() {
        return accessType;
    }

    public void setAccessType(Integer accessType) {
        this.accessType = accessType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "SystemUserSearchDbo [id=" + id + ", searchType=" + searchType + ", searchContent=" + searchContent
                + ", searchTime=" + searchTime + ", ip=" + ip + ", ipPosition=" + ipPosition + ", accessType="
                + accessType + ", createTime=" + createTime + "]";
    }

}
