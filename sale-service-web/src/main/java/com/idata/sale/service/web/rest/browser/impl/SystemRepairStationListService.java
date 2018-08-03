package com.idata.sale.service.web.rest.browser.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.idata.sale.service.web.base.dao.PageInfo;
import com.idata.sale.service.web.base.dao.dbo.SystemRepairStationDbo;
import com.idata.sale.service.web.base.service.ISystemRepairStationService;
import com.idata.sale.service.web.base.service.ServiceException;
import com.idata.sale.service.web.rest.RestCode;
import com.idata.sale.service.web.rest.RestException;
import com.idata.sale.service.web.rest.RestResultFactory;
import com.idata.sale.service.web.rest.browser.dto.SystemRepairStationListDto;

@RestController
@RequestMapping(path = "/api/browser/repair/station/", produces = { MediaType.APPLICATION_JSON_VALUE })
public class SystemRepairStationListService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SystemRepairStationListService.class);

    public SystemRepairStationListService() {
    }

    @Autowired
    private ISystemRepairStationService repairStationService;

    @RequestMapping(path = "get/all", method = RequestMethod.GET)
    public Object getAll() throws ServiceException, RestException {
        return repairStationService.getAll();
    }

    @RequestMapping(path = "get/list", method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    public Object getList(@RequestParam("page") int pageNum, @RequestParam("limit") int limit,
            @RequestParam("params") String params) throws ServiceException, RestException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][getList][pageDto:" + pageNum + "," + limit + "," + params + "]");
        }

        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(limit);

        List<SystemRepairStationDbo> list = repairStationService.getList(pageInfo);

        try {
            return RestResultFactory.getPageResult(list, pageInfo.getTotal());
        }
        finally {
            pageInfo = null;
        }

    }

    @RequestMapping(path = "save", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE })
    public Object saveRepairStation(@RequestBody SystemRepairStationListDto repairStationListDto)
            throws ServiceException, RestException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][saveRepairStation][" + repairStationListDto + "]");
        }

        if (null == repairStationListDto || null == repairStationListDto.getRepairStation()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("");
            }
            throw new RestException(RestCode.FieldIsEmpty, "repairStation is empty");
        }

        SystemRepairStationDbo repairStationDbo = repairStationListDto.getRepairStation();

        if (StringUtils.isEmpty(repairStationDbo.getName())) {
            throw new RestException(RestCode.FieldIsEmpty, "repairStation's name is empty");
        }

        repairStationService.save(repairStationDbo);

        return true;
    }

    @RequestMapping(path = "get", method = RequestMethod.GET)
    public Object getUser(@RequestParam("id") String udid) throws ServiceException, RestException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][getUser][id:" + udid + "]");
        }

        if (StringUtils.isEmpty(udid) || StringUtils.isEmpty(udid)) {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][][fieldName or fieldValue is empty]");
            }

            throw new RestException(RestCode.FieldIsEmpty);
        }

        return repairStationService.get(udid);
    }

    @RequestMapping(path = "delete", method = RequestMethod.DELETE)
    public Object delete(@RequestParam("id") String udid) throws ServiceException, RestException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][getUser][id:" + udid + "]");
        }

        if (StringUtils.isEmpty(udid)) {
            throw new RestException(RestCode.FieldIsEmpty, "id is empty");
        }

        SystemRepairStationDbo repairStationDbo = repairStationService.get(udid);
        if (null == repairStationDbo) {
            throw new RestException(RestCode.FieldValueNotSupport, "not foud repairStation by id");
        }

        try {
            repairStationService.delete(repairStationDbo);
        }
        finally {
            repairStationDbo = null;
        }

        return true;

    }

}
