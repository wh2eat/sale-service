package com.idata.sale.service.web.base.service;

import java.util.List;

import com.idata.sale.service.web.base.dao.PageInfo;
import com.idata.sale.service.web.base.dao.dbo.RepairInvoiceDbo;
import com.idata.sale.service.web.base.dao.dbo.SystemCustomerDbo;

public interface ISystemCustomerService {

    public void add(SystemCustomerDbo customerDbo) throws ServiceException;

    public void addAsync(RepairInvoiceDbo repairInvoiceDbo) throws ServiceException;

    public List<SystemCustomerDbo> list(PageInfo pageInfo, SystemCustomerDbo filterDbo) throws ServiceException;

}
