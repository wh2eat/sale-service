package com.idata.sale.service.web.base.service;

import java.util.List;

import com.idata.sale.service.web.base.dao.dbo.RepairDeviceDbo;
import com.idata.sale.service.web.base.dao.dbo.RepairDeviceDetectInvoiceDbo;

public interface IRepairDeviceDetectInvoiceService {

    public void save(List<RepairDeviceDetectInvoiceDbo> invoiceDbos) throws ServiceException;

    public RepairDeviceDetectInvoiceDbo get(Integer invoiceId);

    public List<RepairDeviceDetectInvoiceDbo> getListByRepairDevice(Integer repairDeviceId);

    public List<RepairDeviceDetectInvoiceDbo> getQuotationListByRepairDevice(Integer repairDeviceId);

    public void delete(List<Integer> invoiceIds);

    public void setRepairDetectInvoices(List<RepairDeviceDbo> deviceDbos);

}
