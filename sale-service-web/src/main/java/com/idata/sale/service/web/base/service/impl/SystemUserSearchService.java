package com.idata.sale.service.web.base.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idata.sale.service.web.base.dao.dbo.SystemUserSearchDbo;
import com.idata.sale.service.web.base.service.ISystemUserSearchService;

@Service
public class SystemUserSearchService implements ISystemUserSearchService {

    private final static Logger logger = LoggerFactory.getLogger(SystemUserSearchService.class);

    public SystemUserSearchService() {
        // TODO Auto-generated constructor stub
    }

    @Autowired
    private SystemUserSearchAsyncService asyncUserSearchService;

    @Override
    public void asyncSave(SystemUserSearchDbo searchDbo) {
        asyncUserSearchService.save(searchDbo);
    }

}
