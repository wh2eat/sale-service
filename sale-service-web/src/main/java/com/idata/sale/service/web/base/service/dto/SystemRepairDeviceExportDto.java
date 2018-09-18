package com.idata.sale.service.web.base.service.dto;

public class SystemRepairDeviceExportDto {

    private String exportName;

    private String remark;

    private String startTime;

    private String endTime;

    private Integer userId;

    public SystemRepairDeviceExportDto() {

    }

    public String getExportName() {
        return exportName;
    }

    public void setExportName(String exportName) {
        this.exportName = exportName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "SystemExportRepairDeviceDto [exportName=" + exportName + ", remark=" + remark + ", startTime="
                + startTime + ", endTime=" + endTime + ", userId=" + userId + "]";
    }

}
