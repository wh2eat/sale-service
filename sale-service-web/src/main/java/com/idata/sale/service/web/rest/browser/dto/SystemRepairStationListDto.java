package com.idata.sale.service.web.rest.browser.dto;

import com.idata.sale.service.web.base.dao.dbo.SystemRepairStationDbo;

public class SystemRepairStationListDto {

    private SystemRepairStationDbo repairStation;

    public SystemRepairStationListDto() {
    }

    public SystemRepairStationDbo getRepairStation() {
        return repairStation;
    }

    public void setRepairStation(SystemRepairStationDbo repairStation) {
        this.repairStation = repairStation;
    }

    @Override
    public String toString() {
        return "SystemRepairStationListDto [repairStation=" + repairStation + "]";
    }
}
