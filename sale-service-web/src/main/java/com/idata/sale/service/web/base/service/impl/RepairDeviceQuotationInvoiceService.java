package com.idata.sale.service.web.base.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idata.sale.service.web.base.dao.BaseDaoException;
import com.idata.sale.service.web.base.dao.dbo.RepairDeviceDbo;
import com.idata.sale.service.web.base.dao.dbo.RepairDeviceDetectInvoiceDbo;
import com.idata.sale.service.web.base.dao.dbo.RepairDeviceQuotationInvoiceDbo;
import com.idata.sale.service.web.base.dao.impl.RepairDeviceQuotationInvoiceDao;
import com.idata.sale.service.web.base.service.IRepairDeviceQuotationInvoiceService;
import com.idata.sale.service.web.base.service.ServiceException;
import com.idata.sale.service.web.base.service.constant.ServiceCode;

@Service
public class RepairDeviceQuotationInvoiceService implements IRepairDeviceQuotationInvoiceService {

    private final static Logger LOGGER = LoggerFactory.getLogger(RepairDeviceQuotationInvoiceService.class);

    public RepairDeviceQuotationInvoiceService() {
    }

    @Autowired
    private RepairDeviceQuotationInvoiceDao repairDeviceQuotationInvoiceDao;

    @Override
    public void setQuotationInvoice(Integer repairDeviceId, List<RepairDeviceDetectInvoiceDbo> detectInvoiceDbos) {

        if (CollectionUtils.isEmpty(detectInvoiceDbos)) {
            return;
        }

        List<RepairDeviceQuotationInvoiceDbo> quotationInvoiceDbos = repairDeviceQuotationInvoiceDao
                .getListByRepairDevice(repairDeviceId);
        if (CollectionUtils.isEmpty(quotationInvoiceDbos)) {
            return;
        }

        Map<Integer, RepairDeviceQuotationInvoiceDbo> idQuotationInvoiceMap = new HashMap<>();
        for (RepairDeviceQuotationInvoiceDbo repairDeviceQuotationInvoiceDbo : quotationInvoiceDbos) {
            idQuotationInvoiceMap.put(repairDeviceQuotationInvoiceDbo.getDetectInvoiceId(),
                    repairDeviceQuotationInvoiceDbo);
        }

        for (RepairDeviceDetectInvoiceDbo repairDeviceDetectInvoiceDbo : detectInvoiceDbos) {
            repairDeviceDetectInvoiceDbo
                    .setQuotationInvoice(idQuotationInvoiceMap.get(repairDeviceDetectInvoiceDbo.getId()));
        }

        idQuotationInvoiceMap.clear();
        idQuotationInvoiceMap = null;

        quotationInvoiceDbos.clear();
        quotationInvoiceDbos = null;

    }

    @Override
    public void saveQuotationInvoice(Integer createUserId, List<RepairDeviceQuotationInvoiceDbo> quotationInvoiceDbos)
            throws ServiceException {

        List<RepairDeviceQuotationInvoiceDbo> addDbos = null;
        List<RepairDeviceQuotationInvoiceDbo> updateDbos = null;

        Date now = new Date(System.currentTimeMillis());

        for (RepairDeviceQuotationInvoiceDbo quotationInvoiceDbo : quotationInvoiceDbos) {

            quotationInvoiceDbo.setUpdateTime(now);

            if (null != quotationInvoiceDbo.getId()) {
                if (null == updateDbos) {
                    updateDbos = new ArrayList<>();
                }
                updateDbos.add(quotationInvoiceDbo);
            }
            else {
                if (null == addDbos) {
                    addDbos = new ArrayList<>();
                }
                quotationInvoiceDbo.setCreateTime(now);
                quotationInvoiceDbo.setCreateUserId(createUserId);
                addDbos.add(quotationInvoiceDbo);
            }
        }

        try {
            if (CollectionUtils.isNotEmpty(addDbos)) {
                repairDeviceQuotationInvoiceDao.batchSave(addDbos);
            }

            if (CollectionUtils.isNotEmpty(updateDbos)) {
                repairDeviceQuotationInvoiceDao.batchUpdate(updateDbos);
            }
        }
        catch (BaseDaoException e) {
            LOGGER.error("[][saveQuotationInvoice][failed]", e);
            throw new ServiceException(ServiceCode.system_db_exception, "");
        }
        finally {
            if (null != addDbos) {
                addDbos.clear();
                addDbos = null;
            }

            if (null != updateDbos) {
                updateDbos.clear();
                updateDbos = null;
            }
        }

    }

    @Override
    public void saveQuotationInvoiceConfirm(RepairDeviceQuotationInvoiceDbo quotationInvoiceDbo) {
        Date now = new Date(System.currentTimeMillis());
        quotationInvoiceDbo.setUpdateTime(now);
        quotationInvoiceDbo.setRepairDeviceId(null);
        repairDeviceQuotationInvoiceDao.updateConfirmStatus(quotationInvoiceDbo);
    }

    @Override
    public void setQuotationInvoice(List<RepairDeviceDbo> repairDeviceDbos) {

        List<Integer> repairDeviceIds = new ArrayList<>(repairDeviceDbos.size());
        for (RepairDeviceDbo repairDeviceDbo : repairDeviceDbos) {
            repairDeviceIds.add(repairDeviceDbo.getId());
        }

        List<RepairDeviceQuotationInvoiceDbo> quotationInvoiceDbos = repairDeviceQuotationInvoiceDao
                .getListByRepairDevice(repairDeviceIds);

        repairDeviceIds.clear();
        repairDeviceIds = null;

        if (CollectionUtils.isEmpty(quotationInvoiceDbos)) {
            return;
        }

        Map<Integer, RepairDeviceQuotationInvoiceDbo> idQuotationInvoiceMap = new HashMap<>();
        for (RepairDeviceQuotationInvoiceDbo repairDeviceQuotationInvoiceDbo : quotationInvoiceDbos) {
            idQuotationInvoiceMap.put(repairDeviceQuotationInvoiceDbo.getDetectInvoiceId(),
                    repairDeviceQuotationInvoiceDbo);
        }

        for (RepairDeviceDbo repairDeviceDbo : repairDeviceDbos) {
            List<RepairDeviceDetectInvoiceDbo> detectInvoiceDbos = repairDeviceDbo.getDetectInvoices();
            if (CollectionUtils.isEmpty(detectInvoiceDbos)) {
                continue;
            }
            for (RepairDeviceDetectInvoiceDbo repairDeviceDetectInvoiceDbo : detectInvoiceDbos) {
                repairDeviceDetectInvoiceDbo
                        .setQuotationInvoice(idQuotationInvoiceMap.get(repairDeviceDetectInvoiceDbo.getId()));
            }
        }

        idQuotationInvoiceMap.clear();
        idQuotationInvoiceMap = null;

        quotationInvoiceDbos.clear();
        quotationInvoiceDbos = null;

    }

}
