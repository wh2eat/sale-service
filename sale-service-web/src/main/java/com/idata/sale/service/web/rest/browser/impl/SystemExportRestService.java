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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.idata.sale.service.web.base.dao.PageInfo;
import com.idata.sale.service.web.base.dao.dbo.SystemExportTaskDbo;
import com.idata.sale.service.web.base.dao.dbo.SystemUserDbo;
import com.idata.sale.service.web.base.dao.fo.SystemExportTaskFo;
import com.idata.sale.service.web.base.service.ISystemExportTaskService;
import com.idata.sale.service.web.base.service.ISystemUserService;
import com.idata.sale.service.web.base.service.ServiceException;
import com.idata.sale.service.web.base.service.dto.SystemRepairDeviceExportDto;
import com.idata.sale.service.web.rest.RestCode;
import com.idata.sale.service.web.rest.RestException;
import com.idata.sale.service.web.rest.RestResultFactory;
import com.idata.sale.service.web.rest.browser.dto.ExportRepairDeviceDto;

@RestController
@RequestMapping(path = "/api/browser/sys/export/", produces = { MediaType.APPLICATION_JSON_VALUE })
public class SystemExportRestService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SystemExportRestService.class);

    public SystemExportRestService() {

    }

    @Autowired
    private ISystemExportTaskService systemExportService;

    @Autowired
    private ISystemUserService userService;

    @RequestMapping(path = "repairDevice", method = RequestMethod.POST)
    public Object exportRepairDevice(@RequestBody ExportRepairDeviceDto exportRepairDeviceDto) throws RestException {

        String userId = exportRepairDeviceDto.getUserId();
        if (null == userId) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][][userId is empty]");
            }
            throw new RestException(RestCode.FieldIsEmpty, "userId");
        }

        SystemRepairDeviceExportDto repairDeviceExportDto = exportRepairDeviceDto.getParam();
        if (null == repairDeviceExportDto) {
            throw new RestException(RestCode.FieldIsEmpty);
        }

        String startTime = repairDeviceExportDto.getStartTime();
        if (StringUtils.isEmpty(startTime)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][][startTime is empty]");
            }
            throw new RestException(RestCode.FieldIsEmpty, "startTime");
        }

        String endTime = repairDeviceExportDto.getEndTime();
        if (StringUtils.isEmpty(endTime)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][][endTime is empty]");
            }
            throw new RestException(RestCode.FieldIsEmpty, "endTime");
        }

        SystemUserDbo userDbo = userService.get(userId);
        if (null == userDbo) {
            throw new RestException(RestCode.FieldValueNotSupport, "userId");
        }
        repairDeviceExportDto.setUserId(userDbo.getId());

        return systemExportService.exportRepairDeivce(repairDeviceExportDto);
    }

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

        SystemExportTaskFo exportTaskFo = null;
        if (StringUtils.isEmpty(params)) {
            throw new RestException(RestCode.FieldIsEmpty);
        }
        JSONObject paramsJson = JSON.parseObject(params);
        String uuid = paramsJson.getString("userId");
        if (StringUtils.isEmpty(uuid)) {
            throw new RestException(RestCode.FieldIsEmpty);
        }
        SystemUserDbo userDbo = userService.get(uuid);
        if (null == userDbo) {
            throw new RestException(RestCode.FieldIsEmpty);
        }

        paramsJson.put("userId", userDbo.getId());

        exportTaskFo = JSON.toJavaObject(paramsJson, SystemExportTaskFo.class);
        List<SystemExportTaskDbo> list = systemExportService.list(exportTaskFo, pageInfo);

        try {
            return RestResultFactory.getPageResult(list, pageInfo.getTotal());
        }
        finally {
            pageInfo = null;
        }
    }

    @RequestMapping(path = "delete", method = RequestMethod.POST)
    public Object deleteTask(@RequestBody ExportRepairDeviceDto exportRepairDeviceDto)
            throws RestException, ServiceException {

        String userId = exportRepairDeviceDto.getUserId();
        if (null == userId) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][][userId is empty]");
            }
            throw new RestException(RestCode.FieldIsEmpty, "userId");
        }

        SystemUserDbo userDbo = userService.get(userId);
        if (null == userDbo) {
            throw new RestException(RestCode.FieldValueNotSupport, "userId");
        }

        systemExportService.delete(exportRepairDeviceDto.getTaskId(), userDbo.getId());

        return true;
    }

}
