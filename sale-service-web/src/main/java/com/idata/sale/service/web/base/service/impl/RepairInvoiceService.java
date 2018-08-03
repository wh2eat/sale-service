package com.idata.sale.service.web.base.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.idata.sale.service.web.base.dao.BaseDaoException;
import com.idata.sale.service.web.base.dao.PageInfo;
import com.idata.sale.service.web.base.dao.constant.RepairStatus;
import com.idata.sale.service.web.base.dao.dbo.RepairInvoiceDbo;
import com.idata.sale.service.web.base.dao.dbo.RepairInvoiceDboSet;
import com.idata.sale.service.web.base.dao.impl.RepairInvoiceDao;
import com.idata.sale.service.web.base.service.IRepairInvoiceService;
import com.idata.sale.service.web.base.service.ISystemCustomerService;
import com.idata.sale.service.web.base.service.SerialNumberUtil;
import com.idata.sale.service.web.base.service.ServiceException;
import com.idata.sale.service.web.base.service.constant.ServiceCode;
import com.idata.sale.service.web.base.service.event.RepairInvoiceStatusEvent;

@Service
public class RepairInvoiceService implements IRepairInvoiceService {

    private final static Logger LOGGER = LoggerFactory.getLogger(RepairInvoiceService.class);

    public RepairInvoiceService() {
    }

    @Autowired
    private RepairInvoiceDao repairInvoiceDao;

    @Autowired
    private ISystemCustomerService customerService;

    @Override
    public void add(RepairInvoiceDbo repairInvoiceDbo) throws ServiceException {

        Date now = new Date(System.currentTimeMillis());

        repairInvoiceDbo.setCreateTime(now);
        repairInvoiceDbo.setUpdateTime(now);
        repairInvoiceDbo.setRepairDate(now);
        repairInvoiceDbo.setStatus(RepairStatus.Received.getCode());

        repairInvoiceDbo.setSerialNumber(SerialNumberUtil.generateRepairInvoiceSerialNumber());

        try {
            long id = repairInvoiceDao.save(repairInvoiceDbo);
            customerService.addAsync(repairInvoiceDbo);
            LOGGER.info("[][add][success,id:" + id + "]");
        }
        catch (BaseDaoException e) {
            LOGGER.error("", e);
            throw new ServiceException(ServiceCode.system_db_exception, e.getMessage());
        }

    }

    @Override
    public void update(RepairInvoiceDbo repairInvoiceDbo) throws ServiceException {

        if (StringUtils.isEmpty(repairInvoiceDbo.getSerialNumber())) {
            throw new ServiceException(ServiceCode.repair_invoice_field_is_empty, "serialNumber");
        }

        if (null == repairInvoiceDbo.getId()) {
            throw new ServiceException(ServiceCode.repair_invoice_field_is_empty, "id");
        }

        validate(repairInvoiceDbo);

        repairInvoiceDbo.setUpdateTime(new Date(System.currentTimeMillis()));

        try {
            repairInvoiceDao.update(repairInvoiceDbo);
        }
        catch (BaseDaoException e) {
            LOGGER.error("[][update][repairInvoiceDbo update failed]", e);
            throw new ServiceException(ServiceCode.system_db_exception, e.getMessage());
        }

    }

    private void validate(RepairInvoiceDbo repairInvoiceDbo) throws ServiceException {

        RepairInvoiceDbo dbRepairInvoiceDbo = repairInvoiceDao.get(repairInvoiceDbo.getSerialNumber());
        if (null == dbRepairInvoiceDbo) {
            throw new ServiceException(ServiceCode.repair_invoice_not_found, "");
        }

        if (!dbRepairInvoiceDbo.getId().equals(repairInvoiceDbo.getId())) {
            throw new ServiceException(ServiceCode.repair_invoice_param_error, "");
        }
    }

    @Override
    public void delete(RepairInvoiceDbo repairInvoiceDbo) throws ServiceException {

        validate(repairInvoiceDbo);

        try {
            repairInvoiceDao.delete(repairInvoiceDbo);
        }
        catch (BaseDaoException e) {
            LOGGER.error("[][delete][" + repairInvoiceDbo.toString() + "]", e);
            throw new ServiceException(ServiceCode.system_db_exception, e.getMessage());
        }
    }

    @Override
    public List<RepairInvoiceDbo> list(PageInfo pageInfo, RepairInvoiceDbo filterDbo, String createTime)
            throws ServiceException {
        return repairInvoiceDao.findPageList(pageInfo, filterDbo, createTime);
    }

    @Override
    public void setRepairInvoiceDbo(RepairInvoiceDboSet dboSet) {
        dboSet.setRepairInvoiceDbo(repairInvoiceDao.findById(dboSet.getRepairInvoiceId()));
    }

    @Override
    public void setRepairInvoiceDbo(List dboSets) {

        if (CollectionUtils.isEmpty(dboSets)) {
            return;
        }

        Set<Integer> invoiceIds = new HashSet<>();
        for (Object obj : dboSets) {
            RepairInvoiceDboSet dboSet = (RepairInvoiceDboSet) obj;
            invoiceIds.add(dboSet.getRepairInvoiceId());
        }
        List<RepairInvoiceDbo> invoiceDbos = repairInvoiceDao.getAll(invoiceIds);
        Map<Integer, RepairInvoiceDbo> invoiceIdDboMap = new HashMap<>();

        for (RepairInvoiceDbo repairInvoiceDbo : invoiceDbos) {
            invoiceIdDboMap.put(repairInvoiceDbo.getId(), repairInvoiceDbo);
        }

        for (Object obj : dboSets) {
            RepairInvoiceDboSet dboSet = (RepairInvoiceDboSet) obj;
            dboSet.setRepairInvoiceDbo(invoiceIdDboMap.get(dboSet.getRepairInvoiceId()));
        }

        invoiceIds.clear();
        invoiceIds = null;

        invoiceIdDboMap.clear();
        invoiceIdDboMap = null;

        invoiceDbos.clear();
        invoiceDbos = null;
    }

    @Override
    public RepairInvoiceDbo get(String serialNumber) {

        return repairInvoiceDao.get(serialNumber);
    }

    @Override
    public Integer getId(String serialNumber) {

        RepairInvoiceDbo repairInvoiceDbo = get(serialNumber);
        if (null != repairInvoiceDbo) {
            try {
                return repairInvoiceDbo.getId();
            }
            finally {
                repairInvoiceDbo = null;
            }

        }

        return null;
    }

    public static void main(String[] args) {

        for (int i = 0; i < 1000000; i++) {
            // System.out.println(generateSerialNumber());
        }

    }

    @EventListener
    public void dealRepairInvocieStatusEvent(RepairInvoiceStatusEvent repairInvoiceStatusEvent) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][dealRepairInvocieStatusEvent][" + repairInvoiceStatusEvent + "]");
        }

        RepairInvoiceDbo repairInvoiceDbo = new RepairInvoiceDbo();
        repairInvoiceDbo.setId(repairInvoiceStatusEvent.getRepairInvoiceId());
        repairInvoiceDbo.setStatus(repairInvoiceStatusEvent.getStatus());
        repairInvoiceDbo.setUpdateTime(new Date(System.currentTimeMillis()));

        try {
            repairInvoiceDao.update(repairInvoiceDbo);
        }
        catch (BaseDaoException e) {
            LOGGER.error("", e);
        }
        finally {
            repairInvoiceDbo = null;
            repairInvoiceStatusEvent = null;
        }

    }

}
