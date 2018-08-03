package com.idata.sale.service.web.base.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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
import com.idata.sale.service.web.base.dao.dbo.RepairPackageDbo;
import com.idata.sale.service.web.base.dao.fo.RepairPackageFo;

@Repository
public class RepairPackageDao extends BaseDao<RepairPackageDbo> {

    private final static Logger LOGGER = LoggerFactory.getLogger(RepairPackageDao.class);

    public RepairPackageDao() {
    }

    public List<RepairPackageDbo> findList(RepairPackageFo repairPackageFo) {

        String sql = _select + getAllCloumnStrWithComma() + _from + getTableName() + _where + "1=1";

        Collection<Integer> ids = repairPackageFo.getIds();
        if (CollectionUtils.isNotEmpty(ids)) {
            sql += _and + "(1!=1";
            for (Integer id : ids) {
                sql += _or + "id='" + id + "'";
            }
            sql += ")";
        }

        return findList(sql, null);
    }

    public RepairPackageDbo getByExpressNumber(String expressNumber) {

        String sql = _select + getAllCloumnStrWithComma() + _from + getTableName() + _where + "express_number=?";
        Object[] values = new Object[] { expressNumber };

        List<RepairPackageDbo> repairPackageDbos = findList(sql, values);
        try {
            if (CollectionUtils.isNotEmpty(repairPackageDbos)) {
                return repairPackageDbos.get(0);
            }
        }
        finally {
            sql = null;
            values = null;
        }

        return null;

    }

    public List<RepairPackageDbo> findPageList(PageInfo pageInfo, RepairPackageDbo filterDbo) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][][" + filterDbo.toString() + "]");
        }

        String sql = _select + getAllCloumnStrWithComma() + _from + getTableName() + _where + " 1=1";

        List<Object> values = new ArrayList<>();
        if (null != filterDbo.getRepairInvoiceId()) {
            sql += _and + " repair_invoice_id=?";
            values.add(filterDbo.getRepairInvoiceId());
        }

        if (StringUtils.isNotBlank(filterDbo.getContacts())) {
            sql += _and + " LOCATE(?,contacts)>0";
            values.add(filterDbo.getContacts());
        }

        if (StringUtils.isNotBlank(filterDbo.getContactNumber())) {
            sql += _and + " LOCATE(?,contact_number)>0";
            values.add(filterDbo.getContactNumber());
        }

        if (StringUtils.isNotBlank(filterDbo.getExpressName())) {
            sql += _and + " LOCATE(?,express_name)>0";
            values.add(filterDbo.getExpressName());
        }

        if (StringUtils.isNotBlank(filterDbo.getExpressNumber())) {
            sql += _and + " LOCATE(?,express_number)>0";
            values.add(filterDbo.getExpressNumber());
        }

        if (null != filterDbo.getRepairStationId()) {
            sql += _and + " repair_station_id = ?";
            values.add(filterDbo.getRepairStationId());
        }

        sql += " order by create_time desc";

        try {
            return findListByPage(sql, values.toArray(), pageInfo);
        }
        catch (BaseDaoException e) {
            LOGGER.error("", e);
        }
        finally {
            sql = null;
            values = null;
        }
        return null;
    }

    public RepairPackageDbo get(String serialNumber) {

        String sql = _select + getAllCloumnStrWithComma() + _from + getTableName() + _where + " serial_number=?";
        Object[] values = new Object[] { serialNumber };

        try {

            List<RepairPackageDbo> repairPackageDbos = findList(sql, values);
            if (CollectionUtils.isNotEmpty(repairPackageDbos)) {
                return repairPackageDbos.get(0);
            }
        }
        finally {
            sql = null;
            values = null;
        }
        return null;
    }

    public List<RepairPackageDbo> getAll(Set<Integer> packageIds) {
        String sql = _select + getAllCloumnStrWithComma() + _from + getTableName() + _where + "1=1";
        sql += _and + "(1!=1";
        for (Integer pid : packageIds) {
            sql += _or + "id='" + pid + "'";
        }
        sql += ")";

        try {
            return findList(sql, null);
        }
        finally {
            sql = null;
        }

    }

    public Set<Integer> getRepairInvoiceIds(List<Integer> ids) {
        String sql = _select + "repair_invoice_id" + _from + getTableName() + _where + "1!=1";
        for (Integer id : ids) {
            sql += _or + "id='" + id + "'";
        }
        List<Map<String, Object>> maps = find(sql, null);

        sql = null;

        if (CollectionUtils.isNotEmpty(maps)) {
            try {
                Set<Integer> pids = new HashSet<>();
                for (Map<String, Object> map : maps) {
                    Object pObj = map.get("repair_invoice_id");
                    if (null != pObj) {
                        pids.add((Integer) pObj);
                    }
                }
                return pids;
            }
            finally {
                maps.clear();
                maps = null;
            }
        }
        return null;
    }

    public boolean isAllGreaterEqualStatusByRepairInvoice(Integer id, int status) {
        String sql = _select + "count(id)" + _from + getTableName() + _where + "repair_invoice_id=? and status<?";
        Object[] values = new Object[] { id, status };

        long count = getCount(sql, values);

        try {
            return count == 0;
        }
        finally {
            sql = null;
            values = null;
        }

    }

}
