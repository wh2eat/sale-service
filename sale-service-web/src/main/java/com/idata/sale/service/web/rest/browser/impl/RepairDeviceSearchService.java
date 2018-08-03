package com.idata.sale.service.web.rest.browser.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.idata.sale.service.web.base.service.IRepairSearchService;
import com.idata.sale.service.web.base.service.ServiceException;
import com.idata.sale.service.web.rest.RestCode;
import com.idata.sale.service.web.rest.RestException;
import com.idata.sale.service.web.rest.browser.dto.RepairDeviceProcessDto;

@RestController
@RequestMapping(path = "/api/browser/repair/device/search", produces = { MediaType.APPLICATION_JSON_VALUE })
public class RepairDeviceSearchService {

    private final static Logger LOGGER = LoggerFactory.getLogger(RepairDeviceSearchService.class);

    public RepairDeviceSearchService() {
        // TODO Auto-generated constructor stub
    }

    @Autowired
    private IRepairSearchService repairSearchService;

    @RequestMapping(path = "process", method = RequestMethod.POST)
    public Object searchProcess(@RequestBody RepairDeviceProcessDto processDto) throws RestException, ServiceException {

        String searchType = processDto.getSearchType();

        if (StringUtils.isEmpty(searchType)) {
            throw new RestException(RestCode.FieldIsEmpty, "searchType");
        }

        String searchValue = processDto.getSearchValue();
        if (StringUtils.isEmpty(searchValue)) {
            throw new RestException(RestCode.FieldIsEmpty, "searchValue");
        }

        if ("deviceSn".equals(searchType)) {
            return repairSearchService.searchBySn(searchValue);
        }
        else if ("expressNumber".equals(searchType)) {
            return repairSearchService.searchByExpressNumber(searchValue);
        }

        throw new RestException(RestCode.FieldValueNotSupport, "searchType");
    }

}
