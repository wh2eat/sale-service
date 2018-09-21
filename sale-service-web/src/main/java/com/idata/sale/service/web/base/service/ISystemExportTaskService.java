package com.idata.sale.service.web.base.service;

import java.util.List;

import com.idata.sale.service.web.base.dao.PageInfo;
import com.idata.sale.service.web.base.dao.dbo.SystemExportTaskDbo;
import com.idata.sale.service.web.base.dao.fo.SystemExportTaskFo;
import com.idata.sale.service.web.base.service.dto.SystemRepairDeviceExportDto;

public interface ISystemExportTaskService {

    public SystemExportTaskDbo get(String downloadId);

    public void delete(Integer taskId, Integer userId) throws ServiceException;

    public boolean exportRepairDeivce(SystemRepairDeviceExportDto repairDeviceDto);

    public List<SystemExportTaskDbo> list(SystemExportTaskFo filter, PageInfo pageInfo) throws ServiceException;

}
