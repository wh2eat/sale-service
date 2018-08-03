package com.idata.sale.service.web.base.service;

import java.util.Collection;
import java.util.List;

import com.idata.sale.service.web.base.dao.PageInfo;
import com.idata.sale.service.web.base.dao.dbo.RepairPackageDbo;

public interface IRepairPackageService {

    public List<RepairPackageDbo> getList(Collection<Integer> ids);

    public RepairPackageDbo get(String serialNumber);

    public RepairPackageDbo getByExpressName(String expressName);

    public void add(RepairPackageDbo repairPackageDbo) throws ServiceException;

    public void update(RepairPackageDbo repairPackageDbo) throws ServiceException;

    public void delete(RepairPackageDbo repairPackageDbo);

    public List<RepairPackageDbo> list(PageInfo pageInfo, RepairPackageDbo filterDbo) throws ServiceException;

    public void setRepairPackageDbo(List dboSets);
}
