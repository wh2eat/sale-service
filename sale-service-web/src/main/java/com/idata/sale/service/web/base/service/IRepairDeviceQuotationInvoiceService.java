package com.idata.sale.service.web.base.service;

import java.util.List;

import com.idata.sale.service.web.base.dao.dbo.RepairDeviceDbo;
import com.idata.sale.service.web.base.dao.dbo.RepairDeviceDetectInvoiceDbo;
import com.idata.sale.service.web.base.dao.dbo.RepairDeviceQuotationInvoiceDbo;

public interface IRepairDeviceQuotationInvoiceService {

    public void saveQuotationInvoiceConfirm(RepairDeviceQuotationInvoiceDbo quotationInvoiceDbo);

    public void setQuotationInvoice(List<RepairDeviceDbo> repairDeviceDbos);

    public void setQuotationInvoice(Integer repairDeviceId, List<RepairDeviceDetectInvoiceDbo> detectInvoiceDbos);

    public void saveQuotationInvoice(Integer createUserId, List<RepairDeviceQuotationInvoiceDbo> quotationInvoiceDbos)
            throws ServiceException;

}
