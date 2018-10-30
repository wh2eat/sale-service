package com.idata.sale.service.web.base.dao.dbo;

import java.util.Date;
import java.util.List;

import com.idata.sale.service.web.base.dao.annotation.Column;
import com.idata.sale.service.web.base.dao.annotation.Table;

@Table("repair_device")
public class RepairDeviceDbo implements RepairInvoiceDboSet, RepairPackageDboSet {

    private Integer id;

    @Column()
    private Integer currency;

    @Column()
    private Integer deviceId;

    @Column()
    private Integer result;

    @Column()
    private Integer status;

    private Integer lessThanStatus;

    @Column()
    private Integer repairInvoiceId;

    @Column()
    private Integer repairPackageId;

    @Column()
    private Integer repairBackPackageId;

    private Integer notRepairBackPackageId;

    @Column
    private String sn;

    @Column
    private String imei;

    @Column
    private String imeiTwo;

    @Column
    private String meid;

    @Column
    private String meidTwo;

    @Column
    private String model;

    @Column
    private String machineType;

    @Column
    private String agentName;

    @Column
    private String endCustomerName;

    @Column()
    private String expressNumber;

    @Column()
    private Integer repairTimes;

    @Column
    private Date lastRepairTime;

    @Column()
    private String warrantyType;

    @Column()
    private String faultDescription;

    @Column()
    private String attachment;

    @Column()
    private Date deliveryTime;

    @Column
    private Integer charge;

    @Column()
    private Integer chargeType;

    @Column()
    private String chargeDescription;

    @Column()
    private Integer payType;

    @Column()
    private Integer payStatus;

    @Column
    private Date payFinishTime;

    @Column
    private String payDescription;

    @Column()
    private String laborCosts;

    @Column()
    private String costTotal;

    @Column()
    private Date createTime;

    @Column()
    private Date updateTime;

    @Column
    private Date manufactureTime;

    @Column()
    private String remark;

    @Column
    private Integer detectUserId;

    @Column
    private Integer repairUserId;

    @Column
    private Integer createUserId;

    @Column
    private Integer quotationUserId;

    @Column
    private Date repairStartTime;

    @Column
    private Date repairFinishTime;

    @Column
    private Date detectStartTime;

    @Column
    private Date detectFinishTime;

    @Column
    private Date quotationFinishTime;

    @Column
    private Date backTime;

    @Column
    private String shipRemark;

    public String getShipRemark() {
        return shipRemark;
    }

    public void setShipRemark(String shipRemark) {
        this.shipRemark = shipRemark;
    }

    private SystemUserDbo quotationUser;

    public SystemUserDbo getQuotationUser() {
        return quotationUser;
    }

    public void setQuotationUser(SystemUserDbo quotationUser) {
        this.quotationUser = quotationUser;
    }

    public Integer getQuotationUserId() {
        return quotationUserId;
    }

    public void setQuotationUserId(Integer quotationUserId) {
        this.quotationUserId = quotationUserId;
    }

    public SystemUserDbo getCreateUser() {
        return createUser;
    }

    public void setCreateUser(SystemUserDbo createUser) {
        this.createUser = createUser;
    }

    public SystemUserDbo getDetectUser() {
        return detectUser;
    }

    public void setDetectUser(SystemUserDbo detectUser) {
        this.detectUser = detectUser;
    }

    public SystemUserDbo getRepairUser() {
        return repairUser;
    }

    public void setRepairUser(SystemUserDbo repairUser) {
        this.repairUser = repairUser;
    }

    private RepairInvoiceDbo repairInvoice;

    private RepairPackageDbo repairPackage;

    private SystemUserDbo createUser;

    private SystemUserDbo detectUser;

    private SystemUserDbo repairUser;

    private List<RepairDeviceDetectInvoiceDbo> detectInvoices;

    private Integer notStatus;

    @Column
    private Integer repairStationId;

    public Integer getRepairStationId() {
        return repairStationId;
    }

