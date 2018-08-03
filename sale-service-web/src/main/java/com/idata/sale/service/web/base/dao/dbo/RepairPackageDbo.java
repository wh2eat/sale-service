package com.idata.sale.service.web.base.dao.dbo;

import java.util.Date;
import java.util.List;

import com.idata.sale.service.web.base.dao.annotation.Column;
import com.idata.sale.service.web.base.dao.annotation.Table;

@Table("repair_package")
public class RepairPackageDbo implements RepairInvoiceDboSet {

    private Integer id;

    @Column()
    private Integer repairInvoiceId;

    private String repairInvoiceSerialNumber;

    @Column()
    private String serialNumber;

    @Column()
    private String expressNumber;

    @Column()
    private String expressName;

    @Column()
    private Date receiptTime;

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
    private Integer repairStationId;

    private List<RepairDeviceDbo> repairDevices;

    public Integer getRepairStationId() {
        return repairStationId;
    }

    public void setRepairStationId(Integer repairStationId) {
        this.repairStationId = repairStationId;
    }

    private RepairInvoiceDbo invoice;

    public RepairPackageDbo() {
        // TODO Auto-generated constructor stub
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getRepairInvoiceId() {
        return repairInvoiceId;
    }

    public void setRepairInvoiceId(Integer repairInvoiceId) {
        this.repairInvoiceId = repairInvoiceId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getExpressNumber() {
        return expressNumber;
    }

    public void setExpressNumber(String expressNumber) {
        this.expressNumber = expressNumber;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    public Date getReceiptTime() {
        return receiptTime;
    }

    public void setReceiptTime(Date receiptTime) {
        this.receiptTime = receiptTime;
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

    @Override
    public String toString() {
        return "RepairPackageDbo [id=" + id + ", repairInvoiceId=" + repairInvoiceId + ", repairInvoiceSerialNumber="
                + repairInvoiceSerialNumber + ", serialNumber=" + serialNumber + ", expressNumber=" + expressNumber
                + ", expressName=" + expressName + ", receiptTime=" + receiptTime + ", contacts=" + contacts
                + ", contactAddress=" + contactAddress + ", contactNumber=" + contactNumber + ", status=" + status
                + ", updateTime=" + updateTime + ", createTime=" + createTime + ", repairStationId=" + repairStationId
                + ", repairDevices=" + repairDevices + ", invoice=" + invoice + "]";
    }

    public String getRepairInvoiceSerialNumber() {
        return repairInvoiceSerialNumber;
    }

    public void setRepairInvoiceSerialNumber(String repairInvoiceSerialNumber) {
        this.repairInvoiceSerialNumber = repairInvoiceSerialNumber;
    }

    public RepairInvoiceDbo getInvoice() {
        return invoice;
    }

    public void setInvoice(RepairInvoiceDbo invoice) {
        this.invoice = invoice;
    }

    @Override
    public void setRepairInvoiceDbo(RepairInvoiceDbo dbo) {
        this.invoice = dbo;
    }

    public List<RepairDeviceDbo> getRepairDevices() {
        return repairDevices;
    }

    public void setRepairDevices(List<RepairDeviceDbo> repairDevices) {
        this.repairDevices = repairDevices;
    }

}