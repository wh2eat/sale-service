package com.idata.sale.service.web.base.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.idata.sale.service.web.base.dao.BaseDao;
import com.idata.sale.service.web.base.dao.PageInfo;
import com.idata.sale.service.web.base.dao.dbo.RepairInvoiceDbo;

@Repository
public class RepairInvoiceDao extends BaseDao<RepairInvoiceDbo> {

    private final static Logger LOGGER = LoggerFactory.getLogger(RepairInvoiceDao.class);

    public RepairInvoiceDao() {
    }

    public List<RepairInvoiceDbo> findPageList(PageInfo pageInfo, RepairInvoiceDbo filterDbo, String createTime) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][][" + filterDbo + "]");
        }

        String sql = _select + getAllCloumnStrWithComma() + _from + getTableName() + _where + "1=1";

        List<Object> values = new ArrayList<>();
        if (null != filterDbo) {
            String rma = filterDbo.getCustomerRma();
            if (StringUtils.isNotBlank(rma)) {
                sql += _and + "LOCATE(?,customer_rma)>0";
                values.add(rma);
            }

            String contacts = filterDbo.getContacts();
            if (StringUtils.isNotBlank(contacts)) {
                sql += _and + "LOCATE(?,contacts)>0";
                values.add(contacts);
            }

            String serialNumber = filterDbo.getSerialNumber();
            if (StringUtils.isNotBlank(serialNumber)) {
                sql += _and + "LOCATE(?,serial_number)>0";
                values.add(serialNumber);
            }
        }

        if (StringUtils.isNotEmpty(createTime)) {
            sql += _and + "create_time>=?";
            values.add(createTime + " 00:00:00");

            sql += _and + "create_time<=?";
            values.add(createTime + " 23:59:59");
        }

        if (null != filterDbo.getRepairStationId()) {
            sql += _and + "repair_station_id=?";
            values.add(filterDbo.getRepairStationId());
        }

        sql += " order by create_time desc";

        try {
            return findListByPage(sql, values.toArray(), pageInfo);
        }
        catch (Exception e) {
            LOGGER.error("[][findPageList][failed]", e);
        }
        finally {
            sql = null;
        }
        return null;
    }

    public RepairInvoiceDbo get(String serialNumber) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][get][serialNumber:" + serialNumber + "]");
        }

        String sql = _select + getAllCloumnStrWithComma() + _from + getTableName() + _where + " serial_number=?";
        Object[] values = new Object[] { serialNumber };

        try {
            List<RepairInvoiceDbo> repairInvoiceDbos = findList(sql, values);

            if (CollectionUtils.isNotEmpty(repairInvoiceDbos)) {
                return repairInvoiceDbos.get(0);
            }
        }
        catch (Exception e) {
            LOGGER.error("[][get][failed]", e);
        }
        finally {
            sql = null;
            values = null;
        }
        return null;
    }

    public List<RepairInvoiceDbo> getAll(Set<Integer> ids) {

        String sql = _select + getAllCloumnStrWithComma() + _from + getTableName() + _where + " 1!=1 ";
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
}
