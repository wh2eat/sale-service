package com.idata.sale.service.web.base.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.idata.sale.service.web.base.dao.BaseDao;
import com.idata.sale.service.web.base.dao.dbo.SystemUserSearchDbo;

@Repository
public class SystemUserSearchDao extends BaseDao<SystemUserSearchDbo> {

    private final static Logger LOGGER = LoggerFactory.getLogger(SystemUserSearchDao.class);

    public SystemUserSearchDao() {
    }

}
