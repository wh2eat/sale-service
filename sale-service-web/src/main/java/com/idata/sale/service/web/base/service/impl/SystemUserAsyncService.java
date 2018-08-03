package com.idata.sale.service.web.base.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import com.idata.sale.service.web.base.dao.impl.SystemUserDao;

@Service
@EnableAsync
public class SystemUserAsyncService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SystemUserAsyncService.class);

    public SystemUserAsyncService() {
    }

    @Autowired
    private SystemUserDao systemUserDao;

    @Async
    void updateLoginTime(int userId) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][updateLoginTime][start]");
        }
        systemUserDao.updateUserLoginTime(userId);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][updateLoginTime][finish]");
        }
    }

}
