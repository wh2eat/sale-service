package com.idata.sale.service.web.base.dao.dbo;

import com.idata.sale.service.web.base.dao.annotation.Column;
import com.idata.sale.service.web.base.dao.annotation.Table;

@Table("system_user_operate_record")
public class SystemUserOperateRecordDbo {

    private Integer id;

    @Column()
    private Integer userId;

    @Column()
    private String userName;

    @Column()
    private String model;

    @Column()
    private String description;

    @Column()
    private String createTime;

    public SystemUserOperateRecordDbo() {
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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "SystemUserOperateRecordDbo [id=" + id + ", userId=" + userId + ", userName=" + userName + ", model="
                + model + ", description=" + description + ", createTime=" + createTime + "]";
    }

}