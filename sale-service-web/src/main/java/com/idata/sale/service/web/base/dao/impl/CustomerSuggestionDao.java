package com.idata.sale.service.web.base.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.idata.sale.service.web.base.dao.BaseDao;
import com.idata.sale.service.web.base.dao.BaseDaoException;
import com.idata.sale.service.web.base.dao.PageInfo;
import com.idata.sale.service.web.base.dao.dbo.CustomerSuggestionDbo;

@Repository
public class CustomerSuggestionDao extends BaseDao<CustomerSuggestionDbo> {

    private final static Logger LOGGER = LoggerFactory.getLogger(CustomerSuggestionDao.class);

    public CustomerSuggestionDao() {

    }

    public List<CustomerSuggestionDbo> findList(PageInfo pageInfo) {

        String sql = "select " + getAllCloumnStrWithComma() + " from " + getTableName() + " order by create_time desc";

        try {
            return findListByPage(sql, null, pageInfo);
        }
        catch (BaseDaoException e) {
            LOGGER.error("[][findList][]", e);
        }

        return null;
    }

}
