package com.idata.sale.service.web.rest.browser.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
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

import com.alibaba.fastjson.JSONObject;
import com.idata.sale.service.web.base.dao.PageInfo;
import com.idata.sale.service.web.base.dao.constant.Device;
import com.idata.sale.service.web.base.dao.constant.RepairStatus;
import com.idata.sale.service.web.base.dao.dbo.RepairBackPackageDbo;
import com.idata.sale.service.web.base.dao.dbo.RepairDeviceDbo;
import com.idata.sale.service.web.base.dao.dbo.RepairDeviceDetectInvoiceDbo;
import com.idata.sale.service.web.base.dao.dbo.RepairDeviceQuotationInvoiceDbo;
import com.idata.sale.service.web.base.dao.dbo.RepairInvoiceDbo;
import com.idata.sale.service.web.base.dao.dbo.RepairPackageDbo;
import com.idata.sale.service.web.base.dao.fo.RepairDeviceFo;
import com.idata.sale.service.web.base.service.IRepairBackPackageService;
import com.idata.sale.service.web.base.service.IRepairDeviceDetectInvoiceService;
import com.idata.sale.service.web.base.service.IRepairDeviceQuotationInvoiceService;
import com.idata.sale.service.web.base.service.IRepairDeviceService;
import com.idata.sale.service.web.base.service.IRepairInvoiceService;
import com.idata.sale.service.web.base.service.IRepairPackageService;
import com.idata.sale.service.web.base.service.ISystemRepairStationService;
import com.idata.sale.service.web.base.service.ISystemUserService;
import com.idata.sale.service.web.base.service.ServiceException;
import com.idata.sale.service.web.rest.RestCode;
import com.idata.sale.service.web.rest.RestException;
import com.idata.sale.service.web.rest.RestResultFactory;
import com.idata.sale.service.web.rest.browser.dto.RepairDeviceListDto;
import com.idata.sale.service.web.rest.browser.dto.RepairDeviceQuotationInvoiceDto;

@RestController
@RequestMapping(path = "/api/browser/repair/device", produces = { MediaType.APPLICATION_JSON_VALUE })
public class RepairDeviceListService {

    private final static Logger LOGGER = LoggerFactory.getLogger(RepairDeviceListService.class);

    public RepairDeviceListService() {

    }

    @Autowired
    private IRepairDeviceService repairDeviceService;

