package com.idata.sale.service.web.base.dao.dbo;

import java.util.Date;

import com.idata.sale.service.web.base.dao.annotation.Column;
import com.idata.sale.service.web.base.dao.annotation.Table;

@Table("device")
public class DeviceDbo {

    private Integer id;

    @Column()
    private String sn;

    @Column()
    private String imei;

    @Column()
    private String imeiTwo;

    @Column()
    private String meid;

    @Column()
    private String meidTwo;

    @Column
    private String machineType;

    @Column()
    private String model;

    @Column()
    private Date manufactureTime;

    @Column()
    private String agentName;

    @Column()
    private String endCustomerName;

    @Column()
    private Integer repairedTimes;

    @Column()
    private Date lastRepairTime;

    @Column()
    private Date updateTime;

    @Column()
    private Date createTime;

    public DeviceDbo() {
        // TODO Auto-generated constructor stub
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Date getManufactureTime() {
        return manufactureTime;
    }

    public void setManufactureTime(Date manufactureTime) {
        this.manufactureTime = manufactureTime;
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

    public Integer getRepairedTimes() {
        return repairedTimes;
    }

    public void setRepairedTimes(Integer repairedTimes) {
        this.repairedTimes = repairedTimes;
    }

    public Date getLastRepairTime() {
        return lastRepairTime;
    }

    public void setLastRepairTime(Date lastRepairTime) {
        this.lastRepairTime = lastRepairTime;
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

    public String getMachineType() {
        return machineType;
    }

    public void setMachineType(String machineType) {
        this.machineType = machineType;
    }

    @Override
    public String toString() {
        return "DeviceDbo [id=" + id + ", sn=" + sn + ", imei=" + imei + ", imeiTwo=" + imeiTwo + ", meid=" + meid
                + ", meidTwo=" + meidTwo + ", machineType=" + machineType + ", model=" + model + ", manufactureTime="
                + manufactureTime + ", agentName=" + agentName + ", endCustomerName=" + endCustomerName
                + ", repairedTimes=" + repairedTimes + ", lastRepairTime=" + lastRepairTime + ", updateTime="
                + updateTime + ", createTime=" + createTime + "]";
    }

}