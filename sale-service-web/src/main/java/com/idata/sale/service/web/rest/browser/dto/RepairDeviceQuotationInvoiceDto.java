package com.idata.sale.service.web.rest.browser.dto;

import java.util.List;

import com.idata.sale.service.web.base.dao.dbo.RepairDeviceQuotationInvoiceDbo;

public class RepairDeviceQuotationInvoiceDto {

    private String userId;

    private String sn;

    private Integer id;

    private Integer detectInvoiceId;

    private Integer confirmStatus;

    private String confirmRemark;

    public Integer getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(Integer confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    public String getConfirmRemark() {
        return confirmRemark;
    }

    public void setConfirmRemark(String confirmRemark) {
        this.confirmRemark = confirmRemark;
    }

    private Integer payType;

    private String payDescription;

    private String costTotal;

    private String laborCosts;

    private Integer currency;

    private List<RepairDeviceQuotationInvoiceDbo> quotationInvoices;

    public RepairDeviceQuotationInvoiceDto() {
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public List<RepairDeviceQuotationInvoiceDbo> getQuotationInvoices() {
        return quotationInvoices;
    }

    public void setQuotationInvoices(List<RepairDeviceQuotationInvoiceDbo> quotationInvoices) {
        this.quotationInvoices = quotationInvoices;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "RepairDeviceQuotationInvoiceDto [userId=" + userId + ", sn=" + sn + ", id=" + id + ", detectInvoiceId="
                + detectInvoiceId + ", confirmStatus=" + confirmStatus + ", confirmRemark=" + confirmRemark
                + ", payType=" + payType + ", payDescription=" + payDescription + ", costTotal=" + costTotal
                + ", laborCosts=" + laborCosts + ", currency=" + currency + ", quotationInvoices=" + quotationInvoices
                + "]";
    }

    public String getCostTotal() {
        return costTotal;
    }

    public void setCostTotal(String costTotal) {
        this.costTotal = costTotal;
    }

    public String getPayDescription() {
        return payDescription;
    }

    public void setPayDescription(String payDescription) {
        this.payDescription = payDescription;
    }

    public String getLaborCosts() {
        return laborCosts;
    }

    public void setLaborCosts(String laborCosts) {
        this.laborCosts = laborCosts;
    }

    public Integer getDetectInvoiceId() {
        return detectInvoiceId;
    }

    public void setDetectInvoiceId(Integer detectInvoiceId) {
        this.detectInvoiceId = detectInvoiceId;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }
}