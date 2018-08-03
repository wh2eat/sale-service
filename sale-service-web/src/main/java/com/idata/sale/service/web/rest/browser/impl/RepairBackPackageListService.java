package com.idata.sale.service.web.rest.browser.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.idata.sale.service.web.base.dao.PageInfo;
import com.idata.sale.service.web.base.dao.dbo.RepairBackPackageDbo;
import com.idata.sale.service.web.base.service.IRepairBackPackageService;
import com.idata.sale.service.web.base.service.IRepairDeviceService;
import com.idata.sale.service.web.base.service.ISystemRepairStationService;
import com.idata.sale.service.web.base.service.ISystemUserService;
import com.idata.sale.service.web.base.service.ServiceException;
import com.idata.sale.service.web.rest.RestCode;
import com.idata.sale.service.web.rest.RestException;
import com.idata.sale.service.web.rest.RestResultFactory;
import com.idata.sale.service.web.rest.browser.dto.RepairBackPackageListDto;

@RestController
@RequestMapping(path = "/api/browser/repair/back/package", produces = { MediaType.APPLICATION_JSON_VALUE })
public class RepairBackPackageListService {

    private final static Logger LOGGER = LoggerFactory.getLogger(RepairBackPackageListService.class);

    public RepairBackPackageListService() {
    }

    @Autowired
    private IRepairBackPackageService repairBackPackageService;

