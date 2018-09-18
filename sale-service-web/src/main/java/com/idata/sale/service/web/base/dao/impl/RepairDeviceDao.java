package com.idata.sale.service.web.base.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.stereotype.Repository;

import com.idata.sale.service.web.base.dao.BaseDao;
import com.idata.sale.service.web.base.dao.BaseDaoException;
import com.idata.sale.service.web.base.dao.PageInfo;
import com.idata.sale.service.web.base.dao.constant.Device;
import com.idata.sale.service.web.base.dao.constant.RepairStatus;
import com.idata.sale.service.web.base.dao.dbo.RepairDeviceDbo;
import com.idata.sale.service.web.base.dao.fo.RepairDeviceFo;
import com.idata.sale.service.web.util.TimeUtil;

@Repository
public class RepairDeviceDao extends BaseDao<RepairDeviceDbo> {

    private final static Logger LOGGER = LoggerFactory.getLogger(RepairDeviceDao.class);

    public RepairDeviceDao() {

    }

    public List<RepairDeviceDbo> list4Search(RepairDeviceFo deviceFo) {

        String sql = _select
                + "id,repair_invoice_id,repair_package_id,repair_back_package_id,repair_station_id,sn,model,status,update_time"
                + _from + getTableName() + _where + "1=1";

        List<Object> values = new ArrayList<>();
        String sn = deviceFo.getSn();
        if (StringUtils.isNotEmpty(sn)) {
            sql += _and + "sn=?";
            values.add(sn);
        }

        Integer repairPackageId = deviceFo.getRepairPackageId();
        if (null != repairPackageId) {
            sql += _and + "repair_package_id=?";
            values.add(repairPackageId);
        }

        try {
            return findList(sql, values.toArray());
        }
        finally {
            sql = null;
            values.clear();

            values = null;
        }

    }

