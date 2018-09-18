package com.idata.sale.service.web.rest.browser.dto;

import com.idata.sale.service.web.base.service.dto.SystemRepairDeviceExportDto;

public class ExportRepairDeviceDto {

    private String userId;

    private SystemRepairDeviceExportDto param;

    public ExportRepairDeviceDto() {
        // TODO Auto-generated constructor stub
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "ExportRepairDeviceDto [userId=" + userId + ", param=" + param + "]";
    }

    public SystemRepairDeviceExportDto getParam() {
        return param;
    }

    public void setParam(SystemRepairDeviceExportDto param) {
        this.param = param;
    }

}
