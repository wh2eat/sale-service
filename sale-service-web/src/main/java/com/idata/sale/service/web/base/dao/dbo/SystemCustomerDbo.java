package com.idata.sale.service.web.base.dao.dbo;

import java.util.Date;

import com.idata.sale.service.web.base.dao.annotation.Column;
import com.idata.sale.service.web.base.dao.annotation.Table;

@Table("system_customer")
public class SystemCustomerDbo {

    private Integer id;

    @Column
    private String name;

    @Column
    private String phone;

    @Column
    private String phoneBackup;

    @Column
    private String phoneFix;

    @Column
    private String address;

    @Column
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column
    private String company;

    @Column
    private String remark;

    @Column
    private Date createTime;

    @Column
    private Date updateTime;

    @Column
    private String identifier;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public SystemCustomerDbo() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneBackup() {
        return phoneBackup;
    }

    public void setPhoneBackup(String phoneBackup) {
        this.phoneBackup = phoneBackup;
    }

    public String getPhoneFix() {
        return phoneFix;
    }

    public void setPhoneFix(String phoneFix) {
        this.phoneFix = phoneFix;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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
        return "SystemCustomerDbo [id=" + id + ", name=" + name + ", phone=" + phone + ", phoneBackup=" + phoneBackup
                + ", phoneFix=" + phoneFix + ", address=" + address + ", email=" + email + ", company=" + company
                + ", remark=" + remark + ", createTime=" + createTime + ", updateTime=" + updateTime + ", identifier="
                + identifier + "]";
    }

}
