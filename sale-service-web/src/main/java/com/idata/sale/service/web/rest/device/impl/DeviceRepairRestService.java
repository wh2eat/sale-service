package com.idata.sale.service.web.rest.device.impl;

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

import com.idata.sale.service.web.base.dao.dbo.RepairDeviceDbo;
import com.idata.sale.service.web.base.service.IRepairDeviceDetectInvoiceService;
import com.idata.sale.service.web.base.service.IRepairDeviceService;
import com.idata.sale.service.web.base.service.ISystemUserService;
import com.idata.sale.service.web.base.service.ServiceException;
import com.idata.sale.service.web.rest.RestCode;
import com.idata.sale.service.web.rest.RestException;
import com.idata.sale.service.web.rest.device.DeviceTokenCheck;
import com.idata.sale.service.web.rest.device.dto.DeviceRepairDto;

@RestController
@RequestMapping(path = "/api/device/repair/", produces = { MediaType.APPLICATION_JSON_VALUE })
public class DeviceRepairRestService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DeviceRepairRestService.class);

    public DeviceRepairRestService() {
    }

    @Autowired
    private IRepairDeviceService repairDeviceService;

    @DeviceTokenCheck
    @RequestMapping(path = "get/wait-detect/list", method = RequestMethod.GET)
    public Object getWaitDetectList(@RequestParam(name = "userId", required = false) String userUid,
            @RequestParam(name = "requestMillis", required = false) String requestMillis) throws RestException {
        if (StringUtils.isEmpty(userUid)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][][userUid is null]");
            }
            throw new RestException(RestCode.FieldIsEmpty, "userId is empty");
        }

        Integer repairStationId = systemUserService.getRepairStationId(userUid);
        if (null == repairStationId) {
            throw new RestException(RestCode.FieldValueNotSupport, "userId is error");
        }

        return repairDeviceService.getWaitDetectlist(repairStationId);
    }

    @DeviceTokenCheck
    @RequestMapping(path = "get/detecting/list", method = RequestMethod.GET)
    public Object getDetectingList(@RequestParam("userId") String userUid,
            @RequestParam(name = "requestMillis", required = false) String requestMillis) throws RestException {

        if (StringUtils.isEmpty(userUid)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][][userUid is null]");
            }
            throw new RestException(RestCode.FieldIsEmpty, "userId is empty");
        }

        Integer userId = systemUserService.getId(userUid);
        if (null == userId) {
            throw new RestException(RestCode.FieldValueNotSupport, "userId is error");
        }

        return setDetectInvoices(repairDeviceService.getDetectinglist(userId));
    }

    private List<RepairDeviceDbo> setDetectInvoices(List<RepairDeviceDbo> repairDeviceDbos) {
        if (CollectionUtils.isNotEmpty(repairDeviceDbos)) {
            repairDeviceDetectInvoiceService.setRepairDetectInvoices(repairDeviceDbos);
        }
        return repairDeviceDbos;
    }

    @DeviceTokenCheck
    @RequestMapping(path = "get/wait-repair/list", method = RequestMethod.GET)
    public Object getWaitRepairList(@RequestParam("userId") String userUid,
            @RequestParam(name = "requestMillis", required = false) String requestMillis) throws RestException {

        if (StringUtils.isEmpty(userUid)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][][userUid is null]");
            }
            throw new RestException(RestCode.FieldIsEmpty, "userId is empty");
        }

        Integer userId = systemUserService.getId(userUid);
        if (null == userId) {
            throw new RestException(RestCode.FieldValueNotSupport, "userId is error");
        }

        return setDetectInvoices(repairDeviceService.getWaitRepairlist(userId));
    }

    @Autowired
    private IRepairDeviceDetectInvoiceService repairDeviceDetectInvoiceService;

    @DeviceTokenCheck
    @RequestMapping(path = "get/repairing/list", method = RequestMethod.GET)
    public Object getRepairingList(@RequestParam("userId") String userUid,
            @RequestParam(name = "requestMillis", required = false) String requestMillis) throws RestException {

        if (StringUtils.isEmpty(userUid)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][][userUid is null]");
            }
            throw new RestException(RestCode.FieldIsEmpty, "userId is empty");
        }

        Integer userId = systemUserService.getId(userUid);
        if (null == userId) {
            throw new RestException(RestCode.FieldValueNotSupport, "userId is error");
        }

        return setDetectInvoices(repairDeviceService.getRepairinglist(userId));
    }

    @Autowired
    private ISystemUserService systemUserService;

    @DeviceTokenCheck
    @RequestMapping(path = "detect/start", method = RequestMethod.POST)
    public Object detectStart(@RequestBody DeviceRepairDto deviceRepairDto) throws RestException, ServiceException {

        String uuid = deviceRepairDto.getUserId();

        if (StringUtils.isEmpty(uuid)) {
            throw new RestException(RestCode.FieldIsEmpty, "userId is empty");
        }

        Integer userId = systemUserService.getId(uuid);

        if (null == userId) {
            throw new RestException(RestCode.FieldValueNotSupport, "userId is error");
        }

        Integer deviceId = deviceRepairDto.getId();
        if (null == deviceId) {
            throw new RestException(RestCode.FieldIsEmpty, "id is empty");
        }

        String deviceSn = deviceRepairDto.getSn();
        if (StringUtils.isEmpty(deviceSn)) {
            throw new RestException(RestCode.FieldIsEmpty, "sn is empty");
        }

        repairDeviceService.detectStart(userId, deviceId, deviceSn);

        return true;
    }

    @DeviceTokenCheck
    @RequestMapping(path = "detect/finish", method = RequestMethod.POST)
    public Object detectFinish(@RequestBody DeviceRepairDto deviceRepairDto) throws RestException, ServiceException {

        String uuid = deviceRepairDto.getUserId();

        if (StringUtils.isEmpty(uuid)) {
            throw new RestException(RestCode.FieldIsEmpty, "userId is empty");
        }

        Integer userId = systemUserService.getId(uuid);

        if (null == userId) {
            throw new RestException(RestCode.FieldValueNotSupport, "userId is error");
        }

        Integer deviceId = deviceRepairDto.getId();
        if (null == deviceId) {
            throw new RestException(RestCode.FieldIsEmpty, "id is empty");
        }

        String deviceSn = deviceRepairDto.getSn();
        if (StringUtils.isEmpty(deviceSn)) {
            throw new RestException(RestCode.FieldIsEmpty, "sn is empty");
        }

        repairDeviceService.detectFinish(userId, deviceId, deviceSn);

        return true;
    }

    @DeviceTokenCheck
    @RequestMapping(path = "start", method = RequestMethod.POST)
    public Object repairStart(@RequestBody DeviceRepairDto deviceRepairDto) throws RestException, ServiceException {

        String uuid = deviceRepairDto.getUserId();

        if (StringUtils.isEmpty(uuid)) {
            throw new RestException(RestCode.FieldIsEmpty, "userId is empty");
        }

        Integer userId = systemUserService.getId(uuid);

        if (null == userId) {
            throw new RestException(RestCode.FieldValueNotSupport, "userId is error");
        }

        Integer deviceId = deviceRepairDto.getId();
        if (null == deviceId) {
            throw new RestException(RestCode.FieldIsEmpty, "id is empty");
        }

        String deviceSn = deviceRepairDto.getSn();
        if (StringUtils.isEmpty(deviceSn)) {
            throw new RestException(RestCode.FieldIsEmpty, "sn is empty");
        }

        repairDeviceService.repairStart(userId, deviceId, deviceSn);

        return true;
    }

    @DeviceTokenCheck
    @RequestMapping(path = "finish", method = RequestMethod.POST)
    public Object repairFinish(@RequestBody DeviceRepairDto deviceRepairDto) throws RestException, ServiceException {

        String uuid = deviceRepairDto.getUserId();

        if (StringUtils.isEmpty(uuid)) {
            throw new RestException(RestCode.FieldIsEmpty, "userId is empty");
        }

        Integer userId = systemUserService.getId(uuid);

        if (null == userId) {
            throw new RestException(RestCode.FieldValueNotSupport, "userId is error");
        }

        Integer deviceId = deviceRepairDto.getId();
        if (null == deviceId) {
            throw new RestException(RestCode.FieldIsEmpty, "id is empty");
        }

        String deviceSn = deviceRepairDto.getSn();
        if (StringUtils.isEmpty(deviceSn)) {
            throw new RestException(RestCode.FieldIsEmpty, "sn is empty");
        }

        repairDeviceService.repairFinish(userId, deviceId, deviceSn);

        return true;
    }

}
