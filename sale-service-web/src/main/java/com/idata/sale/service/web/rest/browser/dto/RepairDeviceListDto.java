package com.idata.sale.service.web.rest.browser.dto;

import java.util.List;

import com.idata.sale.service.web.base.dao.dbo.RepairDeviceDbo;

public class RepairDeviceListDto {

    private String sn;

    private Integer repairDeviceId;

    private String userId;

    private String result;

    private List<Integer> repairDeviceIds;

    private String invoiceSerialNumber;

    private String packageSerialNumber;

    private String backPackageSerialNumber;

    private RepairDeviceDbo device;

    private List<Integer> ids;

    public RepairDeviceListDto() {
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

    public RepairDeviceDbo getDevice() {
        return device;
    }

    public void setDevice(RepairDeviceDbo device) {
        this.device = device;
    }

    public List<Integer> getRepairDeviceIds() {
        return repairDeviceIds;
    }

    public void setRepairDeviceIds(List<Integer> repairDeviceIds) {
        this.repairDeviceIds = repairDeviceIds;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Integer getRepairDeviceId() {
        return repairDeviceId;
    }

    public void setRepairDeviceId(Integer repairDeviceId) {
        this.repairDeviceId = repairDeviceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "RepairDeviceListDto [sn=" + sn + ", repairDeviceId=" + repairDeviceId + ", userId=" + userId
                + ", result=" + result + ", repairDeviceIds=" + repairDeviceIds + ", invoiceSerialNumber="
                + invoiceSerialNumber + ", packageSerialNumber=" + packageSerialNumber + ", backPackageSerialNumber="
                + backPackageSerialNumber + ", device=" + device + ", ids=" + ids + "]";
    }

    public String getBackPackageSerialNumber() {
        return backPackageSerialNumber;
    }

    public void setBackPackageSerialNumber(String backPackageSerialNumber) {
        this.backPackageSerialNumber = backPackageSerialNumber;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

}
