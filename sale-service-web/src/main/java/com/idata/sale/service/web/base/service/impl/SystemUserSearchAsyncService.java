package com.idata.sale.service.web.base.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import com.idata.sale.service.web.base.dao.BaseDaoException;
import com.idata.sale.service.web.base.dao.dbo.SystemUserSearchDbo;
import com.idata.sale.service.web.base.dao.impl.SystemUserSearchDao;

@Service
@EnableAsync
public class SystemUserSearchAsyncService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SystemUserSearchAsyncService.class);

    public SystemUserSearchAsyncService() {
        // TODO Auto-generated constructor stub
    }

    @Autowired
    private SystemUserSearchDao userSearchDao;

    @Async
    public void save(SystemUserSearchDbo userSearchDbo) {
        try {
            int id = (int) userSearchDao.save(userSearchDbo);
            userSearchDbo.setId(id);
            LOGGER.info("[][save][SystemUserSearchDbo success,id:" + userSearchDbo.toString() + "]");
        }
        catch (BaseDaoException e) {
            LOGGER.error("[][][]", e);
        }

    }

}
