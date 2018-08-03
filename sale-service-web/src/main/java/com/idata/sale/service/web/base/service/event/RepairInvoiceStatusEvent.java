package com.idata.sale.service.web.base.service.event;

public class RepairInvoiceStatusEvent {

    private int status;

    private int repairInvoiceId;

    public RepairInvoiceStatusEvent() {
        // TODO Auto-generated constructor stub
    }

    public RepairInvoiceStatusEvent(int repairInvoiceId, int status) {
        this.setRepairInvoiceId(repairInvoiceId);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RepairInvoiceStatusEvent [status=" + status + ", repairInvoiceId=" + repairInvoiceId + "]";
    }

    public int getRepairInvoiceId() {
        return repairInvoiceId;
    }

    public void setRepairInvoiceId(int repairInvoiceId) {
        this.repairInvoiceId = repairInvoiceId;
    }

}
