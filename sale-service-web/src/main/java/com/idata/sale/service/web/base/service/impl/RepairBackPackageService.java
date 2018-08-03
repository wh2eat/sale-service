package com.idata.sale.service.web.base.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.idata.sale.service.web.base.dao.BaseDaoException;
import com.idata.sale.service.web.base.dao.PageInfo;
import com.idata.sale.service.web.base.dao.constant.RepairStatus;
import com.idata.sale.service.web.base.dao.dbo.RepairBackPackageDbo;
import com.idata.sale.service.web.base.dao.dbo.RepairDeviceDbo;
import com.idata.sale.service.web.base.dao.impl.RepairBackPackageDao;
import com.idata.sale.service.web.base.dao.impl.RepairDeviceDao;
import com.idata.sale.service.web.base.service.IRepairBackPackageService;
import com.idata.sale.service.web.base.service.SerialNumberUtil;
import com.idata.sale.service.web.base.service.ServiceException;
import com.idata.sale.service.web.base.service.constant.ServiceCode;
import com.idata.sale.service.web.base.service.event.RepairDeviceStatusEvent;

@Service
public class RepairBackPackageService implements IRepairBackPackageService {

    private final static Logger LOGGER = LoggerFactory.getLogger(RepairBackPackageService.class);

    public RepairBackPackageService() {

    }

    @Autowired
    private RepairBackPackageDao repairBackPackageDao;

    @Override
    public RepairBackPackageDbo get(Integer id) {
        return repairBackPackageDao.findById(id);
    }

    @Override
    public RepairBackPackageDbo get(String serialNumber) {
        return repairBackPackageDao.get(serialNumber);
    }

    @Override
    public RepairBackPackageDbo getLast(String serialNumber, Integer repairStationId) {
        if (StringUtils.isNotEmpty(serialNumber)) {
            return get(serialNumber);
        }

        return repairBackPackageDao.getLast(repairStationId);
    }

    @Override
    public Integer getIdNotNull(String serialNumber) throws ServiceException {

        RepairBackPackageDbo repairBackPackageDbo = get(serialNumber);
        try {
            if (null != repairBackPackageDbo) {
                return repairBackPackageDbo.getId();
            }
        }
        finally {
            if (null != repairBackPackageDbo) {
                repairBackPackageDbo = null;
            }
        }

        throw new ServiceException(ServiceCode.system_object_not_exist_error, "not found backPackage");
    }

    @Override
    public List<RepairBackPackageDbo> getList(PageInfo pageInfo, RepairBackPackageDbo filterDbo)
            throws ServiceException {

        return repairBackPackageDao.getList(pageInfo, filterDbo);
    }

    @Override
    public Integer add(RepairBackPackageDbo repairStationDbo) throws ServiceException {

        repairStationDbo.setStatus(RepairStatus.BackWait.getCode());

        Date now = new Date(System.currentTimeMillis());
        repairStationDbo.setCreateTime(now);
        repairStationDbo.setUpdateTime(now);

        repairStationDbo.setSerialNumber(SerialNumberUtil.generateRepairBackPackageSerialNumber());

        try {
            long id = repairBackPackageDao.save(repairStationDbo);
            LOGGER.info("[][][add repairStationDbo success,id:" + id + ";" + repairStationDbo.toString() + "]");
            return (int) id;
        }
        catch (BaseDaoException e) {
            LOGGER.error("[][][add repairStationDbo failed]", e);
            throw new ServiceException(ServiceCode.system_db_exception, "[][][add repairStationDbo failed]");
        }
    }

    @Override
    public void update(RepairBackPackageDbo repairBackPackageDbo) throws ServiceException {

        repairBackPackageDbo.setUpdateTime(new Date(System.currentTimeMillis()));

        try {
            repairBackPackageDao.update(repairBackPackageDbo);
        }
        catch (BaseDaoException e) {
            LOGGER.error("[][][update repairStationDbo failed]", e);
            throw new ServiceException(ServiceCode.system_db_exception, e.getMessage());
        }
    }

