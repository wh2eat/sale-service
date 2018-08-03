package com.idata.sale.service.web.base.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idata.sale.service.web.base.dao.BaseDaoException;
import com.idata.sale.service.web.base.dao.PageInfo;
import com.idata.sale.service.web.base.dao.dbo.SystemRepairStationDbo;
import com.idata.sale.service.web.base.dao.dbo.SystemUserDbo;
import com.idata.sale.service.web.base.dao.impl.SystemRepairStationDao;
import com.idata.sale.service.web.base.service.ISystemRepairStationService;
import com.idata.sale.service.web.base.service.ServiceException;
import com.idata.sale.service.web.base.service.constant.ServiceCode;

@Service
public class SystemRepairStationService implements ISystemRepairStationService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SystemRepairStationService.class);

    public SystemRepairStationService() {

    }

    @Autowired
    private SystemRepairStationDao repairStationDao;

    @Override
    public SystemRepairStationDbo get(Integer id) {
        if (null == id) {
            return null;
        }
        return repairStationDao.findById(id);
    }

    @Override
    public SystemRepairStationDbo get(String udid) {
        return repairStationDao.getByUdid(udid);
    }

    @Override
    public Integer getId(String udid) {

        SystemRepairStationDbo stationDbo = get(udid);

        try {
            if (null != stationDbo) {
                return stationDbo.getId();
            }
        }
        finally {
            stationDbo = null;
        }

        return null;
    }

    @Override
    public Integer getIdNotNull(String udid) throws ServiceException {

        Integer id = getId(udid);
        if (null != id) {
            return id;
        }
        throw new ServiceException(ServiceCode.system_object_not_exist_error,
                "not found repairStation by udid:" + udid);
    }

    @Override
    public List<SystemRepairStationDbo> getAll() {
        try {
            return repairStationDao.findAll();
        }
        catch (BaseDaoException e) {
            LOGGER.error("[][][getAll failed]", e);
        }
        return null;
    }

    @Override
    public List<SystemRepairStationDbo> getList(PageInfo pageInfo) throws ServiceException {
        return repairStationDao.getList(pageInfo);
    }

    @Override
    public void save(SystemRepairStationDbo repairStationDbo) throws ServiceException {

        repairStationDbo.setUpdateTime(new Date(System.currentTimeMillis()));

        Integer repairStationId = repairStationDbo.getId();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][][repairStationId:" + repairStationId + "]");
        }

        try {
            if (null != repairStationId) {
                repairStationDao.update(repairStationDbo);
                LOGGER.info("[][][update SystemRepairStationDbo][" + repairStationDbo.toString() + "]");
            }
            else {

                repairStationDbo.setUdid(getUdid(repairStationDbo.getName()));

                repairStationDbo.setCreateTime(new Date(System.currentTimeMillis()));
                repairStationId = (int) repairStationDao.save(repairStationDbo);
                repairStationDbo.setId(repairStationId);
                LOGGER.info("[][][add SystemRepairStationDbo][" + repairStationDbo.toString() + "]");
            }
        }
        catch (BaseDaoException e) {
            LOGGER.error("[][][save SystemRepairStationDbo failed]", e);
            throw new ServiceException(ServiceCode.system_db_exception, e.getMessage());
        }
    }

    private String getUdid(String name) {
        String serverName = name + "&" + System.currentTimeMillis() + "&" + RandomUtils.nextInt(10000);
        String uuid = UUID.nameUUIDFromBytes(serverName.getBytes()).toString();
        uuid = DigestUtils.md5Hex(uuid);
        return uuid.substring(0, 9);

    }

    public static void main(String[] args) {
    }

    @Override
    public void delete(SystemRepairStationDbo repairStationDbo) throws ServiceException {
        Integer repairStationId = repairStationDbo.getId();
        if (repairStationDao.hasRelation(repairStationId)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][][delete repairStationDbo failed,case:repairStationDbo has relation,id:"
                        + repairStationId + "]");
            }
            throw new ServiceException(ServiceCode.system_object_has_relation_error, "");
        }
        repairStationDao.deleteById(repairStationId);
        LOGGER.info("[][][delete success,repairStationId:" + repairStationId + "]");
    }

    @Override
    public void setRepairStation(List<SystemUserDbo> userDbos) throws ServiceException {

        List<Integer> rsids = new ArrayList<>();
        for (SystemUserDbo systemUserDbo : userDbos) {
            if (null != systemUserDbo.getRepairStationId()) {
                rsids.add(systemUserDbo.getRepairStationId());
            }
        }

        List<SystemRepairStationDbo> repairStationDbos = repairStationDao.getList(rsids);
        Map<Integer, SystemRepairStationDbo> map = new HashMap<>(repairStationDbos.size());
        for (SystemRepairStationDbo systemRepairStationDbo : repairStationDbos) {
            map.put(systemRepairStationDbo.getId(), systemRepairStationDbo);
        }

        for (SystemUserDbo systemUserDbo : userDbos) {
            Integer rsid = systemUserDbo.getRepairStationId();
            if (null != rsid) {
                systemUserDbo.setRepairStation(map.get(rsid));
            }
        }

        rsids.clear();
        rsids = null;

        repairStationDbos.clear();
        repairStationDbos = null;

        map.clear();
        map = null;

    }

}
