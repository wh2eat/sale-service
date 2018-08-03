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

import com.alibaba.fastjson.JSONObject;
import com.idata.sale.service.web.base.dao.PageInfo;
import com.idata.sale.service.web.base.dao.dbo.RepairInvoiceDbo;
import com.idata.sale.service.web.base.dao.dbo.RepairPackageDbo;
import com.idata.sale.service.web.base.service.IRepairInvoiceService;
import com.idata.sale.service.web.base.service.IRepairPackageService;
import com.idata.sale.service.web.base.service.ISystemRepairStationService;
import com.idata.sale.service.web.base.service.ServiceException;
import com.idata.sale.service.web.rest.RestCode;
import com.idata.sale.service.web.rest.RestException;
import com.idata.sale.service.web.rest.RestResultFactory;
import com.idata.sale.service.web.rest.browser.dto.RepairPackageListDto;

@RestController
@RequestMapping(path = "/api/browser/repair/package", produces = { MediaType.APPLICATION_JSON_VALUE })
public class RepairPackageListService {

    private final static Logger LOGGER = LoggerFactory.getLogger(RepairPackageListService.class);

    public RepairPackageListService() {

    }

    @Autowired
    private IRepairPackageService repairPackageService;

    @Autowired
    private IRepairInvoiceService repairInvoiceService;

    @RequestMapping(path = "save", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE })
    public Object save(@RequestBody RepairPackageListDto packageListDto) throws RestException, ServiceException {

        RepairPackageDbo packageDbo = packageListDto.getPkg();
        if (null == packageDbo) {
            throw new RestException(RestCode.FieldIsEmpty);
        }

        if (StringUtils.isEmpty(packageDbo.getContacts())) {
            throw new RestException(RestCode.FieldIsEmpty, "contacts");
        }

        String serialNumber = packageDbo.getSerialNumber();

        if (StringUtils.isNotBlank(serialNumber)) {
            repairPackageService.update(packageDbo);
        }
        else {
            String invoiceSerialNumber = packageListDto.getInvoiceSerialNumber();
            if (StringUtils.isEmpty(invoiceSerialNumber)) {
                throw new RestException(RestCode.FieldIsEmpty);
            }

            RepairInvoiceDbo repairInvoiceDbo = repairInvoiceService.get(invoiceSerialNumber);

            if (null == repairInvoiceDbo) {
                throw new RestException(RestCode.FieldValueNotSupport);
            }
            packageDbo.setRepairInvoiceId(repairInvoiceDbo.getId());
            packageDbo.setRepairStationId(repairInvoiceDbo.getRepairStationId());
            repairPackageService.add(packageDbo);
        }

        return true;
    }

    @Autowired
    private ISystemRepairStationService repairStationService;

    @RequestMapping(path = "list", method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    public Object getList(@RequestParam("page") int pageNum, @RequestParam("limit") int limit,
            @RequestParam("params") String params) throws ServiceException, RestException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][getList][pageDto:" + pageNum + "," + limit + "," + params + "]");
        }

        if (StringUtils.isEmpty(params)) {
            throw new RestException(RestCode.FieldIsEmpty);
        }

        RepairPackageDbo filterDbo = new RepairPackageDbo();

        JSONObject paramsJson = JSONObject.parseObject(params);
        String invoice = paramsJson.getString("invoice");

        if (StringUtils.isNotBlank(invoice)) {
            Integer invoiceId = repairInvoiceService.getId(invoice);
            if (null != invoiceId) {
                filterDbo.setRepairInvoiceId(invoiceId);
            }
        }

        String expressName = paramsJson.getString("expressName");
        if (StringUtils.isNotBlank(expressName)) {
            filterDbo.setExpressName(expressName);
        }

        String expressNumber = paramsJson.getString("expressNumber");
        if (StringUtils.isNotBlank(expressNumber)) {
            filterDbo.setExpressNumber(expressNumber);
        }

        String contacts = paramsJson.getString("contacts");
        if (StringUtils.isNotBlank(contacts)) {
            filterDbo.setContacts(contacts);
        }

        String contactNumber = paramsJson.getString("contactNumber");
        if (StringUtils.isNotBlank(contactNumber)) {
            filterDbo.setContactNumber(contactNumber);
        }

        String repairStationUdid = paramsJson.getString("repairStationUdid");
        if (StringUtils.isNotBlank(repairStationUdid)) {
            filterDbo.setRepairStationId(repairStationService.getIdNotNull(repairStationUdid));
        }

        PageInfo pageInfo = null;
        String isPage = paramsJson.getString("isPackage");
        if (!"0".equals(isPage)) {
            pageInfo = new PageInfo();
            pageInfo.setPageNum(pageNum);
            pageInfo.setPageSize(limit);
        }

        List<RepairPackageDbo> list = repairPackageService.list(pageInfo, filterDbo);

        if (CollectionUtils.isNotEmpty(list)) {
            repairInvoiceService.setRepairInvoiceDbo(list);
        }

        try {
            return RestResultFactory.getPageResult(list, null == pageInfo ? 0 : pageInfo.getTotal());
        }
        finally {
            pageInfo = null;
            filterDbo = null;
        }

    }

    @RequestMapping(path = "get", method = RequestMethod.GET)
    public Object get(@RequestParam("serialNumber") String serialNumber) throws ServiceException, RestException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][get][" + serialNumber + "]");
        }

        if (StringUtils.isEmpty(serialNumber)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][][serialNumber is null]");
            }
            throw new RestException(RestCode.FieldIsEmpty, "serialNumber");
        }

        RepairPackageDbo packageDbo = repairPackageService.get(serialNumber);

        if (null != packageDbo) {
            repairInvoiceService.setRepairInvoiceDbo(packageDbo);
        }

        return packageDbo;
    }

    @RequestMapping(path = "delete", method = RequestMethod.DELETE)
    public Object delete(@RequestParam("serialNumber") String serialNumber, @RequestParam("id") Integer id)
            throws ServiceException, RestException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][getUser][serialNumber:" + serialNumber + ";id:" + id + "]");
        }

        if (StringUtils.isEmpty(serialNumber)) {
            throw new RestException(RestCode.FieldIsEmpty);
        }

        if (null == id || id < 0) {
            throw new RestException(RestCode.FieldIsEmpty);
        }

        RepairPackageDbo dbRepairPackageDbo = repairPackageService.get(serialNumber);
        if (null == dbRepairPackageDbo) {
            throw new RestException(RestCode.ObjectNotFound);
        }

        if (!dbRepairPackageDbo.getId().equals(id)) {
            throw new RestException(RestCode.FieldValueNotSupport);
        }

        try {
            repairPackageService.delete(dbRepairPackageDbo);
        }
        finally {
            dbRepairPackageDbo = null;
        }

        return true;

    }

}
