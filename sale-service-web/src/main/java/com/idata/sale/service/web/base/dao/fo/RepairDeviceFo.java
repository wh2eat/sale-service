package com.idata.sale.service.web.base.dao.fo;

import java.util.Collection;

public class RepairDeviceFo {

    private String invoiceSerialNumber;

    private String endCustomerName;

    private String customerRma;

    private String packageSerialNumber;

    private String repairPackageSerialNumber;

    private String backPackageSerialNumber;

    private String sn;

    private String expressNumber;

    private String expressName;

    private String deliveryTime;

    private String backTime;

    private String backExpressNumber;

    private Integer repairBackPackageId;

    private boolean repairBackPackageIdIsNull;

    private Integer repairPackageId;

    private String status;

    private String moreThanStatus;

    private String lessThanStatus;

    private Integer repairStationId;

    private Collection<Integer> repairPackageIds;

    private Integer showQutationInvoice;

    public Integer getRepairStationId() {
        return repairStationId;
    }

    public void setRepairStationId(Integer repairStationId) {
        this.repairStationId = repairStationId;
    }

    public String getRepairStationUdid() {
        return repairStationUdid;
    }

    public void setRepairStationUdid(String repairStationUdid) {
        this.repairStationUdid = repairStationUdid;
    }

    private String repairStationUdid;

    private Integer detectUserId;

    public RepairDeviceFo() {
        // TODO Auto-generated constructor stub
    }

    public String getInvoiceSerialNumber() {
        return invoiceSerialNumber;
    }

    public void setInvoiceSerialNumber(String invoiceSerialNumber) {
        this.invoiceSerialNumber = invoiceSerialNumber;
    }

    public String getCustomerRma() {
        return customerRma;
    }

    public void setCustomerRma(String customerRma) {
        this.customerRma = customerRma;
    }

    public String getPackageSerialNumber() {
        return packageSerialNumber;
    }

    public void setPackageSerialNumber(String packageSerialNumber) {
        this.packageSerialNumber = packageSerialNumber;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
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

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    @Override
    public String toString() {
        return "RepairDeviceFo [invoiceSerialNumber=" + invoiceSerialNumber + ", endCustomerName=" + endCustomerName
                + ", customerRma=" + customerRma + ", packageSerialNumber=" + packageSerialNumber
                + ", repairPackageSerialNumber=" + repairPackageSerialNumber + ", backPackageSerialNumber="
                + backPackageSerialNumber + ", sn=" + sn + ", expressNumber=" + expressNumber + ", expressName="
                + expressName + ", deliveryTime=" + deliveryTime + ", backTime=" + backTime + ", backExpressNumber="
                + backExpressNumber + ", repairBackPackageId=" + repairBackPackageId + ", repairBackPackageIdIsNull="
                + repairBackPackageIdIsNull + ", repairPackageId=" + repairPackageId + ", status=" + status
                + ", moreThanStatus=" + moreThanStatus + ", lessThanStatus=" + lessThanStatus + ", repairStationId="
                + repairStationId + ", repairPackageIds=" + repairPackageIds + ", showQutationInvoice="
                + showQutationInvoice + ", repairStationUdid=" + repairStationUdid + ", detectUserId=" + detectUserId
                + "]";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getDetectUserId() {
        return detectUserId;
    }

    public void setDetectUserId(Integer detectUserId) {
        this.detectUserId = detectUserId;
    }

    public String getBackPackageSerialNumber() {
        return backPackageSerialNumber;
    }

    public void setBackPackageSerialNumber(String backPackageSerialNumber) {
        this.backPackageSerialNumber = backPackageSerialNumber;
    }

    public String getMoreThanStatus() {
        return moreThanStatus;
    }

    public void setMoreThanStatus(String moreThanStatus) {
        this.moreThanStatus = moreThanStatus;
    }

    public String getBackTime() {
        return backTime;
    }

    public void setBackTime(String backTime) {
        this.backTime = backTime;
    }

    public String getBackExpressNumber() {
        return backExpressNumber;
    }

    public void setBackExpressNumber(String backExpressNumber) {
        this.backExpressNumber = backExpressNumber;
    }

    public Integer getRepairBackPackageId() {
        return repairBackPackageId;
    }

    public void setRepairBackPackageId(Integer repairBackPackageId) {
        this.repairBackPackageId = repairBackPackageId;
    }

    public Integer getRepairPackageId() {
        return repairPackageId;
    }

    public void setRepairPackageId(Integer repairPackageId) {
        this.repairPackageId = repairPackageId;
    }

    public String getRepairPackageSerialNumber() {
        return repairPackageSerialNumber;
    }

    public void setRepairPackageSerialNumber(String repairPackageSerialNumber) {
        this.repairPackageSerialNumber = repairPackageSerialNumber;
    }

    public Collection<Integer> getRepairPackageIds() {
        return repairPackageIds;
    }

    public void setRepairPackageIds(Collection<Integer> repairPackageIds) {
        this.repairPackageIds = repairPackageIds;
    }

    public boolean isRepairBackPackageIdIsNull() {
        return repairBackPackageIdIsNull;
    }

    public void setRepairBackPackageIdIsNull(boolean repairBackPackageIdIsNull) {
        this.repairBackPackageIdIsNull = repairBackPackageIdIsNull;
    }

    public String getLessThanStatus() {
        return lessThanStatus;
    }

    public void setLessThanStatus(String lessThanStatus) {
        this.lessThanStatus = lessThanStatus;
    }

    public String getEndCustomerName() {
        return endCustomerName;
    }

    public void setEndCustomerName(String endCustomerName) {
        this.endCustomerName = endCustomerName;
    }

    public Integer getShowQutationInvoice() {
        return showQutationInvoice;
    }

    public void setShowQutationInvoice(Integer showQutationInvoice) {
        this.showQutationInvoice = showQutationInvoice;
    }

}