    @Override
    public void delete(RepairBackPackageDbo repairBackPackageDbo) throws ServiceException {

        Integer status = repairBackPackageDbo.getStatus();
        if (null != status && RepairStatus.BackWait.getCode() < status.intValue()) {
            throw new ServiceException(ServiceCode.repair_device_status_error,
                    "repairBackPackage not allow delete,status:" + status);
        }

        try {
            repairBackPackageDao.delete(repairBackPackageDbo);
        }
        catch (BaseDaoException e) {
            LOGGER.error("[][][delete repairBackPackage failed]", e);
        }
    }

    @Autowired
    private RepairDeviceDao repairDeviceDao;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public void ship(RepairBackPackageDbo repairBackPackageDbo) throws ServiceException {

        if (repairBackPackageDbo.getStatus().intValue() != RepairStatus.BackWait.getCode()) {
            throw new ServiceException(ServiceCode.repair_device_status_error, "status error ");
        }

        List<RepairDeviceDbo> repairDeviceDbos = repairDeviceDao.getListByBackPackage(repairBackPackageDbo.getId());
        if (CollectionUtils.isEmpty(repairDeviceDbos)) {
            throw new ServiceException(ServiceCode.system_param_error, "no repairDevice in backPacakge");
        }

        for (RepairDeviceDbo repairDeviceDbo : repairDeviceDbos) {
            if (repairDeviceDbo.getStatus() != RepairStatus.BackWait.getCode()) {
                throw new ServiceException(ServiceCode.repair_device_status_error, "");
            }
        }

        List<Integer> repairDeviceIds = new ArrayList<>(repairDeviceDbos.size());
        for (RepairDeviceDbo repairDeviceDbo2 : repairDeviceDbos) {
            repairDeviceIds.add(repairDeviceDbo2.getId());
        }

        RepairDeviceStatusEvent repairDeviceStatusEvent = new RepairDeviceStatusEvent(repairDeviceIds,
                RepairStatus.Backing);
        eventPublisher.publishEvent(repairDeviceStatusEvent);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][publishEvent repairDeviceStatusEvent][" + repairDeviceStatusEvent + "]");
        }

        Date deliveryTime = new Date(System.currentTimeMillis());
        RepairBackPackageDbo backDbo = new RepairBackPackageDbo();
        backDbo.setId(repairBackPackageDbo.getId());
        backDbo.setDeliveryTime(deliveryTime);
        backDbo.setUpdateTime(deliveryTime);
        backDbo.setStatus(RepairStatus.Backing.getCode());

        try {
            repairBackPackageDao.update(backDbo);
        }
        catch (BaseDaoException e) {
            throw new ServiceException(ServiceCode.system_db_exception, e.getMessage());
        }
        finally {
            backDbo = null;
            repairDeviceIds.clear();
            repairDeviceIds = null;
        }

    }

    @Override
    public void setBackPackage(List<RepairDeviceDbo> repairDeviceDbos) {

        Set<Integer> ids = new HashSet(repairDeviceDbos.size());

        for (RepairDeviceDbo repairDeviceDbo : repairDeviceDbos) {
            if (null != repairDeviceDbo.getRepairBackPackageId()
                    && !ids.contains(repairDeviceDbo.getRepairBackPackageId())) {
                ids.add(repairDeviceDbo.getRepairBackPackageId());
            }
        }

        if (CollectionUtils.isEmpty(ids)) {
            return;
        }

        List<RepairBackPackageDbo> backPackageDbos = repairBackPackageDao.getList(ids);

        ids.clear();
        ids = null;

        if (CollectionUtils.isEmpty(backPackageDbos)) {
            return;
        }

        Map<Integer, RepairBackPackageDbo> map = new HashMap<>();
        for (RepairBackPackageDbo repairBackPackageDbo : backPackageDbos) {
            map.put(repairBackPackageDbo.getId(), repairBackPackageDbo);
        }

        for (RepairDeviceDbo repairDeviceDbo : repairDeviceDbos) {
            if (null != repairDeviceDbo.getRepairBackPackageId()) {
                repairDeviceDbo.setRepairBackPackage(map.get(repairDeviceDbo.getRepairBackPackageId()));
            }
        }

        map.clear();
        map = null;

    }

    @Override
    public RepairBackPackageDbo getByExpressNumber(String expressName) {
        return repairBackPackageDao.getByExpressNumber(expressName);
    }
}
