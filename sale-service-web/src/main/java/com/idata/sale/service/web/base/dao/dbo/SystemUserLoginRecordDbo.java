package com.idata.sale.service.web.base.dao.dbo;

import java.util.Date;

import com.idata.sale.service.web.base.dao.annotation.Column;
import com.idata.sale.service.web.base.dao.annotation.Table;

@Table("system_user_login_record")
public class SystemUserLoginRecordDbo {

    private Integer id;

    @Column()
    private Integer userId;

    @Column()
    private String userName;

    @Column()
    private String ip;

    @Column()
    private String description;

    @Column()
    private Date createTime;

    public SystemUserLoginRecordDbo() {
        // TODO Auto-generated constructor stub
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "SystemUserLoginRecordDbo [id=" + id + ", userId=" + userId + ", userName=" + userName + ", ip=" + ip
                + ", description=" + description + ", createTime=" + createTime + "]";
    }

}