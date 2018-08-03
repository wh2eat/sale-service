package com.idata.sale.service.web.base.service.dto;

import java.io.Serializable;
import java.util.Date;

public class RepairDeviceBaseDto implements Serializable {

    private String invoiceSerialNumber;

    private String packageSerialNumber;

    private String customerRma;

    private String expressName;

    private String expressNumber;

    private Date receiptTime;

    private String contacts;

    private String contactNumber;

    private Integer status;

    public RepairDeviceBaseDto() {
    }

    public String getCustomerRma() {
        return customerRma;
    }

    public void setCustomerRma(String customerRma) {
        this.customerRma = customerRma;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RepairDeviceBaseDto [invoiceSerialNumber=" + invoiceSerialNumber + ", packageSerialNumber="
                + packageSerialNumber + ", customerRma=" + customerRma + ", expressName=" + expressName
                + ", expressNumber=" + expressNumber + ", receiptTime=" + receiptTime + ", contacts=" + contacts
                + ", contactNumber=" + contactNumber + ", status=" + status + "]";
    }

    public String getInvoiceSerialNumber() {
        return invoiceSerialNumber;
    }

    public void setInvoiceSerialNumber(String invoiceSerialNumber) {
        this.invoiceSerialNumber = invoiceSerialNumber;
    }

    public String getPackageSerialNumber() {
        return packageSerialNumber;
    }

    public void setPackageSerialNumber(String packageSerialNumber) {
        this.packageSerialNumber = packageSerialNumber;
    }

    public Date getReceiptTime() {
        return receiptTime;
    }

    public void setReceiptTime(Date receiptTime) {
        this.receiptTime = receiptTime;
    }

}
