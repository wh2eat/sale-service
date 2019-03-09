package com.idata.sale.service.web.base.dao.dbo;

import java.util.Date;

import com.idata.sale.service.web.base.dao.annotation.Column;
import com.idata.sale.service.web.base.dao.annotation.Table;

@Table("customer_suggestion")
public class CustomerSuggestionDbo {

    private Integer id;

    @Column
    private String serialNumber;

    @Column
    private String wxUserId;

    @Column
    private String contact;

    @Column
    private String telephone;

    @Column
    private String category;

    @Column
    private Integer deviceAmount;

    @Column
    private String description;

    @Column
    private String attachment;

    @Column
    private String status;

    @Column
    private Date createTime;

    @Column
    private Date updateTime;

    public CustomerSuggestionDbo() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getWxUserId() {
        return wxUserId;
    }

    public void setWxUserId(String wxUserId) {
        this.wxUserId = wxUserId;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getDeviceAmount() {
        return deviceAmount;
    }

    public void setDeviceAmount(Integer deviceAmount) {
        this.deviceAmount = deviceAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        return "CustomerSuggestionDbo [id=" + id + ", serialNumber=" + serialNumber + ", wxUserId=" + wxUserId
                + ", contact=" + contact + ", telephone=" + telephone + ", category=" + category + ", deviceAmount="
                + deviceAmount + ", description=" + description + ", attachment=" + attachment + ", status=" + status
                + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
    }

}
