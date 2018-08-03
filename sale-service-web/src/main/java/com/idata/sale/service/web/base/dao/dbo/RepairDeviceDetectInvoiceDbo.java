package com.idata.sale.service.web.base.dao.dbo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.idata.sale.service.web.base.dao.annotation.Column;
import com.idata.sale.service.web.base.dao.annotation.Table;

@Table("repair_device_detect_invoice")
public class RepairDeviceDetectInvoiceDbo {

    private Integer id;

    @Column()
    private Integer deviceId;

    @Column()
    private Integer repairDeviceId;

    @JSONField(serialize = false)
    @Column()
    private String sn;

    @Column()
    private String malfunctionAppearance;

    @Column()
    private String malfunctionReason;

    @Column()
    private String malfunctionType;

    @Column()
    private String responsibleParty;

    @Column()
    private String repairSuggest;

    @Column()
    private String repairResult;

    @Column()
    private Integer quotedPrice;

    @Column()
    private Date createTime;

    @Column()
    private Date updateTime;

    @Column
    @JSONField(serialize = false)
    private Integer createUserId;

    private RepairDeviceQuotationInvoiceDbo quotationInvoice;

    public RepairDeviceDetectInvoiceDbo() {
        // TODO Auto-generated constructor stub
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getRepairDeviceId() {
        return repairDeviceId;
    }

    public void setRepairDeviceId(Integer repairDeviceId) {
        this.repairDeviceId = repairDeviceId;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getMalfunctionAppearance() {
        return malfunctionAppearance;
    }

    public void setMalfunctionAppearance(String malfunctionAppearance) {
        this.malfunctionAppearance = malfunctionAppearance;
    }

    public String getMalfunctionReason() {
        return malfunctionReason;
    }

    public void setMalfunctionReason(String malfunctionReason) {
        this.malfunctionReason = malfunctionReason;
    }

    public String getMalfunctionType() {
        return malfunctionType;
    }

    public void setMalfunctionType(String malfunctionType) {
        this.malfunctionType = malfunctionType;
    }

    public String getResponsibleParty() {
        return responsibleParty;
    }

    public void setResponsibleParty(String responsibleParty) {
        this.responsibleParty = responsibleParty;
    }

    public String getRepairSuggest() {
        return repairSuggest;
    }

    public void setRepairSuggest(String repairSuggest) {
        this.repairSuggest = repairSuggest;
    }

    public String getRepairResult() {
        return repairResult;
    }

    public void setRepairResult(String repairResult) {
        this.repairResult = repairResult;
    }

    public Integer getQuotedPrice() {
        return quotedPrice;
    }

    public void setQuotedPrice(Integer quotedPrice) {
        this.quotedPrice = quotedPrice;
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

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public RepairDeviceQuotationInvoiceDbo getQuotationInvoice() {
        return quotationInvoice;
    }

    public void setQuotationInvoice(RepairDeviceQuotationInvoiceDbo quotationInvoice) {
        this.quotationInvoice = quotationInvoice;
    }

    @Override
    public String toString() {
        return "RepairDeviceDetectInvoiceDbo [id=" + id + ", deviceId=" + deviceId + ", repairDeviceId="
                + repairDeviceId + ", sn=" + sn + ", malfunctionAppearance=" + malfunctionAppearance
                + ", malfunctionReason=" + malfunctionReason + ", malfunctionType=" + malfunctionType
                + ", responsibleParty=" + responsibleParty + ", repairSuggest=" + repairSuggest + ", repairResult="
                + repairResult + ", quotedPrice=" + quotedPrice + ", createTime=" + createTime + ", updateTime="
                + updateTime + ", createUserId=" + createUserId + ", quotationInvoice=" + quotationInvoice + "]";
    }

}