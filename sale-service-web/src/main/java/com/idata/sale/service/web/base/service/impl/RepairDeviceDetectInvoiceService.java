package com.idata.sale.service.web.base.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idata.sale.service.web.base.dao.BaseDaoException;
import com.idata.sale.service.web.base.dao.constant.Device;
import com.idata.sale.service.web.base.dao.constant.RepairStatus;
import com.idata.sale.service.web.base.dao.dbo.RepairDeviceDbo;
import com.idata.sale.service.web.base.dao.dbo.RepairDeviceDetectInvoiceDbo;
import com.idata.sale.service.web.base.dao.impl.RepairDeviceDao;
import com.idata.sale.service.web.base.dao.impl.RepairDeviceDetectInvoiceDao;
import com.idata.sale.service.web.base.service.IRepairDeviceDetectInvoiceService;
import com.idata.sale.service.web.base.service.ServiceException;
import com.idata.sale.service.web.base.service.constant.ServiceCode;

@Service
public class RepairDeviceDetectInvoiceService implements IRepairDeviceDetectInvoiceService {

    private final static Logger LOGGER = LoggerFactory.getLogger(RepairDeviceDetectInvoiceService.class);

    public RepairDeviceDetectInvoiceService() {
    }

    @Autowired
    private RepairDeviceDetectInvoiceDao repairDeviceDetectInvoiceDao;

    @Autowired
    private RepairDeviceDao repairDeviceDao;

    @Override
    public void save(List<RepairDeviceDetectInvoiceDbo> invoiceDbos) throws ServiceException {

        Integer repairDeviceId = invoiceDbos.get(0).getRepairDeviceId();

        RepairDeviceDbo dbRepairDeviceDbo = repairDeviceDao.findById(repairDeviceId);
        if (null == dbRepairDeviceDbo) {
            throw new ServiceException(ServiceCode.system_param_error, "repairDeviceId is error");
        }

        try {
            if (dbRepairDeviceDbo.getStatus().intValue() >= RepairStatus.CheckFinish.getCode()) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("[][][status:" + dbRepairDeviceDbo.getStatus() + "]");
                }
                throw new ServiceException(ServiceCode.system_param_error, "repairDevice status is error");
            }
        }
        finally {
            dbRepairDeviceDbo = null;
        }

        List<RepairDeviceDetectInvoiceDbo> addDbos = null;
        List<RepairDeviceDetectInvoiceDbo> updateDbos = null;

        Date now = new Date(System.currentTimeMillis());

        int size = invoiceDbos.size();
        for (int i = 0; i < size; i++) {
            RepairDeviceDetectInvoiceDbo detectInvoiceDbo = invoiceDbos.get(i);
            detectInvoiceDbo.setUpdateTime(now);

            if (null == detectInvoiceDbo.getId()) {
                if (null == addDbos) {
                    addDbos = new ArrayList<>();
                }
                detectInvoiceDbo.setCreateTime(now);
                addDbos.add(detectInvoiceDbo);
            }
            else {
                if (null == updateDbos) {
                    updateDbos = new ArrayList<>();
                }
                detectInvoiceDbo.setDeviceId(null);
                detectInvoiceDbo.setRepairDeviceId(null);
                detectInvoiceDbo.setSn(null);
                detectInvoiceDbo.setCreateUserId(null);
                updateDbos.add(detectInvoiceDbo);
            }
        }

        if (CollectionUtils.isNotEmpty(addDbos)) {
            try {
                repairDeviceDetectInvoiceDao.batchSave(addDbos);
            }
            catch (BaseDaoException e) {
                LOGGER.error("[repairDeviceDetectInvoice][batchUpdate][failed]", e);
            }
            finally {
                addDbos.clear();
                addDbos = null;
            }
        }

        if (CollectionUtils.isNotEmpty(updateDbos)) {
            try {
                repairDeviceDetectInvoiceDao.batchUpdate(updateDbos);
            }
            catch (BaseDaoException e) {
                LOGGER.error("[repairDeviceDetectInvoice][batchUpdate][failed]", e);
            }
            finally {
                updateDbos.clear();
                updateDbos = null;
            }
        }
    }

    @Override
    public RepairDeviceDetectInvoiceDbo get(Integer id) {
        return repairDeviceDetectInvoiceDao.findById(id);
    }

    @Override
    public List<RepairDeviceDetectInvoiceDbo> getListByRepairDevice(Integer repairDeviceId) {

        return repairDeviceDetectInvoiceDao.getListByRepairDevice(repairDeviceId);
    }

    @Override
    public List<RepairDeviceDetectInvoiceDbo> getQuotationListByRepairDevice(Integer repairDeviceId) {
        List<RepairDeviceDetectInvoiceDbo> detectInvoiceDbos = repairDeviceDetectInvoiceDao
                .getListByRepairDevice(repairDeviceId);
        List<RepairDeviceDetectInvoiceDbo> rmList = new ArrayList<>();
        for (RepairDeviceDetectInvoiceDbo repairDeviceDetectInvoiceDbo : detectInvoiceDbos) {
            Integer quotedPrice = repairDeviceDetectInvoiceDbo.getQuotedPrice();
            if (null == quotedPrice || !(Device.QuotedPrice.getCode() == quotedPrice.intValue())) {
                rmList.add(repairDeviceDetectInvoiceDbo);
            }
        }

        detectInvoiceDbos.removeAll(rmList);

        rmList.clear();
        rmList = null;

        return detectInvoiceDbos;

    }

    @Override
    public void delete(List<Integer> invoiceIds) {
        repairDeviceDetectInvoiceDao.delete(invoiceIds);
    }

    @Override
    public void setRepairDetectInvoices(List<RepairDeviceDbo> deviceDbos) {

        Set<Integer> repairDeviceIds = new HashSet<>();
        for (RepairDeviceDbo repairDeviceDbo : deviceDbos) {
            repairDeviceIds.add(repairDeviceDbo.getId());
        }

        List<RepairDeviceDetectInvoiceDbo> invoiceDbos = repairDeviceDetectInvoiceDao
                .getListByRepairDevice(repairDeviceIds);

        repairDeviceIds.clear();
        repairDeviceIds = null;

        if (CollectionUtils.isEmpty(invoiceDbos)) {
            return;
        }

        Map<Integer, List<RepairDeviceDetectInvoiceDbo>> idListMap = new HashMap<>();
        for (RepairDeviceDetectInvoiceDbo repairDeviceDetectInvoiceDbo : invoiceDbos) {

            Integer repairDeviceId = repairDeviceDetectInvoiceDbo.getRepairDeviceId();
            List<RepairDeviceDetectInvoiceDbo> repairDeviceDetectInvoiceDbos = idListMap.get(repairDeviceId);
            if (null == repairDeviceDetectInvoiceDbos) {
                repairDeviceDetectInvoiceDbos = new ArrayList<>();
                idListMap.put(repairDeviceId, repairDeviceDetectInvoiceDbos);
            }
            repairDeviceDetectInvoiceDbos.add(repairDeviceDetectInvoiceDbo);
        }
        invoiceDbos.clear();
        invoiceDbos = null;

        for (RepairDeviceDbo repairDeviceDbo : deviceDbos) {
            repairDeviceDbo.setDetectInvoices(idListMap.get(repairDeviceDbo.getId()));
        }

        idListMap.clear();
        idListMap = null;
    }

}
