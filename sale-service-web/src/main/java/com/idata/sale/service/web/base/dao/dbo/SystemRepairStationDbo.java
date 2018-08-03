package com.idata.sale.service.web.base.dao.dbo;

import java.util.Date;

import com.idata.sale.service.web.base.dao.annotation.Column;
import com.idata.sale.service.web.base.dao.annotation.Table;

@Table("system_repair_station")
public class SystemRepairStationDbo {

    private Integer id;

    @Column
    private String udid;

    @Column
    private String name;

    @Column
    private String contacts;

    @Column
    private String contactNumber;

    @Column
    private String contactPhone;

    @Column
    private String contactMail;

    @Column
    private String company;

    @Column
    private String address;

    @Column
    private String remark;

    @Column
    private Date createTime;

    @Column
    private Date updateTime;

    public SystemRepairStationDbo() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String uuid) {
        this.udid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactMail() {
        return contactMail;
    }

    public void setContactMail(String contactMail) {
        this.contactMail = contactMail;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "SystemRepairStationDbo [id=" + id + ", udid=" + udid + ", name=" + name + ", contacts=" + contacts
                + ", contactNumber=" + contactNumber + ", contactPhone=" + contactPhone + ", contactMail=" + contactMail
                + ", company=" + company + ", address=" + address + ", remark=" + remark + ", createTime=" + createTime
                + ", updateTime=" + updateTime + "]";
    }

}