    public List<RepairDeviceDbo> list(PageInfo pageInfo, RepairDeviceDbo deviceDbo) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("" + deviceDbo);
        }

        String sql = _select + getAllCloumnStrWithComma() + _from + getTableName() + _where;
        sql += " 1=1 ";

        List<Object> values = new ArrayList<>();

        if (null != deviceDbo) {
            if (null != deviceDbo.getRepairInvoiceId()) {
                sql += _and + "repair_invoice_id=?";
                values.add(deviceDbo.getRepairInvoiceId());
            }

            if (null != deviceDbo.getRepairPackageId()) {
                sql += _and + "repair_package_id=?";
                values.add(deviceDbo.getRepairPackageId());
            }

            if (null != deviceDbo.getRepairStationId()) {
                sql += _and + "repair_station_id=?";
                values.add(deviceDbo.getRepairStationId());
            }

            if (null != deviceDbo.getRepairBackPackageId()) {

                if (-1 == deviceDbo.getRepairBackPackageId().intValue()) {
                    sql += _and + "repair_back_package_id is null";
                }
                else {
                    sql += _and + "repair_back_package_id=?";
                    values.add(deviceDbo.getRepairBackPackageId());
                }
            }

            if (null != deviceDbo.getNotRepairBackPackageId()) {
                sql += _and + "(not(repair_back_package_id=?) or repair_back_package_id is null)";
                values.add(deviceDbo.getNotRepairBackPackageId());
            }

            if (null != deviceDbo.getStatus()) {
                sql += _and + "status=?";
                values.add(deviceDbo.getStatus());
            }

            if (null != deviceDbo.getNotStatus()) {
                sql += _and + "not(status=?)";
                values.add(deviceDbo.getNotStatus());
            }

            if (null != deviceDbo.getLessThanStatus()) {
                sql += _and + "status<?";
                values.add(deviceDbo.getLessThanStatus());
            }

            if (StringUtils.isNotEmpty(deviceDbo.getExpressNumber())) {
                sql += _and + "locate(?,express_number)>0";
                values.add(deviceDbo.getExpressNumber());
            }

            if (StringUtils.isNotEmpty(deviceDbo.getEndCustomerName())) {
                sql += _and + "locate(?,end_customer_name)>0";
                values.add(deviceDbo.getEndCustomerName());
            }

            String sn = deviceDbo.getSn();
            if (StringUtils.isNotEmpty(sn)) {
                String[] sns = sn.split(",");
                sql += _and + "(1!=1";
                for (String ss : sns) {
                    if (StringUtils.isNotBlank(ss)) {
                        sql += _or + "locate(?,sn)>0";
                        values.add(ss);
                    }
                }
                sql += ")";
            }

        }

        sql += " order by id desc";

        try {
            return findListByPage(sql, values.toArray(), pageInfo);
        }
        catch (BaseDaoException e) {
            LOGGER.error("[][list][failed]", e);
        }

        return null;
    }

    private final static String TABLE_ALIAS = "_rp";

    public List<RepairDeviceDbo> getList4Device(RepairDeviceFo deviceFo) {

        String fields = TABLE_ALIAS + ".id," + TABLE_ALIAS + ".device_id," + TABLE_ALIAS + ".charge," + TABLE_ALIAS
                + ".cost_total," + TABLE_ALIAS + ".sn," + TABLE_ALIAS + ".model," + TABLE_ALIAS + ".status,"
                + TABLE_ALIAS + ".manufacture_time," + TABLE_ALIAS + ".warranty_type," + TABLE_ALIAS + ".warranty_type,"
                + TABLE_ALIAS + ".fault_description," + TABLE_ALIAS + ".attachment," + TABLE_ALIAS + ".repair_times,"
                + TABLE_ALIAS + ".last_repair_time," + TABLE_ALIAS + ".delivery_time," + TABLE_ALIAS + ".pay_type,"
                + TABLE_ALIAS + ".cost_total" + "," + TABLE_ALIAS + ".currency" + "," + TABLE_ALIAS + ".back_time" + ","
                + TABLE_ALIAS + ".end_customer_name" + "," + TABLE_ALIAS + ".repair_user_id" + "," + TABLE_ALIAS
                + ".quotation_user_id";

        return list(TABLE_ALIAS, fields, null, deviceFo);
    }

    public List<RepairDeviceDbo> list(PageInfo pageInfo, RepairDeviceFo deviceFo) {
        return list(TABLE_ALIAS, null, pageInfo, deviceFo);
    }

    private List<RepairDeviceDbo> list(String tabAlias, String fields, PageInfo pageInfo, RepairDeviceFo deviceFo) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][list][" + deviceFo + "]");
        }

        String sql = _select + (null == fields ? getAllCloumnStrWithComma(tabAlias) : fields) + _from
                + getTableName(tabAlias) + _where;
        sql += " 1=1 ";

        List<Object> values = new ArrayList<>();

        if (null != deviceFo) {
            if (StringUtils.isNotEmpty(deviceFo.getSn())) {
                String[] sns = deviceFo.getSn().split(",");
                sql += _and + "(1!=1";
                for (String subSn : sns) {
                    if (StringUtils.isEmpty(subSn)) {
                        continue;
                    }
                    sql += _or + "LOCATE(?," + tabAlias + ".sn)>0";
                    values.add(subSn);
                }
                sql += ")";
            }

            if (StringUtils.isNotEmpty(deviceFo.getEndCustomerName())) {
                String endCustomerName = deviceFo.getEndCustomerName();
                sql += _and + "LOCATE(?," + tabAlias + ".end_customer_name)>0";
                values.add(endCustomerName);
            }

            String status = deviceFo.getStatus();
            if (StringUtils.isNotBlank(status)) {

                String[] statuss = status.split(",");
                sql += _and + "(1!=1";
                for (String subStatus : statuss) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("[][][subStatus=" + subStatus + "]");
                    }
                    if (StringUtils.isEmpty(subStatus)) {
                        continue;
                    }

                    int scode = RepairStatus.getCode(subStatus);
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("[][][scode=" + scode + "]");
                    }
                    if (scode <= 0) {
                        continue;
                    }
                    sql += _or + "status=?";
                    values.add(scode);
                }

                if (RepairStatus.PayWait.getName().equals(status)) {
                    sql += _or + "(pay_status=? and status>=?)";
                    values.add(Device.PayNot);
                    values.add(RepairStatus.CustomerConfirmFinish);
                }

                sql += ")";
            }

            if (StringUtils.isNotEmpty(deviceFo.getMoreThanStatus())) {
                sql += _and + "status>?";
                values.add(RepairStatus.getCode(deviceFo.getMoreThanStatus()));
            }

            if (StringUtils.isNotEmpty(deviceFo.getLessThanStatus())) {
                sql += _and + "status<?";
                values.add(RepairStatus.getCode(deviceFo.getLessThanStatus()));
            }

            if (StringUtils.isNotBlank(deviceFo.getDeliveryTime())) {
                sql += _and + tabAlias + ".delivery_time>=? " + _and + "" + tabAlias + ".delivery_time<=? ";
                values.add(deviceFo.getDeliveryTime() + " 00:00:00");
                values.add(deviceFo.getDeliveryTime() + " 23:59:59");
            }

            if (StringUtils.isNotEmpty(deviceFo.getBackTime())) {
                sql += _and + tabAlias + ".back_time>=? " + _and + "" + tabAlias + ".back_time<=? ";
                values.add(deviceFo.getBackTime() + " 00:00:00");
                values.add(deviceFo.getBackTime() + " 23:59:59");
            }

            String invoiceSerialNumber = deviceFo.getInvoiceSerialNumber();
            String customerRma = deviceFo.getCustomerRma();

            if (StringUtils.isNotBlank(invoiceSerialNumber) || StringUtils.isNotBlank(customerRma)) {
                sql += _and + "exists(select id from repair_invoice _ri where _ri.id = " + tabAlias
                        + ".repair_invoice_id";

                if (StringUtils.isNotBlank(invoiceSerialNumber)) {
                    sql += _and + "LOCATE(?,_ri.serial_number)>0";
                    values.add(invoiceSerialNumber);
                }

                if (StringUtils.isNotBlank(customerRma)) {
                    sql += _and + "LOCATE(?,_ri.customer_rma)>0";
                    values.add(customerRma);
                }

                sql += ")";

            }

            String expressName = deviceFo.getExpressName();
            String expressNumber = deviceFo.getExpressNumber();
            if (StringUtils.isNotBlank(expressName) || StringUtils.isNotBlank(expressNumber)) {
                sql += _and + "exists(select id from repair_package _rp where _rp.id = " + tabAlias
                        + ".repair_package_id";

                if (StringUtils.isNotBlank(expressNumber)) {
                    sql += _and + "LOCATE(?,_rp.express_number)>0";
                    values.add(expressNumber);
                }

                if (StringUtils.isNotBlank(expressName)) {
                    sql += _and + "LOCATE(?,_rp.express_rma)>0";
                    values.add(expressName);
                }

                sql += ")";
            }

            Integer detectUserId = deviceFo.getDetectUserId();
            if (null != detectUserId) {
                sql += _and + tabAlias + ".detect_user_id=? ";
                values.add(detectUserId);
            }

            Integer repairStationId = deviceFo.getRepairStationId();
            if (null != repairStationId) {
                sql += _and + tabAlias + ".repair_station_id=? ";
                values.add(repairStationId);
            }

            Integer repairPackageId = deviceFo.getRepairPackageId();
            if (null != repairPackageId) {
                sql += _and + tabAlias + ".repair_package_id=? ";
                values.add(repairPackageId);
            }

            Integer repairBackPackageId = deviceFo.getRepairBackPackageId();
            if (null != repairBackPackageId) {
                sql += _and + tabAlias + ".repair_back_package_id=? ";
                values.add(repairBackPackageId);
            }
            else {
                if (deviceFo.isRepairBackPackageIdIsNull()) {
                    sql += _and + tabAlias + ".repair_back_package_id is null ";
                }
            }

        }

        sql += " order by id desc";

        try {
            return findListByPage(sql, values.toArray(), pageInfo);
        }
        catch (BaseDaoException e) {
            LOGGER.error("[][list][failed]", e);
        }

        return null;
    }

    public void updateRepairDeviceStatus(Integer repairDeviceId, RepairDeviceDbo repairDeviceDbo) {

        List<Integer> ids = new ArrayList<>(1);
        ids.add(repairDeviceId);

        try {
            updateRepairDeviceStatus(ids, repairDeviceDbo);
        }
        finally {
            ids.clear();
            ids = null;
        }

    }

    public void updateRepairDeviceStatus(Collection<Integer> repairDeviceIds, RepairDeviceDbo repairDeviceDbo) {
        boolean rs = jdbcTemplate.execute(new UpdateRepairDeviceStatusCallback(repairDeviceIds, repairDeviceDbo));
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][][updateRepairDeviceStatus:" + rs + "]");
        }
    }

    private class UpdateRepairDeviceStatusCallback implements ConnectionCallback<Boolean> {

        private Collection<Integer> rDeviceIds;

        private RepairDeviceDbo repairDevice;

        private String updateTime;

        private UpdateRepairDeviceStatusCallback(Collection<Integer> rDeviceIds, RepairDeviceDbo repairDevice) {
            this.rDeviceIds = rDeviceIds;
            this.repairDevice = repairDevice;
            this.updateTime = TimeUtil.getNowTimeStr();
        }

        @Override
        public Boolean doInConnection(Connection connection) throws SQLException, DataAccessException {

            boolean changeToManual = false;

            if (connection.getAutoCommit()) {
                connection.setAutoCommit(false);
                changeToManual = true;
            }

            List<Object> values = new ArrayList<>();

            java.sql.PreparedStatement pstmt = null;

            String sql = _update + getTableName() + _set + "update_time=? ";
            values.add(updateTime);

            if (null != repairDevice.getStatus()) {
                sql += ",status=? ";
                values.add(repairDevice.getStatus());

                if (RepairStatus.Checking.getCode() == repairDevice.getStatus().intValue()) {
                    sql += ",detect_start_time=? ";
                    values.add(updateTime);
                }

                if (RepairStatus.Checked.getCode() == repairDevice.getStatus().intValue()) {
                    sql += ",detect_finish_time=? ";
                    values.add(updateTime);
                }

                if (RepairStatus.CustomerConfirmFinish.getCode() == repairDevice.getStatus().intValue()) {
                    sql += ",quotation_finish_time=? ";
                    values.add(updateTime);
                }

                if (RepairStatus.Repairing.getCode() == repairDevice.getStatus().intValue()) {
                    sql += ",repair_start_time=? ";
                    values.add(updateTime);
                }

                if (RepairStatus.RepairFinish.getCode() == repairDevice.getStatus().intValue()) {
                    sql += ",repair_finish_time=? ";
                    values.add(updateTime);
                }

                if (RepairStatus.Backing.getCode() == repairDevice.getStatus().intValue()) {
                    sql += ",back_time=? ";
                    values.add(updateTime);
                }
            }

            if (null != repairDevice.getDetectUserId()) {
                sql += ",detect_user_id=? ";
                values.add(repairDevice.getDetectUserId());
            }

            if (null != repairDevice.getRepairUserId()) {
                sql += ",repair_user_id=? ";
                values.add(repairDevice.getRepairUserId());
            }

            if (null != repairDevice.getCharge()) {
                sql += ",charge=? ";
                values.add(repairDevice.getCharge());
            }

            if (null != repairDevice.getPayStatus()) {
                sql += ",pay_status=? ";
                values.add(repairDevice.getPayStatus());
            }
            if (null != repairDevice.getPayDescription()) {
                sql += ",pay_description=? ";
                values.add(repairDevice.getPayDescription());
            }
            if (null != repairDevice.getPayFinishTime()) {
                sql += ",pay_finish_time=? ";
                values.add(repairDevice.getPayFinishTime());
            }

            if (null != repairDevice.getDeliveryTime()) {
                sql += ",delivery_time=? ";
                values.add(TimeUtil.formatByYYYYmmDDhhMMSS(repairDevice.getDeliveryTime()));
            }

            if (null != repairDevice.getResult()) {
                sql += ",result=? ";
                values.add(repairDevice.getResult());
            }

            if (null != repairDevice.getLaborCosts()) {
                sql += ",labor_costs=? ";
                values.add(repairDevice.getLaborCosts());
            }

            if (null != repairDevice.getCostTotal()) {
                sql += ",cost_total=? ";
                values.add(repairDevice.getCostTotal());
            }

            sql += "where id=?";

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[UpdateRepairDeviceStatusCallback][][" + sql + "]");

                LOGGER.debug("[UpdateRepairDeviceStatusCallback][][" + values.toArray().toString() + "]");
            }

            try {
                pstmt = connection.prepareStatement(sql);

                int valuesLength = values.size();

                for (Integer deviceId : rDeviceIds) {

                    for (int i = 0; i < valuesLength; i++) {
                        pstmt.setObject((i + 1), values.get(i));
                    }
                    pstmt.setObject(valuesLength + 1, deviceId);
                    pstmt.addBatch();
                }

                int[] affects = pstmt.executeBatch();
                connection.commit();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("[UpdateRepairDeviceStatusCallback][][affects:" + Arrays.toString(affects) + "]");
                }

                return true;
            }
            catch (SQLException e) {
                throw e;
            }
            catch (DataAccessException e) {
                throw e;
            }
            finally {

                if (null != pstmt) {
                    pstmt.clearBatch();
                    pstmt.close();
                    pstmt = null;
                }

                if (changeToManual) {
                    connection.setAutoCommit(true);
                }

                sql = null;

                values.clear();
                values = null;
            }

        }
    }

    public void updateRepairBackPackage(Integer backPackageId, List<Integer> repairDeviceIds) {
        String nowStr = TimeUtil.getNowTimeStr();
        String sql = _update + getTableName() + _set + " repair_back_package_id=? ,update_time=? where id=?";
        List<Object[]> values = new ArrayList<>(repairDeviceIds.size());
        for (Integer repairDeviceId : repairDeviceIds) {
            Object[] vs = new Object[] { backPackageId, nowStr, repairDeviceId };
            values.add(vs);
        }
        try {
            batchUpdate(sql, values);
        }
        finally {
            values.clear();
            values = null;
            sql = null;
        }
    }

    public List<RepairDeviceDbo> getListByBackPackage(Integer repairBackPackageId) {
        String sql = _select + getAllCloumnStrWithComma() + _from + getTableName() + _where
                + " repair_back_package_id=?";
        Object[] values = new Object[] { repairBackPackageId };
        return findList(sql, values);
    }

    public Set<Integer> getRepairPackageIds(List<Integer> ids) {
        String sql = _select + "repair_package_id" + _from + getTableName() + _where + "1!=1";
        for (Integer id : ids) {
            sql += _or + "id='" + id + "'";
        }
        List<Map<String, Object>> maps = find(sql, null);

        sql = null;

        if (CollectionUtils.isNotEmpty(maps)) {
            try {
                Set<Integer> pids = new HashSet<>();
                for (Map<String, Object> map : maps) {
                    Object pObj = map.get("repair_package_id");
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

    public boolean isAllGreaterEqualStatusByRepairPackage(Integer id, int status) {
        String sql = _select + "count(id)" + _from + getTableName() + _where + "repair_package_id=? and status<?";
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

    public int count(Date start, Date end) {

        String sql = "select count(id) from repair_device where create_time >='"
                + TimeUtil.formatByYYYYmmDDhhMMSS(start) + "' and create_time <='"
                + TimeUtil.formatByYYYYmmDDhhMMSS(end) + "'";

        return (int) count(sql, null);
    }

    public List<Map<String, Object>> getMapList(String startTime, String endTime, int pageNum, int pageSize) {

        String sql = "select " + getAllCloumnStrWithComma() + " from " + getTableName()

                + " where create_time >= '" + startTime + "' and create_time<='" + endTime + "' limit "

                + (pageNum - 1) * pageSize + "," + pageSize;

        try {
            return find(sql, null);
        }
        finally {
            sql = null;
        }

    }

}
