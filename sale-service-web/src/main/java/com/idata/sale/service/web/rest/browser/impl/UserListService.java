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
import com.idata.sale.service.web.base.dao.PageInfo;
import com.idata.sale.service.web.base.dao.constant.SystemUserType;
import com.idata.sale.service.web.base.dao.dbo.RepairDeviceDbo;
import com.idata.sale.service.web.base.dao.dbo.SystemUserDbo;
import com.idata.sale.service.web.base.service.ISystemRepairStationService;
import com.idata.sale.service.web.base.service.ISystemUserService;
import com.idata.sale.service.web.base.service.ServiceException;
import com.idata.sale.service.web.rest.RestCode;
import com.idata.sale.service.web.rest.RestException;
import com.idata.sale.service.web.rest.RestResultFactory;
import com.idata.sale.service.web.rest.browser.dto.UserListDto;

@RestController
@RequestMapping(path = "/api/browser/user/", produces = { MediaType.APPLICATION_JSON_VALUE })
public class UserListService {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserListService.class);

    public UserListService() {

    }

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ISystemRepairStationService repairStationService;

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

        if (StringUtils.isEmpty(params)) {
            throw new RestException(RestCode.FieldIsEmpty);
        }

        UserListDto userListDto = JSON.toJavaObject(JSON.parseObject(params), UserListDto.class);
        String repairStationUdid = userListDto.getRepairStationId();
        if (StringUtils.isEmpty(repairStationUdid)) {
            throw new RestException(RestCode.FieldIsEmpty);
        }
        Integer repairStationId = repairStationService.getIdNotNull(repairStationUdid);

        RepairDeviceDbo repairDeviceDbo = new RepairDeviceDbo();
        repairDeviceDbo.setRepairStationId(repairStationId);

        List<SystemUserDbo> list = systemUserService.getList(pageInfo, repairDeviceDbo);
        if (CollectionUtils.isNotEmpty(list)) {
            repairStationService.setRepairStation(list);
        }

        try {
            return RestResultFactory.getPageResult(list, pageInfo.getTotal());
        }
        finally {
            pageInfo = null;
        }

    }

    @RequestMapping(path = "exist", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE })
    public Object exist(@RequestBody UserListDto userListDto) throws ServiceException, RestException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][existByLoginName][" + userListDto + "]");
        }

        String fieldName = userListDto.getFieldName();
        if (StringUtils.isEmpty(fieldName)) {
            throw new RestException(RestCode.FieldIsEmpty, "fieldName");
        }

        String fieldValue = userListDto.getFieldValue();
        if (StringUtils.isEmpty(fieldValue)) {
            throw new RestException(RestCode.FieldIsEmpty, "fieldValue");
        }

        String udid = userListDto.getUdid();

        if ("loginName".equals(fieldName)) {
            return systemUserService.existByLoginName(fieldValue, udid);
        }
        else if ("email".equals(fieldName)) {
            return systemUserService.existByEmail(fieldValue, udid);
        }
        else if ("telephone".equals(fieldName)) {
            return systemUserService.existByTelephone(fieldValue, udid);
        }
        else {
            throw new RestException(RestCode.FieldValueNotSupport, "fieldValue=" + fieldValue);
        }
    }

    @RequestMapping(path = "save", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE })
    public Object saveUser(@RequestBody UserListDto userListDto) throws ServiceException, RestException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][existByLoginName][" + userListDto + "]");
        }

        if (null == userListDto || null == userListDto.getUser()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("");
            }
            throw new RestException(RestCode.FieldIsEmpty, "user is empty");
        }

        SystemUserDbo userDbo = userListDto.getUser();
        String loginName = userDbo.getLoginName();
        if (StringUtils.isEmpty(loginName)) {
            throw new RestException(RestCode.FieldIsEmpty, "loginName");
        }

        if (systemUserService.existByLoginName(loginName, userDbo.getUdid())) {
            throw new RestException(RestCode.UnknownException);
        }

        String email = userDbo.getEmail();
        if (StringUtils.isEmpty(email)) {
            throw new RestException(RestCode.FieldIsEmpty);
        }

        int userType = SystemUserType.getCode(userListDto.getUserType());
        userDbo.setType(userType);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][][userTypeStr:" + userListDto.getUserType() + ";userType:" + userType + "]");

        }

        String udid = userDbo.getUdid();
        if (StringUtils.isEmpty(udid)) {
            systemUserService.add(userDbo);
            return true;
        }
        else {
            return systemUserService.update(userDbo);
        }
    }

    @RequestMapping(path = "get", method = RequestMethod.GET)
    public Object getUser(@RequestParam("fieldName") String fieldName, @RequestParam("fieldValue") String fieldValue)
            throws ServiceException, RestException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][getUser][fieldName:" + fieldName + ";fieldValue:" + fieldValue + "]");
        }

        if (StringUtils.isEmpty(fieldName) || StringUtils.isEmpty(fieldValue)) {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][][fieldName or fieldValue is empty]");
            }

            throw new RestException(RestCode.FieldIsEmpty);
        }

        if (!"id".equals(fieldName)) {
            throw new RestException(RestCode.FieldValueNotSupport);
        }

        String udid = fieldValue;
        return systemUserService.get(udid);
    }

    @RequestMapping(path = "delete", method = RequestMethod.DELETE)
    public Object delete(@RequestParam("loginName") String loginName, @RequestParam("id") String id)
            throws ServiceException, RestException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][getUser][loginName:" + loginName + ";id:" + id + "]");
        }

        SystemUserDbo userDbo = systemUserService.get(id);
        if (null == userDbo) {
            throw new RestException(RestCode.UnknownException);
        }

        try {
            if (userDbo.getLoginName().equals(loginName)) {
                systemUserService.remove(userDbo.getId());
            }
        }
        finally {
            userDbo = null;
        }

        return true;

    }

}
