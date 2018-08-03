package com.idata.sale.service.web.base.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.idata.sale.service.web.base.dao.BaseDao;
import com.idata.sale.service.web.base.dao.BaseDaoException;
import com.idata.sale.service.web.base.dao.PageInfo;
import com.idata.sale.service.web.base.dao.dbo.RepairDeviceDbo;
import com.idata.sale.service.web.base.dao.dbo.SystemUserDbo;
import com.idata.sale.service.web.util.TimeUtil;

@Repository
public class SystemUserDao extends BaseDao<SystemUserDbo> {

    private final static Logger LOGGER = LoggerFactory.getLogger(SystemUserDao.class);

    public SystemUserDao() {

    }

    public List<SystemUserDbo> getListWithName(Collection<Integer> ids) {
        String sql = _select + "id,user_name,login_name" + _from + getTableName() + _where + "1=1 and (1!=1";
        for (Integer id : ids) {
            sql += _or + "id='" + id + "'";
        }
        sql += ")";

        try {
            return findList(sql, null);

        }
        finally {
            sql = null;
        }

    }

    public SystemUserDbo getUser(String loginName) {
        String sql = _select + getAllCloumnStrWithComma() + _from + getTableName() + _where + "login_name=?";
        Object[] values = new Object[] { loginName };

        List<SystemUserDbo> userDbos = null;

        try {
            userDbos = findList(sql, values);

            if (CollectionUtils.isNotEmpty(userDbos)) {
                return userDbos.get(0);
            }
        }
        finally {
            sql = null;
            values = null;
            if (null != userDbos) {
                userDbos.clear();
                userDbos = null;
            }
        }

        return null;
    }

    public List<SystemUserDbo> findPageList(PageInfo pageInfo, RepairDeviceDbo filter) {

        String sql = _select + getAllCloumnStrWithComma() + _from + getTableName() + _where + "1=1";
        if (null != filter) {
            Integer repairStationId = filter.getRepairStationId();
            sql += _and + "repair_station_id='" + repairStationId + "'";
        }

        try {
            return findListByPage(sql, null, pageInfo);
        }
        catch (BaseDaoException e) {
            LOGGER.error("[][][getUser failed]", e);
        }

        return null;
    }

    public SystemUserDbo getUserByUdid(String udid) {
        String sql = _select + getAllCloumnStrWithComma() + _from + getTableName() + _where + "udid=?";
        Object[] values = new Object[] { udid };

        List<SystemUserDbo> userDbos = null;
        ;
        try {
            userDbos = findList(sql, values);

            if (CollectionUtils.isNotEmpty(userDbos)) {
                return userDbos.get(0);
            }
        }
        finally {
            sql = null;
            values = null;
            if (null != userDbos) {
                userDbos.clear();
                userDbos = null;
            }
        }

        return null;
    }

    public boolean existByLoginName(String loginName) {
        String sql = _select + " count(id) " + _from + getTableName() + _where + "login_name=?";
        Object[] values = new Object[] { loginName };

        try {
            long allSize = getCount(sql, values);
            if (allSize > 0) {
                return true;
            }
        }
        finally {
            sql = null;
            values = null;
        }

        return false;
    }

    public void updateUserPassword(int userId, String password) {

        String updateSql = _update + getTableName() + _set + "password=?,update_time=?" + _where + "id=?";
        Object[] values = new Object[] { password, TimeUtil.getNowTimeStr(), userId };

        try {
            update(updateSql, values);
        }
        finally {
            updateSql = null;
            values = null;
        }
    }

    public void updateUserLoginTime(int userId) {

        String updateSql = _update + getTableName() + _set + "last_login_time=?,update_time=?" + _where + "id=?";
        String nowTime = TimeUtil.getNowTimeStr();
        Object[] values = new Object[] { nowTime, nowTime, userId };

        try {
            update(updateSql, values);
        }
        finally {
            updateSql = null;
            values = null;
            nowTime = null;
        }
    }

    public boolean exist(SystemUserDbo userDbo) throws SQLException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][exist][" + userDbo + "]");
        }

        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append(_select).append("count(id)").append(_from).append(getTableName()).append(_where)
                .append("(1!=1");
        List<Object> values = new ArrayList<>();
        if (StringUtils.isNotBlank(userDbo.getEmail())) {
            sqlBuilder.append(_or).append("email=?");
            values.add(userDbo.getEmail());
        }

        if (StringUtils.isNotBlank(userDbo.getLoginName())) {
            sqlBuilder.append(_or).append("login_name=?");
            values.add(userDbo.getLoginName());
        }

        if (StringUtils.isNotBlank(userDbo.getTelephone())) {
            sqlBuilder.append(_or).append("telephone=?");
            values.add(userDbo.getTelephone());
        }
        sqlBuilder.append(")");

        if (StringUtils.isNotBlank(userDbo.getUdid())) {
            sqlBuilder.append(_and).append("udid!=?");
            values.add(userDbo.getUdid());
        }
        else {
            sqlBuilder.append(_and).append("1=1");
        }

        try {
            if (!values.isEmpty()) {
                Object countObj = getObject(sqlBuilder.toString(), values.toArray());
                if (null != countObj) {
                    long count = (Long) countObj;
                    if (count > 0) {
                        return true;
                    }
                    else {
                        return false;
                    }
                }
            }
        }
        finally {
            values.clear();
            values = null;
            sqlBuilder = null;
        }

        return false;
    }

}
