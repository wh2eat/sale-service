package com.idata.sale.service.web.base.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idata.sale.service.web.base.dao.BaseDaoException;
import com.idata.sale.service.web.base.dao.PageInfo;
import com.idata.sale.service.web.base.dao.dbo.CustomerSuggestionDbo;
import com.idata.sale.service.web.base.dao.impl.CustomerSuggestionDao;
import com.idata.sale.service.web.base.service.ICustomerSuggestionService;
import com.idata.sale.service.web.base.service.dto.CustomerSuggestionDto;

@Service
public class CustomerSuggestionService implements ICustomerSuggestionService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CustomerSuggestionService.class);

    public CustomerSuggestionService() {
    }

    @Autowired
    private CustomerSuggestionDao suggestionDao;

    @Override
    public int add(CustomerSuggestionDto suggestionDto) {

        CustomerSuggestionDbo suggestionDbo = new CustomerSuggestionDbo();

        suggestionDbo.setCategory(suggestionDto.getCategory());
        suggestionDbo.setContact(suggestionDto.getContact());
        suggestionDbo.setCreateTime(new Date(System.currentTimeMillis()));
        suggestionDbo.setUpdateTime(new Date(System.currentTimeMillis()));
        suggestionDbo.setDescription(suggestionDto.getDesc());
        suggestionDbo.setDeviceAmount(suggestionDto.getDeviceNumber());
        suggestionDbo.setStatus("0");
        suggestionDbo.setSerialNumber(generateSerialNumber());
        suggestionDbo.setTelephone(suggestionDto.getPhoneNumber());

        List<String> urls = suggestionDto.getStoreUrl();
        if (null != urls && urls.size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (String url : urls) {
                builder.append(url).append(",");
            }
            suggestionDbo.setAttachment(builder.substring(0, builder.length() - 1).toString());
        }

        int id = -1;
        try {
            id = (int) suggestionDao.save(suggestionDbo);
        }
        catch (BaseDaoException e) {
            LOGGER.error("", e);
        }

        LOGGER.info("[][add][id:" + id + "][" + suggestionDbo + "]");

        return id;
    }

    private String generateSerialNumber() {
        return RandomStringUtils.randomAlphanumeric(8).toLowerCase();
    }

    @Override
    public List<CustomerSuggestionDbo> findList(PageInfo pageInfo) {

        return suggestionDao.findList(pageInfo);
    }

    @Override
    public CustomerSuggestionDbo get(String uuid, Integer id) {

        CustomerSuggestionDbo suggestionDbo = suggestionDao.findById(id);
        if (null == suggestionDbo) {
            LOGGER.error("[][get][failed,not found CustomerSuggestionDbo,uuid:" + uuid + ";id:" + id + "]");
            return null;
        }

        if (!suggestionDbo.getSerialNumber().equals(uuid)) {
            LOGGER.error("[][get][failed,serialNumber check failed,uuid:" + uuid + ";id:" + id + "]");
            return null;
        }

        return suggestionDbo;
    }

}
