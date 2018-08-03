package com.idata.sale.service.web.rest.browser.dto;

import com.idata.sale.service.web.base.dao.dbo.RepairInvoiceDbo;

public class RepairInvoiceListDto {

    private String serialNumber;

    private RepairInvoiceDbo invoice;

    public RepairInvoiceListDto() {

    }

    public RepairInvoiceDbo getInvoice() {
        return invoice;
    }

    public void setInvoice(RepairInvoiceDbo invoice) {
        this.invoice = invoice;
    }

    @Override
    public String toString() {
        return "RepairInvoiceListDto [serialNumber=" + serialNumber + ", invoice=" + invoice + "]";
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

}
