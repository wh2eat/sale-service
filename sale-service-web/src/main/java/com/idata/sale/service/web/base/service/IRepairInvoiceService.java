package com.idata.sale.service.web.base.service;

import java.util.List;

import com.idata.sale.service.web.base.dao.PageInfo;
import com.idata.sale.service.web.base.dao.dbo.RepairInvoiceDbo;
import com.idata.sale.service.web.base.dao.dbo.RepairInvoiceDboSet;

public interface IRepairInvoiceService {

    public RepairInvoiceDbo get(String serialNumber);

    public Integer getId(String serialNumber);

    public void add(RepairInvoiceDbo repairInvoiceDbo) throws ServiceException;

    public void update(RepairInvoiceDbo repairInvoiceDbo) throws ServiceException;

    public void delete(RepairInvoiceDbo repairInvoiceDbo) throws ServiceException;

    public List<RepairInvoiceDbo> list(PageInfo pageInfo, RepairInvoiceDbo filterDbo, String createTime)
            throws ServiceException;

    public void setRepairInvoiceDbo(RepairInvoiceDboSet dbo);

    public void setRepairInvoiceDbo(List dboSets);

}
