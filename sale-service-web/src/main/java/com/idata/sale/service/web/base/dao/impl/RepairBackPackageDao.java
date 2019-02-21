package com.idata.sale.service.web.base.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.idata.sale.service.web.base.dao.BaseDao;
import com.idata.sale.service.web.base.dao.BaseDaoException;
import com.idata.sale.service.web.base.dao.PageInfo;
import com.idata.sale.service.web.base.dao.dbo.RepairBackPackageDbo;

@Repository
public class RepairBackPackageDao extends BaseDao<RepairBackPackageDbo> {

    private final static Logger LOGGER = LoggerFactory.getLogger(RepairBackPackageDao.class);

    public RepairBackPackageDao() {
    }

    public List<RepairBackPackageDbo> getList(PageInfo pageInfo, RepairBackPackageDbo filterDbo) {

        String sql = _select + getAllCloumnStrWithComma() + _from + getTableName() + _where + " 1=1";
        List<Object> values = new ArrayList<>();

        if (null != filterDbo) {
            Integer repairStationId = filterDbo.getRepairStationId();
            if (null != repairStationId) {
                sql += _and + "repair_station_id=?";
                values.add(repairStationId);
            }

            String serialNumber = filterDbo.getSerialNumber();
            if (StringUtils.isNotEmpty(serialNumber)) {
                sql += _and + "serial_number=?";
                values.add(serialNumber);
            }
        }
        sql += " order by id desc";

        try {
            return findListByPage(sql, values.toArray(), pageInfo);
        }
        catch (BaseDaoException e) {
            LOGGER.error("[][][getList failed]", e);
        }
        finally {
            sql = null;
            values.clear();
            values = null;
        }
        return null;
    }

    public RepairBackPackageDbo get(String serialNumber) {
        String sql = _select + getAllCloumnStrWithComma() + _from + getTableName() + _where
                + " 1=1 and serial_number=?";
        Object[] values = new Object[] { serialNumber };
        List<RepairBackPackageDbo> backPackageDbos = findList(sql, values);
        try {
            if (CollectionUtils.isNotEmpty(backPackageDbos)) {
                return backPackageDbos.get(0);
            }
        }
        finally {
            if (null != backPackageDbos) {
                backPackageDbos.clear();
                backPackageDbos = null;
            }
            sql = null;
            values = null;
        }

        return null;
    }

    public RepairBackPackageDbo getByExpressNumber(String expressNumber) {
        String sql = _select + getAllCloumnStrWithComma() + _from + getTableName() + _where
                + " 1=1 and express_number=?";
        Object[] values = new Object[] { expressNumber };
        List<RepairBackPackageDbo> backPackageDbos = findList(sql, values);
        try {
            if (CollectionUtils.isNotEmpty(backPackageDbos)) {
                return backPackageDbos.get(0);
            }
        }
        finally {
            if (null != backPackageDbos) {
                backPackageDbos.clear();
                backPackageDbos = null;
            }
            sql = null;
            values = null;
        }

        return null;
    }

    public RepairBackPackageDbo getLast(Integer repairStationId) {
        String sql = _select + " max(id) as maxId" + _from + getTableName() + _where + "repair_station_id='"
                + repairStationId + "'";
        Integer maxId = null;
        try {
            maxId = getInteger(sql, null);
        }
        catch (SQLException e) {
            LOGGER.error("", e);
        }
        finally {
            sql = null;
        }

        if (null == maxId) {
            return null;
        }
        return findById(maxId);
    }

    public List<RepairBackPackageDbo> getList(Collection<Integer> ids) {

        String sql = _select + getAllCloumnStrWithComma() + _from + getTableName() + _where + "1!=1";
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

    public Map<String, Map<String, Object>> getIdMMap(Set<String> repairBackPackageIds) throws SQLException {

        StringBuilder sql = new StringBuilder("select ").append(getAllCloumnStrWithComma()).append(" from ")
                .append(getTableName()).append(" where id in(");
        for (String rbpid : repairBackPackageIds) {
            sql.append(rbpid).append(",");
        }
        sql.append(0).append(")");

        List<Map<String, Object>> maps = find(sql.toString(), null);

        try {
            if (CollectionUtils.isEmpty(maps)) {
                return new HashMap<>(1);
            }

            Map<String, Map<String, Object>> repairBackPacakgeMap = new HashMap<>();
            for (Map<String, Object> map : maps) {
                repairBackPacakgeMap.put(String.valueOf(map.get("id")), map);
            }

            maps.clear();
            maps = null;

            return repairBackPacakgeMap;
        }
        finally {
            sql = null;
        }
    }

}
