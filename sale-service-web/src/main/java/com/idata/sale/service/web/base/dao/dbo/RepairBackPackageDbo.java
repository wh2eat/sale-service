package com.idata.sale.service.web.base.dao.dbo;

import java.util.Date;

import com.idata.sale.service.web.base.dao.annotation.Column;
import com.idata.sale.service.web.base.dao.annotation.Table;

@Table("repair_back_package")
public class RepairBackPackageDbo {

    private Integer id;

    @Column()
    private String serialNumber;

    @Column()
    private String expressName;

    @Column()
    private String expressNumber;

    @Column()
    private Date deliveryTime;

    @Column()
    private String contacts;

    @Column()
    private String contactNumber;

    @Column()
    private String contactAddress;

    @Column()
    private Integer status;

    @Column()
    private Date createTime;

    @Column()
    private Date updateTime;

    @Column
    private Integer repairStationId;

    @Column
    private Integer createUserId;

    @Column
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public String getRepairStationUdid() {
        return repairStationUdid;
    }

    public void setRepairStationUdid(String repairStationUdid) {
        this.repairStationUdid = repairStationUdid;
    }

    private String repairStationUdid;

    public Integer getRepairStationId() {
        return repairStationId;
    }

    public void setRepairStationId(Integer repairStationId) {
        this.repairStationId = repairStationId;
    }

    public RepairBackPackageDbo() {
        // TODO Auto-generated constructor stub
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

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    public String getExpressNumber() {
        return expressNumber;
    }

    public void setExpressNumber(String expressNumber) {
        this.expressNumber = expressNumber;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
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

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
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
        return "RepairBackPackageDbo [id=" + id + ", serialNumber=" + serialNumber + ", expressName=" + expressName
                + ", expressNumber=" + expressNumber + ", deliveryTime=" + deliveryTime + ", contacts=" + contacts
                + ", contactNumber=" + contactNumber + ", contactAddress=" + contactAddress + ", status=" + status
                + ", createTime=" + createTime + ", updateTime=" + updateTime + ", repairStationId=" + repairStationId
                + ", createUserId=" + createUserId + ", remark=" + remark + ", repairStationUdid=" + repairStationUdid
                + "]";
    }

}