package com.idata.sale.service.web.base.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.idata.sale.service.web.base.dao.BaseDao;
import com.idata.sale.service.web.base.dao.constant.Device;
import com.idata.sale.service.web.base.dao.dbo.RepairDeviceDetectInvoiceDbo;

@Repository
public class RepairDeviceDetectInvoiceDao extends BaseDao<RepairDeviceDetectInvoiceDbo> {

    private final static Logger LOGGER = LoggerFactory.getLogger(RepairDeviceDetectInvoiceDao.class);

    public RepairDeviceDetectInvoiceDao() {
    }

    public List<RepairDeviceDetectInvoiceDbo> getListByRepairDevice(Integer id) {

        List<Integer> ids = Arrays.asList(id);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][][" + ids + "]");
        }
        try {
            return getListByRepairDevice(ids);
        }
        finally {
            if (null != ids) {
                ids = null;
            }
        }
    }

    public List<RepairDeviceDetectInvoiceDbo> getListByRepairDevice(Collection<Integer> ids) {
        String sql = _select + getAllCloumnStrWithComma() + _from + getTableName() + _where + " 1!=1";
        for (Integer id : ids) {
            sql += _or + "repair_device_id='" + id + "'";
        }
        try {
            return findList(sql, null);
        }
        finally {
            sql = null;
        }
    }

    public List<RepairDeviceDetectInvoiceDbo> getQuotedPriceListByRepairDevice(Integer id) {

        List<Integer> ids = Arrays.asList(id);

        try {
            return getQuotedPriceListByRepairDevice(ids);
        }
        finally {
            ids.clear();
            ids = null;
        }
    }

    public List<RepairDeviceDetectInvoiceDbo> getQuotedPriceListByRepairDevice(Collection<Integer> ids) {
        String sql = _select + getAllCloumnStrWithComma() + _from + getTableName() + _where
                + " quoted_price=? and (1!=1";
        for (Integer id : ids) {
            sql += _or + "repair_device_id='" + id + "'";
        }
        sql += ")";

        Object[] values = new Object[] { Device.QuotedPrice.getCode() };

        try {
            return findList(sql, values);
        }
        finally {
            sql = null;
            values = null;
        }
    }

    public void delete(List<Integer> invoiceIds) {
        batchDelete(invoiceIds.toArray());
    }

    public Map<String, List<Map<String, Object>>> getRepairDeviceDetectInvoiceMap(Set<String> repairDeviceIds)
            throws SQLException {

        StringBuilder sql = new StringBuilder("select ").append(getAllCloumnStrWithComma()).append(" from ")
                .append(getTableName()).append(" where repair_device_id in(");
        for (String rdid : repairDeviceIds) {
            sql.append(rdid).append(",");
        }
        sql.append(0).append(")");

        List<Map<String, Object>> maps = find(sql.toString(), null);

        if (CollectionUtils.isEmpty(maps)) {
            return null;
        }

        Map<String, List<Map<String, Object>>> repairDeviceDetectInvoiceMap = new HashMap<String, List<Map<String, Object>>>();
        for (Map<String, Object> map : maps) {
            String rdid = String.valueOf(map.get("repair_device_id"));
            List<Map<String, Object>> smaps = null;
            if (repairDeviceDetectInvoiceMap.containsKey(rdid)) {
                smaps = repairDeviceDetectInvoiceMap.get(rdid);
            }
            else {
                smaps = new ArrayList<>(3);
            }
            smaps.add(map);
            repairDeviceDetectInvoiceMap.put(rdid, smaps);
        }

        maps.clear();
        maps = null;

        return repairDeviceDetectInvoiceMap;
    }

}
