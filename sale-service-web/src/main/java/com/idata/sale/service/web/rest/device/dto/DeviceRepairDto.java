package com.idata.sale.service.web.rest.device.dto;

import java.util.List;

import com.idata.sale.service.web.base.dao.dbo.RepairDeviceDbo;

public class DeviceRepairDto {

    private String userId;

    private Integer id;

    private String sn;

    private List<RepairDeviceDbo> devices;

    public DeviceRepairDto() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public List<RepairDeviceDbo> getDevices() {
        return devices;
    }

    public void setDevices(List<RepairDeviceDbo> devices) {
        this.devices = devices;
    }

    @Override
    public String toString() {
        return "DeviceRepairDto [userId=" + userId + ", id=" + id + ", sn=" + sn + ", devices=" + devices + "]";
    }
}
