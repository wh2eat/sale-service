package com.idata.sale.service.web.base.dao.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.idata.sale.service.web.base.dao.BaseDao;
import com.idata.sale.service.web.base.dao.dbo.DeviceDbo;

@Repository
public class DeviceDao extends BaseDao<DeviceDbo> {

    private final static Logger LOGGER = LoggerFactory.getLogger(DeviceDao.class);

    public DeviceDao() {
    }

    public DeviceDbo getBySn(String sn) {

        String sql = _select + getAllCloumnStrWithComma() + _from + getTableName() + _where + "sn=?";
        Object[] values = new Object[] { sn };

        try {
            List<DeviceDbo> deviceDbos = findList(sql, values);
            try {
                if (CollectionUtils.isNotEmpty(deviceDbos)) {
                    return deviceDbos.get(0);
                }
            }
            finally {
                if (null != deviceDbos) {
                    deviceDbos.clear();
                    deviceDbos = null;
                }
            }
        }
        finally {
            sql = null;
            values = null;
        }
        return null;
    }

}