    public void setRepairStationId(Integer repairStationId) {
        this.repairStationId = repairStationId;
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

    @Override
    public Integer getRepairInvoiceId() {
        return repairInvoiceId;
    }

    public void setRepairInvoiceId(Integer repairInvoiceId) {
        this.repairInvoiceId = repairInvoiceId;
    }

    @Override
    public Integer getRepairPackageId() {
        return repairPackageId;
    }

    private RepairBackPackageDbo repairBackPackage;

    public void setRepairPackageId(Integer repairPackageId) {
        this.repairPackageId = repairPackageId;
    }

    public Integer getRepairBackPackageId() {
        return repairBackPackageId;
    }

    public void setRepairBackPackageId(Integer repairBackPackageId) {
        this.repairBackPackageId = repairBackPackageId;
    }

    public String getExpressNumber() {
        return expressNumber;
    }

    public void setExpressNumber(String expressNumber) {
        this.expressNumber = expressNumber;
    }

    public Integer getRepairTimes() {
        return repairTimes;
    }

    public void setRepairTimes(Integer repairTimes) {
        this.repairTimes = repairTimes;
    }

    public String getWarrantyType() {
        return warrantyType;
    }

    public void setWarrantyType(String warrantyType) {
        this.warrantyType = warrantyType;
    }

    public String getFaultDescription() {
        return faultDescription;
    }

    public void setFaultDescription(String faultDescription) {
        this.faultDescription = faultDescription;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Integer getChargeType() {
        return chargeType;
    }

    public void setChargeType(Integer chargeType) {
        this.chargeType = chargeType;
    }

    public String getChargeDescription() {
        return chargeDescription;
    }

    public void setChargeDescription(String chargeDescription) {
        this.chargeDescription = chargeDescription;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public String getCostTotal() {
        return costTotal;
    }

    public void setCostTotal(String costTotal) {
        this.costTotal = costTotal;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImeiTwo() {
        return imeiTwo;
    }

    public void setImeiTwo(String imeiTwo) {
        this.imeiTwo = imeiTwo;
    }

    public String getMeid() {
        return meid;
    }

    public void setMeid(String meid) {
        this.meid = meid;
    }

    public String getMeidTwo() {
        return meidTwo;
    }

    public void setMeidTwo(String meidTwo) {
        this.meidTwo = meidTwo;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getEndCustomerName() {
        return endCustomerName;
    }

    public void setEndCustomerName(String endCustomerName) {
        this.endCustomerName = endCustomerName;
    }

    public Date getManufactureTime() {
        return manufactureTime;
    }

    public void setManufactureTime(Date manufactureTime) {
        this.manufactureTime = manufactureTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDetectUserId() {
        return detectUserId;
    }

    public void setDetectUserId(Integer detectUserId) {
        this.detectUserId = detectUserId;
    }

    public Integer getRepairUserId() {
        return repairUserId;
    }

    public void setRepairUserId(Integer repairUserId) {
        this.repairUserId = repairUserId;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public RepairInvoiceDbo getRepairInvoice() {
        return repairInvoice;
    }

    public void setRepairInvoice(RepairInvoiceDbo repairInvoice) {
        this.repairInvoice = repairInvoice;
    }

    public RepairPackageDbo getRepairPackage() {
        return repairPackage;
    }

    public void setRepairPackage(RepairPackageDbo repairPackage) {
        this.repairPackage = repairPackage;
    }

    @Override
    public void setRepairPackageDbo(RepairPackageDbo repairPackageDbo) {
        this.repairPackage = repairPackageDbo;
    }

    @Override
    public void setRepairInvoiceDbo(RepairInvoiceDbo dbo) {
        this.repairInvoice = dbo;
    }

    public Date getLastRepairTime() {
        return lastRepairTime;
    }

    public void setLastRepairTime(Date lastRepairTime) {
        this.lastRepairTime = lastRepairTime;
    }

    public List<RepairDeviceDetectInvoiceDbo> getDetectInvoices() {
        return detectInvoices;
    }

    public void setDetectInvoices(List<RepairDeviceDetectInvoiceDbo> detectInvoices) {
        this.detectInvoices = detectInvoices;
    }

    public Integer getCharge() {
        return charge;
    }

    public void setCharge(Integer charge) {
        this.charge = charge;
    }

    public Date getPayFinishTime() {
        return payFinishTime;
    }

    public void setPayFinishTime(Date payFinishTime) {
        this.payFinishTime = payFinishTime;
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

    public String getMachineType() {
        return machineType;
    }

    public void setMachineType(String machineType) {
        this.machineType = machineType;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "RepairDeviceDbo [id=" + id + ", currency=" + currency + ", deviceId=" + deviceId + ", result=" + result
                + ", status=" + status + ", lessThanStatus=" + lessThanStatus + ", repairInvoiceId=" + repairInvoiceId
                + ", repairPackageId=" + repairPackageId + ", repairBackPackageId=" + repairBackPackageId
                + ", notRepairBackPackageId=" + notRepairBackPackageId + ", sn=" + sn + ", imei=" + imei + ", imeiTwo="
                + imeiTwo + ", meid=" + meid + ", meidTwo=" + meidTwo + ", model=" + model + ", machineType="
                + machineType + ", agentName=" + agentName + ", endCustomerName=" + endCustomerName + ", expressNumber="
                + expressNumber + ", repairTimes=" + repairTimes + ", lastRepairTime=" + lastRepairTime
                + ", warrantyType=" + warrantyType + ", faultDescription=" + faultDescription + ", attachment="
                + attachment + ", deliveryTime=" + deliveryTime + ", charge=" + charge + ", chargeType=" + chargeType
                + ", chargeDescription=" + chargeDescription + ", payType=" + payType + ", payStatus=" + payStatus
                + ", payFinishTime=" + payFinishTime + ", payDescription=" + payDescription + ", laborCosts="
                + laborCosts + ", costTotal=" + costTotal + ", createTime=" + createTime + ", updateTime=" + updateTime
                + ", manufactureTime=" + manufactureTime + ", remark=" + remark + ", detectUserId=" + detectUserId
                + ", repairUserId=" + repairUserId + ", createUserId=" + createUserId + ", quotationUserId="
                + quotationUserId + ", repairStartTime=" + repairStartTime + ", repairFinishTime=" + repairFinishTime
                + ", detectStartTime=" + detectStartTime + ", detectFinishTime=" + detectFinishTime
                + ", quotationFinishTime=" + quotationFinishTime + ", backTime=" + backTime + ", shipRemark="
                + shipRemark + ", quotationUser=" + quotationUser + ", repairInvoice=" + repairInvoice
                + ", repairPackage=" + repairPackage + ", createUser=" + createUser + ", detectUser=" + detectUser
                + ", repairUser=" + repairUser + ", detectInvoices=" + detectInvoices + ", notStatus=" + notStatus
                + ", repairStationId=" + repairStationId + ", repairBackPackage=" + repairBackPackage + "]";
    }

    public Integer getNotStatus() {
        return notStatus;
    }

    public void setNotStatus(Integer notStatus) {
        this.notStatus = notStatus;
    }

    public Integer getNotRepairBackPackageId() {
        return notRepairBackPackageId;
    }

    public void setNotRepairBackPackageId(Integer notRepairBackPackageId) {
        this.notRepairBackPackageId = notRepairBackPackageId;
    }

    public Integer getLessThanStatus() {
        return lessThanStatus;
    }

    public void setLessThanStatus(Integer lessThanStatus) {
        this.lessThanStatus = lessThanStatus;
    }

    public Date getRepairStartTime() {
        return repairStartTime;
    }

    public void setRepairStartTime(Date repairStartTime) {
        this.repairStartTime = repairStartTime;
    }

    public Date getRepairFinishTime() {
        return repairFinishTime;
    }

    public void setRepairFinishTime(Date repairFinishTime) {
        this.repairFinishTime = repairFinishTime;
    }

    public Date getDetectStartTime() {
        return detectStartTime;
    }

    public void setDetectStartTime(Date detectStartTime) {
        this.detectStartTime = detectStartTime;
    }

    public Date getDetectFinishTime() {
        return detectFinishTime;
    }

    public void setDetectFinishTime(Date detectFinishTime) {
        this.detectFinishTime = detectFinishTime;
    }

    public Date getQuotationFinishTime() {
        return quotationFinishTime;
    }

    public void setQuotationFinishTime(Date quotationFinishTime) {
        this.quotationFinishTime = quotationFinishTime;
    }

    public Date getBackTime() {
        return backTime;
    }

    public void setBackTime(Date backTime) {
        this.backTime = backTime;
    }

    public RepairBackPackageDbo getRepairBackPackage() {
        return repairBackPackage;
    }

    public void setRepairBackPackage(RepairBackPackageDbo repairBackPackage) {
        this.repairBackPackage = repairBackPackage;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

}