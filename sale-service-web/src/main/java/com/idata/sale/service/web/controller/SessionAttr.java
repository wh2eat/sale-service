package com.idata.sale.service.web.controller;

public enum SessionAttr {

    LoginUser("loginUser"), LoginRepairStation("loginRepairStation"), RepairStationId("repairStationId");

    private String name;

    private SessionAttr(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
