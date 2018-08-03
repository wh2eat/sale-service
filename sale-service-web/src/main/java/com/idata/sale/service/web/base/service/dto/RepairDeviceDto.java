package com.idata.sale.service.web.base.service.dto;

import com.idata.sale.service.web.base.dao.dbo.DeviceDbo;
import com.idata.sale.service.web.base.dao.dbo.RepairDeviceDbo;
import com.idata.sale.service.web.base.dao.dbo.RepairInvoiceDbo;
import com.idata.sale.service.web.base.dao.dbo.RepairPackageDbo;

public class RepairDeviceDto {

    private RepairDeviceDbo repairDeviceDbo;

    private RepairPackageDbo repairPackageDbo;

    private RepairInvoiceDbo repairInvoiceDbo;

    private DeviceDbo deviceDbo;

    public RepairDeviceDto() {
        // TODO Auto-generated constructor stub
    }

    public RepairDeviceDbo getRepairDeviceDbo() {
        return repairDeviceDbo;
    }

    public void setRepairDeviceDbo(RepairDeviceDbo repairDeviceDbo) {
        this.repairDeviceDbo = repairDeviceDbo;
    }

    public RepairPackageDbo getRepairPackageDbo() {
        return repairPackageDbo;
    }

    public void setRepairPackageDbo(RepairPackageDbo repairPackageDbo) {
        this.repairPackageDbo = repairPackageDbo;
    }

    public RepairInvoiceDbo getRepairInvoiceDbo() {
        return repairInvoiceDbo;
    }

    public void setRepairInvoiceDbo(RepairInvoiceDbo repairInvoiceDbo) {
        this.repairInvoiceDbo = repairInvoiceDbo;
    }

    public DeviceDbo getDeviceDbo() {
        return deviceDbo;
    }

    public void setDeviceDbo(DeviceDbo deviceDbo) {
        this.deviceDbo = deviceDbo;
    }

    @Override
    public String toString() {
        return "RepairDeviceDto [repairDeviceDbo=" + repairDeviceDbo + ", repairPackageDbo=" + repairPackageDbo
                + ", repairInvoiceDbo=" + repairInvoiceDbo + ", deviceDbo=" + deviceDbo + "]";
    }

}
