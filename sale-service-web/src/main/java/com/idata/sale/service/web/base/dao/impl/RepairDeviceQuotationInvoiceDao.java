package com.idata.sale.service.web.base.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
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
import com.idata.sale.service.web.base.dao.constant.RepairDeviceQuotationConfirmStatus;
import com.idata.sale.service.web.base.dao.dbo.RepairDeviceQuotationInvoiceDbo;
import com.idata.sale.service.web.util.TimeUtil;

@Repository
public class RepairDeviceQuotationInvoiceDao extends BaseDao<RepairDeviceQuotationInvoiceDbo> {

    private final static Logger LOGGER = LoggerFactory.getLogger(RepairDeviceQuotationInvoiceDao.class);

    public RepairDeviceQuotationInvoiceDao() {
    }

    public List<RepairDeviceQuotationInvoiceDbo> getListByRepairDevice(Integer repairDeviceId) {

        String sql = _select + getAllCloumnStrWithComma() + _from + getTableName() + _where + "repair_device_id=?";
        Object[] values = new Object[] { repairDeviceId };

        try {
            return findList(sql, values);
        }
        finally {
            sql = null;
            values = null;
        }
    }

    public List<RepairDeviceQuotationInvoiceDbo> getListByRepairDevice(List<Integer> repairDeviceIds) {

        String sql = _select + getAllCloumnStrWithComma() + _from + getTableName() + _where + "1!=1 ";

        for (Integer id : repairDeviceIds) {
            sql += _or + "repair_device_id='" + id + "'";
        }

        try {
            return findList(sql, null);
        }
        finally {
            sql = null;
        }
    }

    public void confirmQuotationInvoice(List<Integer> ids) {

        String sql = _update + getTableName() + _set + "confirm_status=?,update_time=? where id=?";

        String nowTime = TimeUtil.getNowTimeStr();
        List<Object[]> values = new ArrayList<>(ids.size());
        for (Integer id : ids) {
            values.add(new Object[] { RepairDeviceQuotationConfirmStatus.Confirmed.code, nowTime, id });
        }

        try {
            batchUpdate(sql, values);
        }
        finally {
            sql = null;
            values.clear();
            values = null;
        }
    }

    public void updateConfirmStatus(RepairDeviceQuotationInvoiceDbo quotationInvoiceDbo) {

        Integer confirmStatus = quotationInvoiceDbo.getConfirmStatus();
        if (null == confirmStatus) {
            confirmStatus = 9;
        }

        String confirmRemark = quotationInvoiceDbo.getConfirmRemark();
        if (StringUtils.isEmpty(confirmRemark)) {
            confirmRemark = "";
        }

        String sql = _update + getTableName() + _set + "`confirm_status`='" + confirmStatus + "',`confirm_remark`='"
                + confirmRemark + "',`update_time`='" + TimeUtil.getNowTimeStr() + "' where `id`="
                + quotationInvoiceDbo.getId();

        // final Object[] values = new Object[] { confirmStatus, confirmRemark,
        // TimeUtil.getNowTimeStr(),
        // quotationInvoiceDbo.getId() };

        try {
            int affect = update(sql, null);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("affect:" + affect);
            }
        }
        finally {
            sql = null;
        }
    }

    public Map<String, List<Map<String, Object>>> getRepairDeviceQuotationInvoiceMap(Set<String> repairDeviceIds)
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

        Map<String, List<Map<String, Object>>> repairDeviceQuotationInvoiceMap = new HashMap<>();
        for (Map<String, Object> map : maps) {
            String rdid = String.valueOf(map.get("repair_device_id"));
            List<Map<String, Object>> smaps = null;
            if (repairDeviceQuotationInvoiceMap.containsKey(rdid)) {
                smaps = repairDeviceQuotationInvoiceMap.get(rdid);
            }
            else {
                smaps = new ArrayList<>(3);
            }
            smaps.add(map);
            repairDeviceQuotationInvoiceMap.put(rdid, smaps);
        }

        maps.clear();
        maps = null;

        return repairDeviceQuotationInvoiceMap;
    }

    public List<Integer> getIds(List<Integer> repairDeviceDetectInvoiceIds) {
        String sql = "select id from " + getTableName() + " where detect_invoice_id in(0";
        for (Integer dtid : repairDeviceDetectInvoiceIds) {
            if (null != dtid) {
                sql += "," + dtid;
            }
        }
        sql += ")";
        List<Map<String, Object>> maps = find(sql, null);
        if (CollectionUtils.isNotEmpty(maps)) {
            List<Integer> ids = new ArrayList<>(maps.size());
            for (Map<String, Object> map : maps) {
                if (null != map.get("id")) {
                    ids.add((Integer) map.get("id"));
                }
            }
            return ids;
        }
        return null;
    }

    public void delete(List<Integer> ids) {
        batchDelete(ids.toArray());
    }

}
