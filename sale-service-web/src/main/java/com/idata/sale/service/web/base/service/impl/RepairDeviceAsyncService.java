package com.idata.sale.service.web.base.service.impl;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import com.idata.sale.service.web.base.dao.constant.RepairStatus;
import com.idata.sale.service.web.base.dao.impl.RepairDeviceDao;
import com.idata.sale.service.web.base.service.event.RepairPackageStatusEvent;

@Service
@EnableAsync
public class RepairDeviceAsyncService {

    private final static Logger LOGGER = LoggerFactory.getLogger(RepairDeviceAsyncService.class);

    public RepairDeviceAsyncService() {
        // TODO Auto-generated constructor stub
    }

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private RepairDeviceDao repairDeviceDao;

    @Async
    public void checkRepairPackageStatus(Collection<Integer> packageIds, RepairStatus status) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][checkRepairPackageStatus][" + packageIds + ";" + status + "]");
        }

        for (Integer pid : packageIds) {
            boolean allStatusIsOk = repairDeviceDao.isAllGreaterEqualStatusByRepairPackage(pid, status.getCode());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(
                        "[][checkRepairPackageStatus][allStatusIsOk:" + allStatusIsOk + ";" + status.getCode() + "]");
            }
            if (allStatusIsOk) {
                publisher.publishEvent(new RepairPackageStatusEvent(pid, status.getCode()));
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("[][checkRepairPackageStatus][push RepairPackageStatusEvent:" + pid + ";"
                            + status.getCode() + "]");
                }
            }
        }

        packageIds.clear();
        packageIds = null;

    }

}
