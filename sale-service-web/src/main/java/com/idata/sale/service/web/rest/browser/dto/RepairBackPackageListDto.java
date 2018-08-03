package com.idata.sale.service.web.rest.browser.dto;

import java.util.List;

import com.idata.sale.service.web.base.dao.dbo.RepairBackPackageDbo;

public class RepairBackPackageListDto {

    private String userId;

    private List<Integer> repairDeviceIds;

    private RepairBackPackageDbo backPackage;

    public RepairBackPackageListDto() {
    }

    public RepairBackPackageDbo getBackPackage() {
        return backPackage;
    }

    public void setBackPackage(RepairBackPackageDbo backPackage) {
        this.backPackage = backPackage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "RepairBackPackageListDto [userId=" + userId + ", repairDeviceIds=" + repairDeviceIds + ", backPackage="
                + backPackage + "]";
    }

    public List<Integer> getRepairDeviceIds() {
        return repairDeviceIds;
    }

    public void setRepairDeviceIds(List<Integer> repairDeviceIds) {
        this.repairDeviceIds = repairDeviceIds;
    }

}
