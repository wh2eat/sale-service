package com.idata.sale.service.web.rest.browser.dto;

import com.idata.sale.service.web.base.dao.dbo.RepairPackageDbo;

public class RepairPackageListDto {

    private String invoiceSerialNumber;

    private RepairPackageDbo pkg;

    public RepairPackageListDto() {

    }

    public RepairPackageDbo getPkg() {
        return pkg;
    }

    public void setPkg(RepairPackageDbo pkg) {
        this.pkg = pkg;
    }

    @Override
    public String toString() {
        return "RepairPackageListDto [invoiceSerialNumber=" + invoiceSerialNumber + ", pkg=" + pkg + "]";
    }

    public String getInvoiceSerialNumber() {
        return invoiceSerialNumber;
    }

    public void setInvoiceSerialNumber(String invoiceSerialNumber) {
        this.invoiceSerialNumber = invoiceSerialNumber;
    }
}
