package com.idata.sale.service.web.rest.browser.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.idata.sale.service.web.base.dao.PageInfo;
import com.idata.sale.service.web.base.dao.dbo.SystemCustomerDbo;
import com.idata.sale.service.web.base.service.ISystemCustomerService;
import com.idata.sale.service.web.base.service.ServiceException;
import com.idata.sale.service.web.rest.RestException;
import com.idata.sale.service.web.rest.RestResultFactory;

@RestController
@RequestMapping(path = "/api/browser/sys/customer/", produces = { MediaType.APPLICATION_JSON_VALUE })
public class SystemCustomerListService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SystemCustomerListService.class);

    public SystemCustomerListService() {
    }

    @Autowired
    private ISystemCustomerService customerService;

    @RequestMapping(path = "find/list", method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    public Object getList(@RequestParam("page") int pageNum, @RequestParam("limit") int limit,
            @RequestParam("params") String params) throws ServiceException, RestException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][getList][pageDto:" + pageNum + "," + limit + "," + params + "]");
        }

        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(limit);

        SystemCustomerDbo filterDbo = null;
        if (StringUtils.isNotBlank(params)) {
            filterDbo = JSON.toJavaObject(JSON.parseObject(params), SystemCustomerDbo.class);
        }

        List<SystemCustomerDbo> list = customerService.list(pageInfo, filterDbo);

        try {
            return RestResultFactory.getPageResult(list, pageInfo.getTotal());
        }
        finally {
            pageInfo = null;
        }
    }

}