    @RequestMapping(path = "get/invoice", method = RequestMethod.GET)
    public Object getInvoiceData(@RequestParam("pid") String packageSerialNumber,
            @RequestParam("iid") String invoiceSerialNumber) throws ServiceException, RestException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][getUser][packageSerialNumber:" + packageSerialNumber + ";invoiceSerialNumber:"
                    + invoiceSerialNumber + "]");
        }

        if (StringUtils.isBlank(packageSerialNumber) || StringUtils.isEmpty(invoiceSerialNumber)) {
            throw new RestException(RestCode.FieldIsEmpty);
        }

        return repairDeviceService.getInvoiceData(packageSerialNumber, invoiceSerialNumber);
    }

    @RequestMapping(path = "get/base", method = RequestMethod.GET)
    public Object getBaseData(@RequestParam("sn") String sn) throws ServiceException, RestException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][getUser][sn:" + sn + "]");
        }

        if (StringUtils.isBlank(sn)) {
            throw new RestException(RestCode.FieldIsEmpty);
        }

        return repairDeviceService.getBaseData(sn);
    }

    @Autowired
    private IRepairInvoiceService repairInvoiceService;

    @Autowired
    private IRepairPackageService repairPackageService;

    @RequestMapping(path = "save", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE })
    public Object save(@RequestBody RepairDeviceListDto deviceListDto) throws RestException, ServiceException {

        RepairDeviceDbo deviceDbo = deviceListDto.getDevice();

        if (null == deviceDbo) {
            throw new RestException(RestCode.FieldIsEmpty);
        }

        if (StringUtils.isEmpty(deviceListDto.getUserId())) {
            throw new RestException(RestCode.FieldIsEmpty, "userId");
        }

        if (StringUtils.isEmpty(deviceDbo.getSn())) {
            throw new RestException(RestCode.FieldIsEmpty, "sn");
        }

        deviceDbo.setSn(deviceDbo.getSn().trim());

        Integer id = deviceDbo.getId();

        if (null == id) {

            Integer userId = systemUserService.getId(deviceListDto.getUserId());
            if (null == userId) {
                throw new RestException(RestCode.FieldIsEmpty, "userId");
            }

            String invoiceSerialNumber = deviceListDto.getInvoiceSerialNumber();
            RepairInvoiceDbo invoiceDbo = repairInvoiceService.get(invoiceSerialNumber);
            if (null == invoiceDbo) {
                throw new RestException(RestCode.FieldIsEmpty);
            }
            int invoiceId = invoiceDbo.getId();
            deviceDbo.setRepairInvoiceId(invoiceId);
            deviceDbo.setRepairStationId(invoiceDbo.getRepairStationId());
            invoiceDbo = null;

            String packageSerialNumber = deviceListDto.getPackageSerialNumber();
            RepairPackageDbo packageDbo = repairPackageService.get(packageSerialNumber);
            if (null == packageDbo) {
                throw new RestException(RestCode.FieldIsEmpty);
            }
            Integer packageId = packageDbo.getId();
            deviceDbo.setRepairPackageId(packageId);
            deviceDbo.setExpressNumber(packageDbo.getExpressNumber());
            packageDbo = null;

            deviceDbo.setCreateUserId(userId);

            repairDeviceService.add(deviceDbo);
        }
        else {
            repairDeviceService.update(deviceDbo);
        }

        return true;
    }

    @RequestMapping(path = "modify/status", method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE })
    public Object modifyStatus(@RequestBody RepairDeviceListDto deviceListDto) throws RestException, ServiceException {

        RepairDeviceDbo deviceDbo = deviceListDto.getDevice();

        if (null == deviceDbo) {
            throw new RestException(RestCode.FieldIsEmpty);
        }

        if (StringUtils.isEmpty(deviceListDto.getUserId())) {
            throw new RestException(RestCode.FieldIsEmpty, "userId");
        }

        if (StringUtils.isEmpty(deviceDbo.getSn())) {
            throw new RestException(RestCode.FieldIsEmpty, "sn");
        }

        repairDeviceService.modifyStatus(deviceDbo.getId(), deviceDbo.getSn(), deviceDbo.getStatus());

        return true;
    }

    @Autowired
    private IRepairDeviceDetectInvoiceService repairDeviceDetectInvoiceService;

    @Autowired
    private ISystemRepairStationService repairStationService;

    @Autowired
    private IRepairBackPackageService repairBackPackageService;

    @RequestMapping(path = "list", method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    public Object getList(@RequestParam("page") int pageNum, @RequestParam("limit") int limit,
            @RequestParam("params") String params) throws ServiceException, RestException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][getList][pageDto:" + pageNum + "," + limit + "," + params + "]");
        }

        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(limit);

        RepairDeviceDbo repairDeviceDbo = new RepairDeviceDbo();

        if (StringUtils.isNotBlank(params)) {
            JSONObject paramsJson = JSONObject.parseObject(params);

            String invoiceSerialNumber = paramsJson.getString("invoiceSerialNumber");
            if (StringUtils.isNotBlank(invoiceSerialNumber)) {
                RepairInvoiceDbo invoiceDbo = repairInvoiceService.get(invoiceSerialNumber);
                if (null != invoiceDbo) {
                    Integer refpairInvoiceId = invoiceDbo.getId();
                    repairDeviceDbo.setRepairInvoiceId(refpairInvoiceId);
                    invoiceDbo = null;
                }
            }

            String packageSerialNumber = paramsJson.getString("repairPackageSerialNumber");

            if (StringUtils.isEmpty(packageSerialNumber)) {
                packageSerialNumber = paramsJson.getString("packageSerialNumber");
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][][packageSerialNumber:" + packageSerialNumber + "]");
            }
            if (StringUtils.isNotBlank(packageSerialNumber)) {
                RepairPackageDbo packageDbo = repairPackageService.get(packageSerialNumber);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("packageDbo:" + packageDbo);
                }
                if (null != packageDbo) {
                    Integer repairPackageId = packageDbo.getId();
                    repairDeviceDbo.setRepairPackageId(repairPackageId);
                    packageDbo = null;
                }
            }

            String sns = paramsJson.getString("sn");
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][][sns:" + sns + "]");
            }
            if (StringUtils.isNotBlank(sns)) {
                repairDeviceDbo.setSn(sns);
            }

            String expressNumber = paramsJson.getString("expressNumber");
            if (StringUtils.isNotBlank(expressNumber)) {
                repairDeviceDbo.setExpressNumber(expressNumber);
            }

            String endCustomerName = paramsJson.getString("endCustomerName");
            if (StringUtils.isNotBlank(endCustomerName)) {
                repairDeviceDbo.setEndCustomerName(endCustomerName);
            }

            String repairStationUdid = paramsJson.getString("repairStationUdid");
            repairDeviceDbo.setRepairStationId(repairStationService.getIdNotNull(repairStationUdid));

            String allWaitBack = paramsJson.getString("allWaitBack");
            if ("1".equals(allWaitBack)) {
                // repairDeviceDbo.setNotStatus(RepairStatus.Backing.getCode());
                repairDeviceDbo.setStatus(RepairStatus.BackWait.getCode());
            }

            String bpSerialNumber = paramsJson.getString("repairBackPackageSerialNumber");
            if (StringUtils.isNotBlank(bpSerialNumber)) {
                RepairBackPackageDbo repairBackPackageDbo = repairBackPackageService.get(bpSerialNumber);
                if (null != repairBackPackageDbo) {
                    if ("1".equals(allWaitBack)) {
                        repairDeviceDbo.setNotRepairBackPackageId(repairBackPackageDbo.getId());
                    }
                    else {
                        repairDeviceDbo.setRepairBackPackageId(repairBackPackageDbo.getId());
                    }
                    repairBackPackageDbo = null;
                }
            }

            String lessThanStatus = paramsJson.getString("lessThanStatus");
            if (StringUtils.isNotBlank(lessThanStatus)) {
                int ltStatus = RepairStatus.getCode(lessThanStatus);
                repairDeviceDbo.setLessThanStatus(ltStatus);
            }

        }

        List<RepairDeviceDbo> list = repairDeviceService.list(pageInfo, repairDeviceDbo);

        if (CollectionUtils.isNotEmpty(list)) {

            repairDeviceDetectInvoiceService.setRepairDetectInvoices(list);

            repairPackageService.setRepairPackageDbo(list);
        }

        try {
            return RestResultFactory.getPageResult(list, pageInfo.getTotal());
        }
        finally {
            pageInfo = null;
        }

    }

    @RequestMapping(path = "find/detect/invoice/list", method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    public Object findDetectInvoiceList(@RequestParam("page") int pageNum, @RequestParam("limit") int limit,
            @RequestParam("params") String params) throws ServiceException, RestException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][getList][pageDto:" + pageNum + "," + limit + "," + params + "]");
        }

        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(limit);
        pageInfo.setTotal(0);

        Integer repairDeviceId = null;
        if (StringUtils.isNotEmpty(params)) {
            JSONObject paramJson = JSONObject.parseObject(params);
            String repairDeviceIdStr = paramJson.getString("repairDeviceId");
            if (StringUtils.isNotBlank(repairDeviceIdStr)) {
                repairDeviceId = Integer.parseInt(repairDeviceIdStr);
            }
        }

        List<RepairDeviceDetectInvoiceDbo> list = null;

        if (null != repairDeviceId) {
            list = repairDeviceDetectInvoiceService.getListByRepairDevice(repairDeviceId);
            if (CollectionUtils.isNotEmpty(list)) {
                pageInfo.setTotal(list.size());
            }
        }

        try {
            return RestResultFactory.getPageResult(list, pageInfo.getTotal());
        }
        finally {
            pageInfo = null;
        }

    }

    @RequestMapping(path = "find/list", method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    public Object findList(@RequestParam("page") int pageNum, @RequestParam("limit") int limit,
            @RequestParam("params") String params) throws ServiceException, RestException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][getList][pageDto:" + pageNum + "," + limit + "," + params + "]");
        }

        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(limit);

        RepairDeviceFo deviceFo = null;
        if (StringUtils.isNotBlank(params)) {
            JSONObject paramsJson = (JSONObject) JSONObject.parse(params);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][][" + paramsJson + "]");
            }
            if (null != paramsJson) {
                deviceFo = paramsJson.toJavaObject(RepairDeviceFo.class);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("[][][" + deviceFo + "]");
                }

                String status = deviceFo.getStatus();
                if (StringUtils.isNotBlank(status)) {
                    if (status.startsWith("lessThan")) {
                        deviceFo.setStatus(null);
                        deviceFo.setLessThanStatus(status.substring(8, status.length()));
                    }
                    else if (status.startsWith("moreThan")) {
                        deviceFo.setStatus(null);
                        deviceFo.setMoreThanStatus(status.substring(8, status.length()));
                    }
                }

                if (StringUtils.isNotBlank(deviceFo.getRepairStationUdid())) {
                    deviceFo.setRepairStationId(repairStationService.getIdNotNull(deviceFo.getRepairStationUdid()));
                }
                if (StringUtils.isNotBlank(deviceFo.getBackExpressNumber())) {
                    RepairBackPackageDbo backPackageDbo = repairBackPackageService
                            .getByExpressNumber(deviceFo.getBackExpressNumber());
                    if (null != backPackageDbo) {
                        deviceFo.setRepairBackPackageId(backPackageDbo.getId());
                        backPackageDbo = null;
                    }
                    else {
                        deviceFo.setRepairBackPackageId(-1);
                    }
                }

                if (StringUtils.isNotBlank(deviceFo.getPackageSerialNumber())) {
                    RepairPackageDbo packageDbo = repairPackageService.get(deviceFo.getPackageSerialNumber());
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("[][findList][" + packageDbo + "]");
                    }
                    if (null != packageDbo) {
                        deviceFo.setRepairPackageId(packageDbo.getId());
                        packageDbo = null;
                    }
                    else {
                        deviceFo.setRepairPackageId(-1);
                    }
                }
            }
        }

        List<RepairDeviceDbo> list = repairDeviceService.list(pageInfo, deviceFo);

        if (CollectionUtils.isNotEmpty(list)) {

            repairPackageService.setRepairPackageDbo(list);

            repairInvoiceService.setRepairInvoiceDbo(list);

            repairDeviceDetectInvoiceService.setRepairDetectInvoices(list);

            if (null != deviceFo.getShowQutationInvoice() && 1 == deviceFo.getShowQutationInvoice().intValue()) {
                quotationInvoiceService.setQuotationInvoice(list);
            }

            systemUserService.setUserOnlyame(list);

            repairBackPackageService.setBackPackage(list);
        }

        try {
            return RestResultFactory.getPageResult(list, pageInfo.getTotal());
        }
        finally {
            pageInfo = null;
        }
    }

    @RequestMapping(path = "get", method = RequestMethod.GET)
    public Object get(@RequestParam("id") Integer id, @RequestParam("sn") String sn)
            throws ServiceException, RestException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][get][id:" + id + ";sn:" + sn + "]");
        }

        if (null == id) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][][sn is null]");
            }
            throw new RestException(RestCode.FieldIsEmpty, "id");
        }

        if (StringUtils.isEmpty(sn)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][][sn is null]");
            }
            throw new RestException(RestCode.FieldIsEmpty, "sn");
        }

        RepairDeviceDbo deviceDbo = repairDeviceService.get(id);

        if (null != deviceDbo && deviceDbo.getSn().equals(sn)) {
            return deviceDbo;
        }

        return null;

    }

    @RequestMapping(path = "delete", method = RequestMethod.DELETE)
    public Object delete(@RequestParam("sn") String sn, @RequestParam("id") Integer id)
            throws ServiceException, RestException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][getUser][sn:" + sn + ";id:" + id + "]");
        }

        RepairDeviceDbo deviceDbo = repairDeviceService.get(id);
        if (null != deviceDbo && deviceDbo.getSn().equals(sn)) {
            repairDeviceService.delete(deviceDbo);
            deviceDbo = null;
            return true;
        }

        return false;
    }

    @RequestMapping(path = "commit/check", method = RequestMethod.POST)
    public Object commitCheck(@RequestBody RepairDeviceListDto repairDeviceListDto)
            throws ServiceException, RestException {

        List<Integer> ids = repairDeviceListDto.getRepairDeviceIds();

        if (CollectionUtils.isEmpty(ids)) {
            LOGGER.error("[][][RepairDeviceIds is empty]");
            throw new RestException(RestCode.FieldIsEmpty);
        }
        repairDeviceService.toWaitDetect(ids);
        return true;
    }

    @RequestMapping(path = "finish/confirm", method = RequestMethod.POST)
    public Object finishConfirm(@RequestBody RepairDeviceListDto repairDeviceListDto)
            throws ServiceException, RestException {

        List<Integer> ids = repairDeviceListDto.getRepairDeviceIds();

        if (CollectionUtils.isEmpty(ids)) {
            LOGGER.error("[][][RepairDeviceIds is empty]");
            throw new RestException(RestCode.FieldIsEmpty);
        }
        repairDeviceService.finishConfim(ids);
        return true;
    }

    @RequestMapping(path = "detect/review/finish", method = RequestMethod.POST)
    public Object detectReviewFinish(@RequestBody RepairDeviceListDto repairDeviceListDto)
            throws ServiceException, RestException {

        List<Integer> repairDeviceIds = repairDeviceListDto.getRepairDeviceIds();
        if (CollectionUtils.isEmpty(repairDeviceIds)) {
            LOGGER.error("[][][RepairDeviceIds is empty]");
            throw new RestException(RestCode.FieldIsEmpty);
        }

        String result = repairDeviceListDto.getResult();
        if ("pass".equals(result)) {
            repairDeviceService.detectReviewPass(null, repairDeviceIds);
        }
        else if ("fail".equals(result)) {
            repairDeviceService.detectReviewFail(null, repairDeviceIds);
        }
        else {
            throw new RestException(RestCode.FieldValueNotSupport);
        }

        return true;
    }

    @Autowired
    private IRepairDeviceQuotationInvoiceService quotationInvoiceService;

    @RequestMapping(path = "get/detect/invoice/detail", method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    public Object getDetectInvoiceDetail(@RequestParam("page") int pageNum, @RequestParam("limit") int limit,
            @RequestParam("params") String params) throws ServiceException, RestException {

        if (StringUtils.isEmpty(params)) {
            throw new RestException(RestCode.FieldIsEmpty);
        }

        JSONObject paramJson = JSONObject.parseObject(params);

        Integer repairDeviceId = paramJson.getInteger("repairDeviceId");
        if (null == repairDeviceId) {
            LOGGER.error("[][][RepairDeviceIds is empty]");
            throw new RestException(RestCode.FieldIsEmpty);
        }

        String sn = paramJson.getString("sn");
        if (StringUtils.isEmpty(sn)) {
            LOGGER.error("[][][sn is empty]");
            throw new RestException(RestCode.FieldIsEmpty);
        }

        Integer quptationType = paramJson.getInteger("quptationType");
        List<RepairDeviceDetectInvoiceDbo> invoiceDbos = null;

        if (null != quptationType && Device.QuotedPrice.getCode() == quptationType.intValue()) {
            invoiceDbos = repairDeviceDetectInvoiceService.getQuotationListByRepairDevice(repairDeviceId);
        }
        else {
            invoiceDbos = repairDeviceDetectInvoiceService.getListByRepairDevice(repairDeviceId);
        }

        if (CollectionUtils.isNotEmpty(invoiceDbos)) {
            quotationInvoiceService.setQuotationInvoice(repairDeviceId, invoiceDbos);
        }

        return RestResultFactory.getPageResult(invoiceDbos, null == invoiceDbos ? 0 : invoiceDbos.size());
    }

    @Autowired
    private ISystemUserService systemUserService;

    @RequestMapping(path = "save/quotation/invoice", method = RequestMethod.POST)
    public Object saveQuotationInvoice(@RequestBody RepairDeviceQuotationInvoiceDto quotationInvoiceDto)
            throws ServiceException, RestException {

        String uuid = quotationInvoiceDto.getUserId();
        if (StringUtils.isEmpty(uuid)) {
            throw new RestException(RestCode.FieldIsEmpty);
        }

        Integer userId = systemUserService.getId(uuid);
        if (null == userId) {
            throw new RestException(RestCode.FieldValueNotSupport);
        }

        Integer repairDeviceId = quotationInvoiceDto.getId();
        if (null == repairDeviceId) {
            throw new RestException(RestCode.FieldIsEmpty);
        }

        Integer payType = quotationInvoiceDto.getPayType();
        if (null == payType) {
            throw new RestException(RestCode.FieldIsEmpty);
        }

        String costTotal = quotationInvoiceDto.getCostTotal();
        if (StringUtils.isEmpty(costTotal)) {
            throw new RestException(RestCode.FieldIsEmpty);
        }

        String sn = quotationInvoiceDto.getSn();
        if (StringUtils.isEmpty(sn)) {
            throw new RestException(RestCode.FieldIsEmpty);
        }

        List<RepairDeviceQuotationInvoiceDbo> quotationInvoices = quotationInvoiceDto.getQuotationInvoices();
        if (CollectionUtils.isEmpty(quotationInvoices)) {
            throw new RestException(RestCode.FieldIsEmpty);
        }

        RepairDeviceDbo deviceDbo = new RepairDeviceDbo();
        deviceDbo.setId(repairDeviceId);
        deviceDbo.setPayType(payType);
        deviceDbo.setCostTotal(costTotal);
        deviceDbo.setCurrency(quotationInvoiceDto.getCurrency());

        int laborCosts = 0;
        String laborCostsStr = quotationInvoiceDto.getLaborCosts();
        if (StringUtils.isNotEmpty(laborCostsStr)) {
            laborCosts = Integer.parseInt(laborCostsStr);
        }
        deviceDbo.setLaborCosts(Integer.toString(laborCosts));
        deviceDbo.setUpdateTime(new Date(System.currentTimeMillis()));
        deviceDbo.setQuotationUserId(userId);

        repairDeviceService.update(deviceDbo);

        quotationInvoiceService.saveQuotationInvoice(userId, quotationInvoices);

        LOGGER.info("[][saveQuotationInvoice][userId:" + userId + ";repairDeviceId:" + repairDeviceId + "]");

        return true;
    }

    @RequestMapping(path = "save/quotation/invoice/confirm", method = RequestMethod.POST)
    public Object saveQuotationInvoiceConfirmStatus(@RequestBody RepairDeviceQuotationInvoiceDto quotationInvoiceDto)
            throws ServiceException, RestException {

        Integer id = quotationInvoiceDto.getId();
        if (null == id) {
            throw new RestException(RestCode.FieldIsEmpty, "id");
        }

        Integer detectInvoiceId = quotationInvoiceDto.getDetectInvoiceId();
        if (null == detectInvoiceId) {
            throw new RestException(RestCode.FieldIsEmpty, "detectInvoiceId");
        }

        Integer confirmStatus = quotationInvoiceDto.getConfirmStatus();
        if (null == confirmStatus) {
            throw new RestException(RestCode.FieldIsEmpty, "confirmStatus");
        }

        RepairDeviceQuotationInvoiceDbo quotationInvoiceDbo = new RepairDeviceQuotationInvoiceDbo();
        quotationInvoiceDbo.setId(id);
        quotationInvoiceDbo.setConfirmStatus(confirmStatus);
        quotationInvoiceDbo.setConfirmRemark(quotationInvoiceDto.getConfirmRemark());

        try {
            quotationInvoiceService.saveQuotationInvoiceConfirm(quotationInvoiceDbo);
        }
        finally {
            quotationInvoiceDbo = null;
        }

        return true;

    }

    // @RequestMapping(path = "finish/quotation", method = RequestMethod.POST)
    // public Object finishQuotationInvoice(@RequestBody RepairDeviceQuotationInvoiceDto
    // quotationInvoiceDto)
    // throws ServiceException, RestException {
    //
    // String uuid = quotationInvoiceDto.getUserId();
    // if (StringUtils.isEmpty(uuid)) {
    // throw new RestException(RestCode.FieldIsEmpty);
    // }
    //
    // Integer userId = systemUserService.getId(uuid);
    // if (null == userId) {
    // throw new RestException(RestCode.FieldValueNotSupport);
    // }
    //
    // Integer repairDeviceId = quotationInvoiceDto.getId();
    // if (null == repairDeviceId) {
    // throw new RestException(RestCode.FieldIsEmpty);
    // }
    //
    // String sn = quotationInvoiceDto.getSn();
    // if (StringUtils.isEmpty(sn)) {
    // throw new RestException(RestCode.FieldIsEmpty);
    // }
    //
    // repairDeviceService.finishQutation(userId, repairDeviceId, sn);
    //
    // return true;
    // }

    @RequestMapping(path = "confirm/quotation", method = RequestMethod.POST)
    public Object confirmQuotationInvoice(@RequestBody RepairDeviceQuotationInvoiceDto quotationInvoiceDto)
            throws ServiceException, RestException {

        String uuid = quotationInvoiceDto.getUserId();
        if (StringUtils.isEmpty(uuid)) {
            throw new RestException(RestCode.FieldIsEmpty);
        }

        Integer userId = systemUserService.getId(uuid);
        if (null == userId) {
            throw new RestException(RestCode.FieldValueNotSupport);
        }

        Integer repairDeviceId = quotationInvoiceDto.getId();
        if (null == repairDeviceId) {
            throw new RestException(RestCode.FieldIsEmpty);
        }

        String sn = quotationInvoiceDto.getSn();
        if (StringUtils.isEmpty(sn)) {
            throw new RestException(RestCode.FieldIsEmpty);
        }

        repairDeviceService.finishQutation(userId, repairDeviceId, sn);

        repairDeviceService.confirmQutation(userId, repairDeviceId, sn);

        return true;
    }

    @RequestMapping(path = "bacth/confirm/quotation", method = RequestMethod.POST)
    public Object batchConfirmQuotationInvoice(@RequestBody RepairDeviceQuotationInvoiceDto quotationInvoiceDto)
            throws ServiceException, RestException {

        String uuid = quotationInvoiceDto.getUserId();
        if (StringUtils.isEmpty(uuid)) {
            throw new RestException(RestCode.FieldIsEmpty);
        }

        Integer userId = systemUserService.getId(uuid);
        if (null == userId) {
            throw new RestException(RestCode.FieldValueNotSupport);
        }

        List<RepairDeviceDbo> deviceDbos = quotationInvoiceDto.getDevices();
        if (CollectionUtils.isEmpty(deviceDbos)) {
            throw new RestException(RestCode.FieldIsEmpty);
        }

        List<Integer> deviceIds = new ArrayList<>(deviceDbos.size());
        for (RepairDeviceDbo repairDeviceDbo : deviceDbos) {
            Integer repairDeviceId = repairDeviceDbo.getId();
            String sn = repairDeviceDbo.getSn();
            repairDeviceService.finishQutation(userId, repairDeviceId, sn);
            deviceIds.add(repairDeviceId);
        }

        repairDeviceService.confirmQutation(deviceIds);

        return true;
    }

    @RequestMapping(path = "refuse/quotation", method = RequestMethod.POST)
    public Object refuseQuotationInvoice(@RequestBody RepairDeviceQuotationInvoiceDto quotationInvoiceDto)
            throws ServiceException, RestException {

        String uuid = quotationInvoiceDto.getUserId();
        if (StringUtils.isEmpty(uuid)) {
            throw new RestException(RestCode.FieldIsEmpty);
        }

        Integer userId = systemUserService.getId(uuid);
        if (null == userId) {
            throw new RestException(RestCode.FieldValueNotSupport);
        }

        Integer repairDeviceId = quotationInvoiceDto.getId();
        if (null == repairDeviceId) {
            throw new RestException(RestCode.FieldIsEmpty);
        }

        String sn = quotationInvoiceDto.getSn();
        if (StringUtils.isEmpty(sn)) {
            throw new RestException(RestCode.FieldIsEmpty);
        }

        repairDeviceService.refuseQutation(userId, repairDeviceId, sn);

        return true;
    }

    @RequestMapping(path = "batch/refuse/quotation", method = RequestMethod.POST)
    public Object batchRefuseQuotationInvoice(@RequestBody RepairDeviceQuotationInvoiceDto quotationInvoiceDto)
            throws ServiceException, RestException {

        String uuid = quotationInvoiceDto.getUserId();
        if (StringUtils.isEmpty(uuid)) {
            throw new RestException(RestCode.FieldIsEmpty);
        }

        Integer userId = systemUserService.getId(uuid);
        if (null == userId) {
            throw new RestException(RestCode.FieldValueNotSupport);
        }

        List<RepairDeviceDbo> repairDeviceDbos = quotationInvoiceDto.getDevices();
        if (CollectionUtils.isEmpty(repairDeviceDbos)) {
            throw new RestException(RestCode.FieldIsEmpty);
        }

        List<Integer> deviceIds = new ArrayList<>(repairDeviceDbos.size());
        for (RepairDeviceDbo repairDeviceDbo : repairDeviceDbos) {
            Integer repairDeviceId = repairDeviceDbo.getId();
            String sn = repairDeviceDbo.getSn();
            repairDeviceService.finishQutation(userId, repairDeviceId, sn);
            deviceIds.add(repairDeviceId);
        }

        repairDeviceService.refuseQutation(deviceIds);

        return true;
    }

    @RequestMapping(path = "finish/pay", method = RequestMethod.POST)
    public Object finishPay(@RequestBody RepairDeviceQuotationInvoiceDto quotationInvoiceDto)
            throws ServiceException, RestException {

        String uuid = quotationInvoiceDto.getUserId();
        if (StringUtils.isEmpty(uuid)) {
            throw new RestException(RestCode.FieldIsEmpty);
        }

        Integer userId = systemUserService.getId(uuid);
        if (null == userId) {
            throw new RestException(RestCode.FieldValueNotSupport);
        }

        Integer repairDeviceId = quotationInvoiceDto.getId();
        if (null == repairDeviceId) {
            throw new RestException(RestCode.FieldIsEmpty);
        }

        String sn = quotationInvoiceDto.getSn();
        if (StringUtils.isEmpty(sn)) {
            throw new RestException(RestCode.FieldIsEmpty);
        }

        String payDescription = quotationInvoiceDto.getPayDescription();

        repairDeviceService.finishPay(userId, repairDeviceId, sn, payDescription);

        return true;
    }

    @RequestMapping(path = "batch/finish/pay", method = RequestMethod.POST)
    public Object batchFinishPay(@RequestBody RepairDeviceQuotationInvoiceDto quotationInvoiceDto)
            throws ServiceException, RestException {

        String uuid = quotationInvoiceDto.getUserId();
        if (StringUtils.isEmpty(uuid)) {
            throw new RestException(RestCode.FieldIsEmpty);
        }

        Integer userId = systemUserService.getId(uuid);
        if (null == userId) {
            throw new RestException(RestCode.FieldValueNotSupport);
        }

        Map<String, String[]> devices = quotationInvoiceDto.getBatchDevices();
        if (MapUtils.isEmpty(devices)) {
            throw new RestException(RestCode.FieldIsEmpty, "devices");
        }

        Set<Entry<String, String[]>> deviceEntries = devices.entrySet();
        for (Entry<String, String[]> entry : deviceEntries) {
            String[] deviceInfo = entry.getValue();
            Integer repairDeviceId = Integer.parseInt(deviceInfo[0]);
            String sn = deviceInfo[1];
            String desc = deviceInfo[2];
            repairDeviceService.finishPay(userId, repairDeviceId, sn, desc);
        }

        return true;
    }

    @RequestMapping(path = "add/back/package", method = RequestMethod.POST)
    public Object addToBackPackage(@RequestBody RepairDeviceListDto repairDeviceListDto)
            throws ServiceException, RestException {

        List<Integer> repairDeviceIds = repairDeviceListDto.getIds();
        if (CollectionUtils.isEmpty(repairDeviceIds)) {
            throw new RestException(RestCode.FieldIsEmpty, "ids is empty");
        }

        String backPackageSerialNumber = repairDeviceListDto.getBackPackageSerialNumber();
        if (StringUtils.isEmpty(backPackageSerialNumber)) {
            throw new RestException(RestCode.FieldIsEmpty, "backPackageSerialNumber is empty");
        }

        Integer backPackageId = repairBackPackageService.getIdNotNull(backPackageSerialNumber);

        repairDeviceService.addToBackPackage(backPackageId, repairDeviceIds);

        return true;
    }

    @RequestMapping(path = "remove/back/package", method = RequestMethod.POST)
    public Object removeFromBackPackage(@RequestBody RepairDeviceListDto repairDeviceListDto)
            throws ServiceException, RestException {

        List<Integer> repairDeviceIds = repairDeviceListDto.getIds();
        if (CollectionUtils.isEmpty(repairDeviceIds)) {
            throw new RestException(RestCode.FieldIsEmpty, "ids is empty");
        }

        String backPackageSerialNumber = repairDeviceListDto.getBackPackageSerialNumber();
        if (StringUtils.isEmpty(backPackageSerialNumber)) {
            throw new RestException(RestCode.FieldIsEmpty, "backPackageSerialNumber is empty");
        }

        Integer backPackageId = repairBackPackageService.getIdNotNull(backPackageSerialNumber);

        repairDeviceService.removeFromBackPackage(backPackageId, repairDeviceIds);

        return true;
    }

}
