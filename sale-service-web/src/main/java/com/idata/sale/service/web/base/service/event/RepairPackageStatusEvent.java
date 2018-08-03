package com.idata.sale.service.web.base.service.event;

public class RepairPackageStatusEvent {

    private int status;

    private int repairPackageId;

    public RepairPackageStatusEvent() {
        // TODO Auto-generated constructor stub
    }

    public RepairPackageStatusEvent(int repairPackageId, int status) {
        this.repairPackageId = repairPackageId;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRepairPackageId() {
        return repairPackageId;
    }

    public void setRepairPackageId(int repairPackageId) {
        this.repairPackageId = repairPackageId;
    }

    @Override
    public String toString() {
        return "RepairPackageStatusEvent [status=" + status + ", repairPackageId=" + repairPackageId + "]";
    }

}
