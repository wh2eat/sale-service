package com.idata.sale.service.web.base.service;

import java.util.List;

import com.idata.sale.service.web.base.dao.PageInfo;
import com.idata.sale.service.web.base.dao.dbo.RepairDeviceDbo;
import com.idata.sale.service.web.base.dao.dbo.SystemUserDbo;
import com.idata.sale.service.web.base.service.dto.SystemUserDto;

public interface ISystemUserService {

    public SystemUserDto login(String loginName, String password);

    public void add(SystemUserDbo userDbo) throws ServiceException;

    public boolean update(SystemUserDbo userDbo) throws ServiceException;

    public List<SystemUserDbo> getList(PageInfo pageInfo, RepairDeviceDbo filter) throws ServiceException;

    public SystemUserDbo get(String udid);

    public Integer getId(String udid);

    public Integer getRepairStationId(String udid);

    public void remove(Integer id) throws ServiceException;

    public void updatePassword(String udid, String oldPassword, String newPassword) throws ServiceException;

    public boolean existByLoginName(String loginName, String udid) throws ServiceException;

    public boolean existByEmail(String email, String udid) throws ServiceException;

    public boolean existByTelephone(String telephone, String udid) throws ServiceException;

    public void setUserOnlyame(List<RepairDeviceDbo> repairDeviceDbos);

}
