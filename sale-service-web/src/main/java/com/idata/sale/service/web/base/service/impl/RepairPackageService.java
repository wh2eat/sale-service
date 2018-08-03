package com.idata.sale.service.web.base.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.idata.sale.service.web.base.dao.BaseDaoException;
import com.idata.sale.service.web.base.dao.PageInfo;
import com.idata.sale.service.web.base.dao.constant.RepairStatus;
import com.idata.sale.service.web.base.dao.dbo.RepairPackageDbo;
import com.idata.sale.service.web.base.dao.dbo.RepairPackageDboSet;
import com.idata.sale.service.web.base.dao.fo.RepairPackageFo;
import com.idata.sale.service.web.base.dao.impl.RepairPackageDao;
import com.idata.sale.service.web.base.service.IRepairPackageService;
import com.idata.sale.service.web.base.service.SerialNumberUtil;
import com.idata.sale.service.web.base.service.ServiceException;
import com.idata.sale.service.web.base.service.constant.ServiceCode;
import com.idata.sale.service.web.base.service.event.RepairInvoiceStatusEvent;
import com.idata.sale.service.web.base.service.event.RepairPackageStatusEvent;

@Service
public class RepairPackageService implements IRepairPackageService {

    private final static Logger LOGGER = LoggerFactory.getLogger(RepairPackageService.class);

    public RepairPackageService() {

    }

    @Autowired
    private RepairPackageDao repairPackageDao;

    @Override
    public void add(RepairPackageDbo repairPackageDbo) throws ServiceException {

        Date now = new Date(System.currentTimeMillis());
        repairPackageDbo.setCreateTime(now);
        repairPackageDbo.setUpdateTime(now);
        repairPackageDbo.setReceiptTime(now);

        repairPackageDbo.setSerialNumber(SerialNumberUtil.generateRepairPackageSerialNumber());
        repairPackageDbo.setStatus(RepairStatus.Received.getCode());

        try {
            long id = repairPackageDao.save(repairPackageDbo);
            LOGGER.info("[][add][success,id:" + id + "]");
        }
        catch (BaseDaoException e) {
            LOGGER.error("[][add][failed]", e);
            throw new ServiceException(ServiceCode.system_db_exception, e.getMessage());
        }

    }

    @Override
    public void update(RepairPackageDbo repairPackageDbo) throws ServiceException {

        try {

            repairPackageDbo.setUpdateTime(new Date(System.currentTimeMillis()));

            repairPackageDao.update(repairPackageDbo);
            LOGGER.info("[][update][success," + repairPackageDbo + "]");
        }
        catch (BaseDaoException e) {
            LOGGER.error("[][add][failed]", e);
            throw new ServiceException(ServiceCode.system_db_exception, e.getMessage());
        }

    }

    @Override
    public void delete(RepairPackageDbo repairPackageDbo) {

        try {
            repairPackageDao.delete(repairPackageDbo);
            LOGGER.info("[][delete][success," + repairPackageDbo + "]");
        }
        catch (BaseDaoException e) {
            LOGGER.error("[][delete][failed," + repairPackageDbo + "]", e);
        }

    }

    @Override
    public void setRepairPackageDbo(List dboSets) {

        Set<Integer> packageIds = new HashSet<>();
        for (Object obj : dboSets) {
            RepairPackageDboSet dboSet = (RepairPackageDboSet) obj;
            packageIds.add(dboSet.getRepairPackageId());
        }

        List<RepairPackageDbo> packageDbos = repairPackageDao.getAll(packageIds);
        if (CollectionUtils.isNotEmpty(packageDbos)) {
            Map<Integer, RepairPackageDbo> idDboMap = new HashMap<>();
            for (RepairPackageDbo repairPackageDbo : packageDbos) {
                idDboMap.put(repairPackageDbo.getId(), repairPackageDbo);
            }

            for (Object obj : dboSets) {
                RepairPackageDboSet dboSet = (RepairPackageDboSet) obj;
                dboSet.setRepairPackageDbo(idDboMap.get(dboSet.getRepairPackageId()));
            }

            packageDbos.clear();

            idDboMap.clear();
            idDboMap = null;
        }
        packageDbos = null;

        packageIds.clear();
        packageIds = null;
    }

    @Override
    public List<RepairPackageDbo> list(PageInfo pageInfo, RepairPackageDbo filterDbo) throws ServiceException {

        return repairPackageDao.findPageList(pageInfo, filterDbo);
    }

    @Override
    public List<RepairPackageDbo> getList(Collection<Integer> ids) {
        RepairPackageFo repairPackageFo = new RepairPackageFo();
        repairPackageFo.setIds(ids);
        try {
            return repairPackageDao.findList(repairPackageFo);
        }
        finally {
            repairPackageFo = null;
        }
    }

    @Override
    public RepairPackageDbo get(String serialNumber) {

        return repairPackageDao.get(serialNumber);
    }

    @Override
    public RepairPackageDbo getByExpressName(String expressName) {
        return repairPackageDao.getByExpressNumber(expressName);
    }

    @Autowired
    private ApplicationEventPublisher publisher;

    @EventListener
    public void dealRepairPackageStatusEvent(RepairPackageStatusEvent repairPackageStatusEvent) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][dealRepairPackageStatusEvent][" + repairPackageStatusEvent + "]");
        }

        RepairPackageDbo packageDbo = new RepairPackageDbo();
        packageDbo.setId(repairPackageStatusEvent.getRepairPackageId());
        packageDbo.setStatus(repairPackageStatusEvent.getStatus());
        packageDbo.setUpdateTime(new Date(System.currentTimeMillis()));

        try {
            repairPackageDao.update(packageDbo);

            List<Integer> pids = new ArrayList<>(1);
            pids.add(repairPackageStatusEvent.getRepairPackageId());

            Collection<Integer> riids = repairPackageDao.getRepairInvoiceIds(pids);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][dealRepairPackageStatusEvent][" + riids + "]");
            }
            if (CollectionUtils.isNotEmpty(riids)) {
                for (Integer riid : riids) {

                    boolean allStatusIsOk = repairPackageDao.isAllGreaterEqualStatusByRepairInvoice(riid,
                            repairPackageStatusEvent.getStatus());
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("[][dealRepairPackageStatusEvent][riid:" + riid + ";allStatusIsOk:" + allStatusIsOk
                                + "]");
                    }
                    if (allStatusIsOk) {
                        publisher
                                .publishEvent(new RepairInvoiceStatusEvent(riid, repairPackageStatusEvent.getStatus()));
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("[][dealRepairPackageStatusEvent][publishEvent repairPackageStatusEvent,riid:"
                                    + riid + ";status:" + repairPackageStatusEvent.getStatus() + "]");
                        }
                    }
                }
                riids.clear();
            }
            riids = null;
        }
        catch (BaseDaoException e) {
            LOGGER.error("", e);
        }
        finally {
            packageDbo = null;
            repairPackageStatusEvent = null;
        }

    }

}
