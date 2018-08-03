package com.idata.sale.service.web.base.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.idata.sale.service.web.base.dao.BaseDao;
import com.idata.sale.service.web.base.dao.BaseDaoException;
import com.idata.sale.service.web.base.dao.PageInfo;
import com.idata.sale.service.web.base.dao.dbo.SystemCustomerDbo;

@Repository
public class SystemCustomerDao extends BaseDao<SystemCustomerDbo> {

    private final static Logger LOGGER = LoggerFactory.getLogger(SystemCustomerDbo.class);

    public SystemCustomerDao() {
    }

    public SystemCustomerDbo getByIdentifier(String identifer) {

        String sql = _select + getAllCloumnStrWithComma() + _from + getTableName() + _where + _b + "identifier=?";

        Object[] values = new Object[] { identifer };

        List<SystemCustomerDbo> customerDbos = findList(sql, values);
        try {
            if (CollectionUtils.isNotEmpty(customerDbos)) {
                return customerDbos.get(0);
            }
        }
        finally {
            sql = null;
            values = null;

            if (null != customerDbos) {
                customerDbos.clear();
                customerDbos = null;
            }
        }
        return null;
    }

    public boolean existByIdentifier(String identifer) {
        String sql = _select + "count(id)" + _from + getTableName() + _where + _b + "identifier=?";
        long count = getCount(sql, null);
        try {
            return count == 0;
        }
        finally {
            sql = null;
        }
    }

    public List<SystemCustomerDbo> findList(PageInfo pageInfo, SystemCustomerDbo filterDbo) {
        String sql = _select + getAllCloumnStrWithComma() + _from + getTableName() + _where + _b + "1=1";
        List<Object> values = new ArrayList<>(10);
        if (null != filterDbo) {
            if (StringUtils.isNotBlank(filterDbo.getName())) {
                sql += _and + "locate(?,name)>0";
                values.add(filterDbo.getName());
            }

            if (StringUtils.isNotBlank(filterDbo.getPhone())) {
                sql += _and + "locate(?,phone)>0";
                values.add(filterDbo.getPhone());
            }

            if (StringUtils.isNotBlank(filterDbo.getAddress())) {
                sql += _and + "locate(?,address)>0";
                values.add(filterDbo.getAddress());
            }
        }

        Object[] pvs = null;
        if (CollectionUtils.isNotEmpty(values)) {
            pvs = values.toArray();
        }

        try {
            return findListByPage(sql, pvs, pageInfo);
        }
        catch (BaseDaoException e) {
            LOGGER.error("[][][]", e);
        }
        finally {
            sql = null;

            values.clear();
            values = null;

            if (pvs != null) {
                pvs = null;
            }
        }

        return null;
    }

}
