package com.idata.sale.service.web.base.service;

import java.util.List;

import com.idata.sale.service.web.base.dao.PageInfo;
import com.idata.sale.service.web.base.dao.dbo.SystemRepairStationDbo;
import com.idata.sale.service.web.base.dao.dbo.SystemUserDbo;

public interface ISystemRepairStationService {

    public SystemRepairStationDbo get(Integer id);

    public SystemRepairStationDbo get(String udid);

    public Integer getId(String udid);

    public Integer getIdNotNull(String udid) throws ServiceException;

    public List<SystemRepairStationDbo> getAll();

    public List<SystemRepairStationDbo> getList(PageInfo pageInfo) throws ServiceException;

    public void save(SystemRepairStationDbo repairStationDbo) throws ServiceException;

    public void delete(SystemRepairStationDbo repairStationDbo) throws ServiceException;

    public void setRepairStation(List<SystemUserDbo> userDbos) throws ServiceException;

}
