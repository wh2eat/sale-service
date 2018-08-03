package com.idata.sale.service.web.base.dao.dbo;

import java.util.Date;

import com.idata.sale.service.web.base.dao.annotation.Column;
import com.idata.sale.service.web.base.dao.annotation.Table;

@Table("repair_device_quotation_invoice")
public class RepairDeviceQuotationInvoiceDbo {

    private Integer id;

    @Column()
    private Integer deviceId;

    @Column()
    private Integer repairDeviceId;

    @Column()
    private Integer detectInvoiceId;

    @Column()
    private String item;

    @Column()
    private String itemName;

    @Column()
    private Integer quantity;

    @Column()
    private String priceUnit;

    @Column()
    private String priceTotal;

    @Column()
    private Integer status;

    @Column()
    private Date time;

    @Column()
    private Date createTime;

    @Column()
    private Date updateTime;

    @Column()
    private Integer createUserId;

    @Column()
    private Integer confirmStatus;

    @Column
    private String confirmRemark;

    public RepairDeviceQuotationInvoiceDbo() {
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

    public Integer getDetectInvoiceId() {
        return detectInvoiceId;
    }

    public void setDetectInvoiceId(Integer detectInvoiceId) {
        this.detectInvoiceId = detectInvoiceId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(String priceUnit) {
        this.priceUnit = priceUnit;
    }

    public String getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(String priceTotal) {
        this.priceTotal = priceTotal;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
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

    @Override
    public String toString() {
        return "RepairDeviceQuotationInvoiceDbo [id=" + id + ", deviceId=" + deviceId + ", repairDeviceId="
                + repairDeviceId + ", detectInvoiceId=" + detectInvoiceId + ", item=" + item + ", itemName=" + itemName
                + ", quantity=" + quantity + ", priceUnit=" + priceUnit + ", priceTotal=" + priceTotal + ", status="
                + status + ", time=" + time + ", createTime=" + createTime + ", updateTime=" + updateTime
                + ", createUserId=" + createUserId + ", confirmStatus=" + confirmStatus + ", confirmRemark="
                + confirmRemark + "]";
    }

}