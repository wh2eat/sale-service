package com.idata.sale.service.web.base.dao.dbo;

import java.util.Date;

import com.idata.sale.service.web.base.dao.annotation.Column;
import com.idata.sale.service.web.base.dao.annotation.Table;

@Table("repair_invoice")
public class RepairInvoiceDbo {

    private Integer id;

    @Column()
    private String serialNumber;

    @Column()
    private String customerRma;

    @Column()
    private Date repairDate;

    @Column()
    private String contacts;

    @Column()
    private String contactAddress;

    @Column()
    private String contactNumber;

    @Column()
    private Integer status;

    @Column()
    private Date updateTime;

    @Column()
    private Date createTime;

    @Column
    private String email;

    @Column
    private Integer repairStationId;

    private String repairStationUdid;

    public Integer getRepairStationId() {
        return repairStationId;
    }

    public void setRepairStationId(Integer repairStationId) {
        this.repairStationId = repairStationId;
    }

    public RepairInvoiceDbo() {
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

    public String getCustomerRma() {
        return customerRma;
    }

    public void setCustomerRma(String customerRma) {
        this.customerRma = customerRma;
    }

    public Date getRepairDate() {
        return repairDate;
    }

    public void setRepairDate(Date repairDate) {
        this.repairDate = repairDate;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "RepairInvoiceDbo [id=" + id + ", serialNumber=" + serialNumber + ", customerRma=" + customerRma
                + ", repairDate=" + repairDate + ", contacts=" + contacts + ", contactAddress=" + contactAddress
                + ", contactNumber=" + contactNumber + ", status=" + status + ", updateTime=" + updateTime
                + ", createTime=" + createTime + ", email=" + email + ", repairStationId=" + repairStationId
                + ", repairStationUdid=" + repairStationUdid + "]";
    }

    public String getRepairStationUdid() {
        return repairStationUdid;
    }

    public void setRepairStationUdid(String repairStationUdid) {
        this.repairStationUdid = repairStationUdid;
    }

}