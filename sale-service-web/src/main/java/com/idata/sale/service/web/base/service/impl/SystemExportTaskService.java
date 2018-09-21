package com.idata.sale.service.web.base.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idata.sale.service.web.base.dao.BaseDaoException;
import com.idata.sale.service.web.base.dao.PageInfo;
import com.idata.sale.service.web.base.dao.constant.SystemExportTaskStatus;
import com.idata.sale.service.web.base.dao.constant.SystemExportTaskType;
import com.idata.sale.service.web.base.dao.dbo.SystemExportTaskDbo;
import com.idata.sale.service.web.base.dao.fo.SystemExportTaskFo;
import com.idata.sale.service.web.base.dao.impl.SystemExportTaskDao;
import com.idata.sale.service.web.base.service.ISystemExportTaskService;
import com.idata.sale.service.web.base.service.ServiceException;
import com.idata.sale.service.web.base.service.constant.ServiceCode;
import com.idata.sale.service.web.base.service.dto.SystemRepairDeviceExportDto;
import com.idata.sale.service.web.util.FileUtils;
import com.idata.sale.service.web.util.TimeUtil;

@Service
public class SystemExportTaskService implements ISystemExportTaskService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SystemExportTaskService.class);

    public SystemExportTaskService() {

    }

    @Autowired
    private SystemExportTaskDao exportTaskDao;

    @Autowired
    private SystemExportTaskAsyncService asyncExportService;

    @Override
    public boolean exportRepairDeivce(SystemRepairDeviceExportDto repairDeviceDto) {

        SystemExportTaskDbo systemExportTaskDbo = new SystemExportTaskDbo();

        systemExportTaskDbo.setDownloadFileName(repairDeviceDto.getExportName());
        systemExportTaskDbo.setTaskName(repairDeviceDto.getExportName());

        systemExportTaskDbo.setStatus(SystemExportTaskStatus.Created.code);
        systemExportTaskDbo.setUserId(repairDeviceDto.getUserId());
        systemExportTaskDbo.setDownloadId(FileUtils.getRandomFileName());
        systemExportTaskDbo.setTaskType(SystemExportTaskType.repairDeivce.code);

        Date now = TimeUtil.getNowDate();
        systemExportTaskDbo.setUpdateTime(now);
        systemExportTaskDbo.setCreateTime(now);

        try {
            int id = (int) exportTaskDao.save(systemExportTaskDbo);
            systemExportTaskDbo.setId(id);

            LOGGER.info("[][exportRepairDeivce][create exportTask sucess," + exportTaskDao.toString() + "]");

            String startTime = repairDeviceDto.getStartTime() + " 00:00:00";
            String endTime = repairDeviceDto.getEndTime() + " 23:59:59";

            asyncExportService.startExportRepairDeivce(TimeUtil.parseDateByYYYYmmDDhhMMSS(startTime),
                    TimeUtil.parseDateByYYYYmmDDhhMMSS(endTime), id);
            LOGGER.info("[][exportRepairDeivce][ start exportTask sucess]");
            return true;

        }
        catch (BaseDaoException e) {
            LOGGER.error("[][][]", e);
        }
        return false;
    }

    @Override
    public List<SystemExportTaskDbo> list(SystemExportTaskFo filter, PageInfo pageInfo) throws ServiceException {

        try {
            return exportTaskDao.findList(filter, pageInfo);
        }
        catch (BaseDaoException e) {
            throw new ServiceException(ServiceCode.system_db_exception, e.getMessage());
        }

    }

    @Override
    public SystemExportTaskDbo get(String downloadId) {
        return exportTaskDao.getByDownloadId(downloadId);
    }

    @Override
    public void delete(Integer taskId, Integer userId) throws ServiceException {
        SystemExportTaskDbo taskDbo = exportTaskDao.findById(taskId);
        if (null == taskDbo) {
            return;
        }

        if (!taskDbo.getUserId().equals(userId)) {
            throw new ServiceException(ServiceCode.system_param_error, "userId error");
        }

        String storePath = taskDbo.getStorePath();
        FileUtils.rm(storePath);
        try {
            exportTaskDao.delete(taskDbo);
        }
        catch (BaseDaoException e) {
            throw new ServiceException(ServiceCode.system_db_exception, e.getMessage());
        }
        LOGGER.info("[][delete][success," + taskDbo + "]");

    }

}
