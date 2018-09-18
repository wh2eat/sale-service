package com.idata.sale.service.web.base.service.impl;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import com.idata.sale.service.web.base.dao.impl.SystemExportTaskDao;
import com.idata.sale.service.web.base.service.IRepairDeviceExportService;
import com.idata.sale.service.web.util.TimeUtil;

@Service
@EnableAsync
public class SystemExportTaskAsyncService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SystemExportTaskAsyncService.class);

    public SystemExportTaskAsyncService() {

    }

    @Autowired
    private SystemExportTaskDao exportTaskDao;

    @Autowired
    private IRepairDeviceExportService repairDeviceExportService;

    @Async
    public void startExportRepairDeivce(Date startTime, Date endTime, int exportTaskId) {

        LOGGER.info("[][startExportRepairDeivce][start,exportTaskId:" + exportTaskId + ";startTime:"
                + TimeUtil.formatByYYYYmmDDhhMMSS(startTime) + ";endTime:" + TimeUtil.formatByYYYYmmDDhhMMSS(endTime)
                + "]");

        try {
            String storePath = repairDeviceExportService.export(startTime, endTime);

            if (StringUtils.isEmpty(storePath)) {
                exportTaskDao.exportFailed(exportTaskId);
                LOGGER.info("[][startExportRepairDeivce][export failed]");
            }
            else {
                exportTaskDao.exportCompleted(exportTaskId, storePath);
                LOGGER.info("[][startExportRepairDeivce][export success,path:" + storePath + "]");
            }
        }
        catch (Exception e) {
            exportTaskDao.exportFailed(exportTaskId);
            LOGGER.error("", e);
        }

        LOGGER.info("[][startExportRepairDeivce][finish,exportTaskId:" + exportTaskId + "]");

    }

}
