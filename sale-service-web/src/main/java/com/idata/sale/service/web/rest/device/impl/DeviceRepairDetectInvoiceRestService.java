package com.idata.sale.service.web.rest.device.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.idata.sale.service.web.base.dao.constant.RepairStatus;
import com.idata.sale.service.web.base.dao.dbo.RepairDeviceDbo;
import com.idata.sale.service.web.base.dao.dbo.RepairDeviceDetectInvoiceDbo;
import com.idata.sale.service.web.base.service.IRepairDeviceDetectInvoiceService;
import com.idata.sale.service.web.base.service.IRepairDeviceService;
import com.idata.sale.service.web.base.service.ISystemUserService;
import com.idata.sale.service.web.base.service.ServiceException;
import com.idata.sale.service.web.rest.RestCode;
import com.idata.sale.service.web.rest.RestException;
import com.idata.sale.service.web.rest.device.DeviceTokenCheck;
import com.idata.sale.service.web.rest.device.dto.DeviceRepairDetectInvoiceDto;

@RestController
@RequestMapping(path = "/api/device/repair/detect/invoice", produces = { MediaType.APPLICATION_JSON_VALUE })
public class DeviceRepairDetectInvoiceRestService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DeviceRepairDetectInvoiceRestService.class);

    public DeviceRepairDetectInvoiceRestService() {
    }

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private IRepairDeviceDetectInvoiceService repairDeviceDetectInvoiceService;

    @DeviceTokenCheck
    @PostMapping(path = "save", consumes = "application/json;charset=UTF-8")
    public Object save(@RequestBody DeviceRepairDetectInvoiceDto invoiceDto) throws RestException, ServiceException {

        String uuid = invoiceDto.getUserId();

        if (StringUtils.isEmpty(uuid)) {
            throw new RestException(RestCode.FieldIsEmpty, "userId is empty");
        }

        Integer userId = systemUserService.getId(uuid);

        if (null == userId) {
            throw new RestException(RestCode.FieldValueNotSupport, "userId is error");
        }

        List<RepairDeviceDetectInvoiceDbo> detectInvoiceDbos = invoiceDto.getInvoices();
        if (CollectionUtils.isEmpty(detectInvoiceDbos)) {
            throw new RestException(RestCode.FieldIsEmpty, "Invoices is empty");
        }

        Integer repairDeviceId = null;

        for (RepairDeviceDetectInvoiceDbo repairDeviceDetectInvoiceDbo : detectInvoiceDbos) {

            if (null != repairDeviceDetectInvoiceDbo.getId() && repairDeviceDetectInvoiceDbo.getId() > 0) {
                continue;
            }

            if (null == repairDeviceDetectInvoiceDbo.getRepairDeviceId()) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("[][][repairDeviceId is null]");
                }
                throw new RestException(RestCode.FieldIsEmpty, "repairDeviceId is empty");
            }

            if (null == repairDeviceId) {
                repairDeviceId = repairDeviceDetectInvoiceDbo.getRepairDeviceId();
            }
            else {
                if (!repairDeviceId.equals(repairDeviceDetectInvoiceDbo.getRepairDeviceId())) {
                    throw new RestException(RestCode.FieldValueNotSupport, "multi repairDeviceId");
                }
            }

            if (null == repairDeviceDetectInvoiceDbo.getDeviceId()) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("[][][deviceId is null]");
                }
                throw new RestException(RestCode.FieldIsEmpty, "deviceId is empty");
            }
            if (StringUtils.isEmpty(repairDeviceDetectInvoiceDbo.getSn())) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("[][][sn is null]");
                }
                throw new RestException(RestCode.FieldIsEmpty, "sn is empty");
            }
            repairDeviceDetectInvoiceDbo.setCreateUserId(userId);
        }

        repairDeviceDetectInvoiceService.save(detectInvoiceDbos);

        return true;
    }

    @DeviceTokenCheck
    @RequestMapping(path = "get/list", method = RequestMethod.GET)
    public Object getList(@RequestParam("repairDeviceId") Integer repairDeviceId, @RequestParam("sn") String sn)
            throws RestException, ServiceException {
        if (null == repairDeviceId) {
            throw new RestException(RestCode.FieldIsEmpty, "repairDeviceId is empty");
        }
        return repairDeviceDetectInvoiceService.getListByRepairDevice(repairDeviceId);
    }

    @DeviceTokenCheck
    @RequestMapping(path = "get", method = RequestMethod.GET)
    public Object get(@RequestParam("id") Integer invoiceId, @RequestParam("sn") String sn)
            throws RestException, ServiceException {
        if (null == invoiceId) {
            throw new RestException(RestCode.FieldIsEmpty, "is is empty");
        }
        return repairDeviceDetectInvoiceService.get(invoiceId);
    }

    @Autowired
    private IRepairDeviceService repairDeviceService;

    @DeviceTokenCheck
    @RequestMapping(path = "delete/list", method = RequestMethod.POST, consumes = "application/json;charset=UTF-8")
    public Object deleteList(@RequestBody String body) throws RestException, ServiceException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(body);
        }

        Object bodyObj = JSONObject.parse(body);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(bodyObj.getClass().getName());
            LOGGER.debug("" + bodyObj);
        }

        JSONObject jsonBody = (JSONObject) bodyObj;

        Integer repairDeviceId = jsonBody.getInteger("repairDeviceId");

        String sn = jsonBody.getString("sn");

        String idsStr = jsonBody.getString("idsStr");

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("repairDeviceId:" + repairDeviceId + ";sn:" + sn + ";idsStr:" + idsStr);
        }

        if (null == repairDeviceId) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][][repairDeviceId is null]");
            }
            throw new RestException(RestCode.FieldIsEmpty, "repairDeviceId is empty");
        }

        if (StringUtils.isEmpty(idsStr)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][][invoice has lock ,don't delete]");
            }
            throw new RestException(RestCode.FieldIsEmpty, "idsStr is empty");
        }

        if (StringUtils.isEmpty(sn)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][][sn is null]");
            }
            throw new RestException(RestCode.FieldIsEmpty, "sn is empty");
        }

        RepairDeviceDbo repairDeviceDbo = repairDeviceService.get(repairDeviceId);
        if (null == repairDeviceDbo) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][][not found repairDeviceDbo,id:" + repairDeviceId + "]");
            }
            throw new RestException(RestCode.FieldValueNotSupport, "repairDeviceId is error");
        }

        // String sn = invoiceDto.getSn();

        if (!(sn.equals(repairDeviceDbo.getSn()))) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][][sn is error]");
            }
            throw new RestException(RestCode.FieldValueNotSupport, "sn is error");
        }

        if (repairDeviceDbo.getStatus() >= RepairStatus.Checked.getCode()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][][invoice has lock ,don't delete]");
            }
            throw new RestException(RestCode.RepairDeviceStatusError, "invoice has lock");
        }

        // String idsStr = invoiceDto.getIdsStr();

        String[] idArray = idsStr.split(",");
        List<Integer> invoiceIds = new ArrayList<>();
        for (String idStr : idArray) {
            if (StringUtils.isNotEmpty(idStr)) {
                invoiceIds.add(Integer.parseInt(idStr.trim()));
            }
        }

        repairDeviceDetectInvoiceService.delete(invoiceIds);

        return true;
    }

}
