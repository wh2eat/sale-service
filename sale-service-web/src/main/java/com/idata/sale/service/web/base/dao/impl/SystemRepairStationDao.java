package com.idata.sale.service.web.base.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.idata.sale.service.web.base.dao.BaseDao;
import com.idata.sale.service.web.base.dao.BaseDaoException;
import com.idata.sale.service.web.base.dao.PageInfo;
import com.idata.sale.service.web.base.dao.dbo.SystemRepairStationDbo;

@Repository
public class SystemRepairStationDao extends BaseDao<SystemRepairStationDbo> {

    private final static Logger LOGGER = LoggerFactory.getLogger(SystemRepairStationDao.class);

    public SystemRepairStationDao() {

    }

    public SystemRepairStationDbo getByUdid(String udid) {

        String sql = _select + getAllCloumnStrWithComma() + _from + getTableName() + _where + " udid=?";
        Object[] values = new Object[] { udid };

        List<SystemRepairStationDbo> dbos = findList(sql, values);

        try {
            if (CollectionUtils.isNotEmpty(dbos)) {
                return dbos.get(0);
            }
        }
        finally {
            sql = null;
            values = null;
            if (null != dbos) {
                dbos.clear();
                dbos = null;
            }
        }
        return null;
    }

    public List<SystemRepairStationDbo> getList(PageInfo pageInfo) {

        String sql = _select + getAllCloumnStrWithComma() + _from + getTableName();

        try {
            return findListByPage(sql, null, pageInfo);
        }
        catch (BaseDaoException e) {
            LOGGER.error("[][][getList failed]", e);
        }
        finally {
            sql = null;
        }
        return null;
    }

    public List<SystemRepairStationDbo> getList(List<Integer> ids) {

        String sql = _select + getAllCloumnStrWithComma() + _from + getTableName() + _where + " 1!=1";
        for (Integer id : ids) {
            sql += _or + "id='" + id + "'";
        }

        try {
            return findList(sql, null);
        }
        finally {
            sql = null;
        }
    }

    public boolean hasRelation(Integer id) {

        String sql = _select + " id " + _from + getTableName() + _where + " id=? and "
                + " (exists(select id from system_user where repair_station_id=?) or "
                + "exists(select id from repair_device where repair_station_id=?) or "
                + "exists(select id from repair_invoice where repair_station_id=?)) ";
        Object[] values = new Object[] { id, id, id, id };

        try {
            List<Map<String, Object>> maps = find(sql, values);
            if (CollectionUtils.isEmpty(maps)) {
                return false;
            }
            maps.clear();
            maps = null;
            return true;
        }
        finally {
            sql = null;
            values = null;
        }
    }

}
