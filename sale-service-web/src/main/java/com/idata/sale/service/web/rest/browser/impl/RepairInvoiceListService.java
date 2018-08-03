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

import com.alibaba.fastjson.JSONObject;
import com.idata.sale.service.web.base.dao.PageInfo;
import com.idata.sale.service.web.base.dao.dbo.RepairInvoiceDbo;
import com.idata.sale.service.web.base.service.IRepairInvoiceService;
import com.idata.sale.service.web.base.service.ISystemRepairStationService;
import com.idata.sale.service.web.base.service.ServiceException;
import com.idata.sale.service.web.rest.RestCode;
import com.idata.sale.service.web.rest.RestException;
import com.idata.sale.service.web.rest.RestResultFactory;
import com.idata.sale.service.web.rest.browser.dto.RepairInvoiceListDto;

@RestController
@RequestMapping(path = "/api/browser/repair/invoice", produces = { MediaType.APPLICATION_JSON_VALUE })
public class RepairInvoiceListService {

    private final static Logger LOGGER = LoggerFactory.getLogger(RepairInvoiceListService.class);

    public RepairInvoiceListService() {

    }

    @Autowired
    private IRepairInvoiceService repairInvoiceService;

    @RequestMapping(path = "save", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE })
    public Object save(@RequestBody RepairInvoiceListDto invoiceListDto) throws RestException, ServiceException {

        RepairInvoiceDbo invoiceDbo = invoiceListDto.getInvoice();
        if (null == invoiceDbo) {
            throw new RestException(RestCode.FieldIsEmpty);
        }

        if (StringUtils.isEmpty(invoiceDbo.getContacts())) {
            throw new RestException(RestCode.FieldIsEmpty, "contacts");
        }

        if (StringUtils.isEmpty(invoiceDbo.getContactAddress())) {
            throw new RestException(RestCode.FieldIsEmpty, "contactAddress");
        }

        if (StringUtils.isEmpty(invoiceDbo.getContactNumber())) {
            throw new RestException(RestCode.FieldIsEmpty, "contactNumber");
        }

        String serialNumber = invoiceDbo.getSerialNumber();

        if (StringUtils.isNotBlank(serialNumber)) {
            repairInvoiceService.update(invoiceDbo);
        }
        else {

            if (StringUtils.isEmpty(invoiceDbo.getRepairStationUdid())) {
                throw new RestException(RestCode.FieldIsEmpty, "repairStationUdid");
            }

            invoiceDbo.setRepairStationId(repairStationService.getIdNotNull(invoiceDbo.getRepairStationUdid()));

            repairInvoiceService.add(invoiceDbo);
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

        boolean isPage = true;
        String createTimeStr = null;

        RepairInvoiceDbo repairInvoiceDbo = null;

        if (StringUtils.isNotBlank(params)) {
            JSONObject paramJson = JSONObject.parseObject(params);
            repairInvoiceDbo = new RepairInvoiceDbo();
            repairInvoiceDbo.setContacts(paramJson.getString("contacts"));
            repairInvoiceDbo.setSerialNumber(paramJson.getString("serialNumber"));
            createTimeStr = paramJson.getString("createTime");

            String repairStationUdid = paramJson.getString("repairStationUdid");
            Integer repairStationId = repairStationService.getIdNotNull(repairStationUdid);
            repairInvoiceDbo.setRepairStationId(repairStationId);

            if ("0".equals(paramJson.get("isPage"))) {
                isPage = false;
            }
        }

        PageInfo pageInfo = null;
        if (isPage) {
            pageInfo = new PageInfo();
            pageInfo.setPageNum(pageNum);
            pageInfo.setPageSize(limit);
        }

        List<RepairInvoiceDbo> list = repairInvoiceService.list(pageInfo, repairInvoiceDbo, createTimeStr);

        try {
            return RestResultFactory.getPageResult(list,
                    isPage ? pageInfo.getTotal() : (null != list ? list.size() : 0));
        }
        finally {
            pageInfo = null;
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

        return repairInvoiceService.get(serialNumber);

    }

    @RequestMapping(path = "delete", method = RequestMethod.DELETE)
    public Object delete(@RequestParam("serialNumber") String serialNumber, @RequestParam("id") Integer id)
            throws ServiceException, RestException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][getUser][serialNumber:" + serialNumber + ";id:" + id + "]");
        }

        RepairInvoiceDbo repairInvoiceDbo = new RepairInvoiceDbo();
        repairInvoiceDbo.setSerialNumber(serialNumber);
        repairInvoiceDbo.setId(id);

        try {
            repairInvoiceService.delete(repairInvoiceDbo);
        }
        finally {
            repairInvoiceDbo = null;
        }

        return true;

    }

}
