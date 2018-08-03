package com.idata.sale.service.web.base.service;

import java.util.List;

import com.idata.sale.service.web.base.dao.PageInfo;
import com.idata.sale.service.web.base.dao.dbo.RepairBackPackageDbo;
import com.idata.sale.service.web.base.dao.dbo.RepairDeviceDbo;

public interface IRepairBackPackageService {

    public RepairBackPackageDbo get(Integer id);

    public RepairBackPackageDbo get(String serialNumber);

    public RepairBackPackageDbo getByExpressNumber(String expressName);

    public RepairBackPackageDbo getLast(String serialNumber, Integer repairStationId);

    public Integer getIdNotNull(String serialNumber) throws ServiceException;

    public List<RepairBackPackageDbo> getList(PageInfo pageInfo, RepairBackPackageDbo filterDbo)
            throws ServiceException;

    public Integer add(RepairBackPackageDbo repairBackPackageDbo) throws ServiceException;

    public void update(RepairBackPackageDbo repairBackPackageDbo) throws ServiceException;

    public void delete(RepairBackPackageDbo repairBackPackageDbo) throws ServiceException;

    public void ship(RepairBackPackageDbo repairBackPackageDbo) throws ServiceException;

    public void setBackPackage(List<RepairDeviceDbo> repairDeviceDbos);

}
