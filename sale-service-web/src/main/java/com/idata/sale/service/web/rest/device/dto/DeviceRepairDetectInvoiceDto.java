package com.idata.sale.service.web.rest.device.dto;

import java.util.List;

import com.idata.sale.service.web.base.dao.dbo.RepairDeviceDetectInvoiceDbo;

public class DeviceRepairDetectInvoiceDto {

    private String userId;

    private Integer repairDeivceId;

    private String sn;

    private String idsStr;

    private List<Integer> ids;

    private List<RepairDeviceDetectInvoiceDbo> invoices;

    public DeviceRepairDetectInvoiceDto() {
    }

    public DeviceRepairDetectInvoiceDto(String userId, Integer repairDeivceId, String sn, String idsStr,
            List<Integer> ids, List<RepairDeviceDetectInvoiceDbo> invoices) {
        super();
        this.userId = userId;
        this.repairDeivceId = repairDeivceId;
        this.sn = sn;
        this.idsStr = idsStr;
        this.ids = ids;
        this.invoices = invoices;
    }

    public List<RepairDeviceDetectInvoiceDbo> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<RepairDeviceDetectInvoiceDbo> invoices) {
        this.invoices = invoices;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getRepairDeivceId() {
        return repairDeivceId;
    }

    public void setRepairDeivceId(Integer repairDeivceId) {
        this.repairDeivceId = repairDeivceId;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public List<Integer> getIds() {

        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public String getIdsStr() {
        return idsStr;
    }

    public void setIdsStr(String idsStr) {
        this.idsStr = idsStr;
    }

    @Override
    public String toString() {
        return "DeviceRepairDetectInvoiceDto [userId=" + userId + ", repairDeivceId=" + repairDeivceId + ", sn=" + sn
                + ", ids=" + ids + ", invoices=" + invoices + "]";
    }

}