    @RequestMapping(path = "get/list", method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    public Object getList(@RequestParam("page") int pageNum, @RequestParam("limit") int limit,
            @RequestParam("params") String params) throws ServiceException, RestException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][getList][pageDto:" + pageNum + "," + limit + "," + params + "]");
        }

        if (StringUtils.isEmpty(params)) {
            throw new RestException(RestCode.FieldIsEmpty, "params is empty");
        }

        RepairBackPackageDbo repairBackPackage = JSONObject.toJavaObject(JSON.parseObject(params),
                RepairBackPackageDbo.class);
        if (null == repairBackPackage) {
            throw new RestException(RestCode.FieldIsEmpty, "params is empty");
        }

        if (StringUtils.isEmpty(repairBackPackage.getRepairStationUdid())) {
            throw new RestException(RestCode.FieldIsEmpty, "repairStationUdid is empty");
        }
        repairBackPackage.setRepairStationId(repairStationService.getId(repairBackPackage.getRepairStationUdid()));

        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(limit);

        List<RepairBackPackageDbo> list = repairBackPackageService.getList(pageInfo, repairBackPackage);

        try {
            return RestResultFactory.getPageResult(list, pageInfo.getTotal());
        }
        finally {
            pageInfo = null;
        }
    }

    @Autowired
    private ISystemRepairStationService repairStationService;

    @Autowired
    private ISystemUserService userService;

    @Autowired
    private IRepairDeviceService repairDeviceService;

    @RequestMapping(path = "save", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE })
    public Object saveRepairStation(@RequestBody RepairBackPackageListDto repairBackPackageListDto)
            throws ServiceException, RestException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][RepairBackPackageListDto][" + repairBackPackageListDto + "]");
        }

        if (null == repairBackPackageListDto || null == repairBackPackageListDto.getBackPackage()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("");
            }
            throw new RestException(RestCode.FieldIsEmpty, "repairBackPackage is empty");
        }

        if (StringUtils.isEmpty(repairBackPackageListDto.getUserId())) {
            throw new RestException(RestCode.FieldIsEmpty, "userId is empty");
        }

        if (StringUtils.isEmpty(repairBackPackageListDto.getBackPackage().getExpressNumber())) {
            throw new RestException(RestCode.FieldIsEmpty, "expressNumber is empty");
        }

        if (StringUtils.isEmpty(repairBackPackageListDto.getBackPackage().getExpressName())) {
            throw new RestException(RestCode.FieldIsEmpty, "expressName is empty");
        }

        if (StringUtils.isEmpty(repairBackPackageListDto.getBackPackage().getContacts())) {
            throw new RestException(RestCode.FieldIsEmpty, "contacts is empty");
        }

        if (StringUtils.isEmpty(repairBackPackageListDto.getBackPackage().getContactAddress())) {
            throw new RestException(RestCode.FieldIsEmpty, "contactAddress is empty");
        }

        RepairBackPackageDbo backPackageDbo = repairBackPackageListDto.getBackPackage();

        Integer id = backPackageDbo.getId();
        if (null == id) {

            String repairStationUdid = backPackageDbo.getRepairStationUdid();
            if (StringUtils.isEmpty(repairStationUdid)) {
                throw new RestException(RestCode.FieldIsEmpty, "repairStationUdid is empty");
            }
            backPackageDbo.setRepairStationId(repairStationService.getIdNotNull(repairStationUdid));

            String userId = repairBackPackageListDto.getUserId();
            if (StringUtils.isEmpty(userId)) {
                throw new RestException(RestCode.FieldIsEmpty, "userId is empty");
            }
            backPackageDbo.setCreateUserId(userService.getId(userId));
            int bpid = repairBackPackageService.add(backPackageDbo);

            if (CollectionUtils.isNotEmpty(repairBackPackageListDto.getRepairDeviceIds())) {
                repairDeviceService.addToBackPackage(bpid, repairBackPackageListDto.getRepairDeviceIds());
            }
        }
        else {
            repairBackPackageService.update(backPackageDbo);
        }

        return true;
    }

    @RequestMapping(path = "get", method = RequestMethod.GET)
    public Object get(@RequestParam("id") String serialNumber) throws ServiceException, RestException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][getUser][serialNumber:" + serialNumber + "]");
        }

        if (StringUtils.isEmpty(serialNumber) || StringUtils.isEmpty(serialNumber)) {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][][id is empty]");
            }

            throw new RestException(RestCode.FieldIsEmpty, "id is null");
        }

        return repairBackPackageService.get(serialNumber);
    }

    @RequestMapping(path = "get/last", method = RequestMethod.GET)
    public Object getLast(@RequestParam("id") String serialNumber,
            @RequestParam("repairStationId") String repairStationUdid) throws ServiceException, RestException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][getUser][serialNumber:" + serialNumber + "]");
        }

        Integer repairStationId = repairStationService.getIdNotNull(repairStationUdid);

        return repairBackPackageService.getLast(serialNumber, repairStationId);
    }

    @RequestMapping(path = "delete", method = RequestMethod.DELETE)
    public Object delete(@RequestParam("id") String serialNumber) throws ServiceException, RestException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][getUser][id:" + serialNumber + "]");
        }

        if (StringUtils.isEmpty(serialNumber)) {
            throw new RestException(RestCode.FieldIsEmpty, "id is empty");
        }

        RepairBackPackageDbo repairBackPackageDbo = repairBackPackageService.get(serialNumber);
        if (null == repairBackPackageDbo) {
            throw new RestException(RestCode.FieldValueNotSupport, "not foud repairStation by id");
        }

        try {
            repairBackPackageService.delete(repairBackPackageDbo);
        }
        finally {
            repairBackPackageDbo = null;
        }

        return true;
    }

    @RequestMapping(path = "ship", method = RequestMethod.GET)
    public Object ship(@RequestParam("id") String serialNumber) throws ServiceException, RestException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][getUser][id:" + serialNumber + "]");
        }

        if (StringUtils.isEmpty(serialNumber)) {
            throw new RestException(RestCode.FieldIsEmpty, "id is empty");
        }

        RepairBackPackageDbo repairBackPackageDbo = repairBackPackageService.get(serialNumber);
        if (null == repairBackPackageDbo) {
            throw new RestException(RestCode.FieldValueNotSupport, "not foud repairStation by id");
        }

        try {
            repairBackPackageService.ship(repairBackPackageDbo);
        }
        finally {
            repairBackPackageDbo = null;
        }

        return true;
    }

}
