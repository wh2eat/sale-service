package com.idata.sale.service.web.base.service.event;

import java.util.List;

import com.idata.sale.service.web.base.dao.constant.RepairStatus;

public class RepairDeviceStatusEvent {

    private List<Integer> repairDeviceIds;

    private RepairStatus status;

    public RepairDeviceStatusEvent(List<Integer> repairDeviceIds, RepairStatus status) {
        setRepairDeviceIds(repairDeviceIds);
        setStatus(status);
    }

    public RepairDeviceStatusEvent() {
        // TODO Auto-generated constructor stub
    }

    public List<Integer> getRepairDeviceIds() {
        return repairDeviceIds;
    }

    @Override
    public String toString() {
        return "RepairDeviceStatusEvent [repairDeviceIds=" + repairDeviceIds + ", status=" + status + "]";
    }

    public void setRepairDeviceIds(List<Integer> repairDeviceIds) {
        this.repairDeviceIds = repairDeviceIds;
    }

    public RepairStatus getStatus() {
        return status;
    }

    public void setStatus(RepairStatus status) {
        this.status = status;
    }

}
