package com.idata.sale.service.web.base.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.idata.sale.service.web.base.dao.BaseDaoCode;
import com.idata.sale.service.web.base.dao.BaseDaoException;
import com.idata.sale.service.web.base.dao.PageInfo;
import com.idata.sale.service.web.base.dao.constant.Device;
import com.idata.sale.service.web.base.dao.constant.RepairDeviceQuotationConfirmStatus;
import com.idata.sale.service.web.base.dao.constant.RepairResult;
import com.idata.sale.service.web.base.dao.constant.RepairStatus;
import com.idata.sale.service.web.base.dao.dbo.DeviceDbo;
import com.idata.sale.service.web.base.dao.dbo.RepairDeviceDbo;
import com.idata.sale.service.web.base.dao.dbo.RepairDeviceDetectInvoiceDbo;
import com.idata.sale.service.web.base.dao.dbo.RepairDeviceQuotationInvoiceDbo;
import com.idata.sale.service.web.base.dao.dbo.RepairInvoiceDbo;
import com.idata.sale.service.web.base.dao.dbo.RepairPackageDbo;
import com.idata.sale.service.web.base.dao.fo.RepairDeviceFo;
import com.idata.sale.service.web.base.dao.impl.RepairDeviceDao;
import com.idata.sale.service.web.base.dao.impl.RepairDeviceDetectInvoiceDao;
import com.idata.sale.service.web.base.dao.impl.RepairDeviceQuotationInvoiceDao;
import com.idata.sale.service.web.base.service.IDeviceService;
import com.idata.sale.service.web.base.service.IRepairDeviceDetectInvoiceService;
import com.idata.sale.service.web.base.service.IRepairDeviceQuotationInvoiceService;
import com.idata.sale.service.web.base.service.IRepairDeviceService;
import com.idata.sale.service.web.base.service.IRepairInvoiceService;
import com.idata.sale.service.web.base.service.IRepairPackageService;
import com.idata.sale.service.web.base.service.ServiceException;
import com.idata.sale.service.web.base.service.constant.ServiceCode;
import com.idata.sale.service.web.base.service.dto.RepairDeviceBaseDto;
import com.idata.sale.service.web.base.service.event.RepairDeviceStatusEvent;
import com.idata.sale.service.web.util.TimeUtil;

@Service
public class RepairDeviceService implements IRepairDeviceService {

    private final static Set<Integer> OperateRepairSet = new HashSet<>();

    private final static Logger LOGGER = LoggerFactory.getLogger(RepairDeviceService.class);

    public RepairDeviceService() {
    }

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private RepairDeviceDao repairDeviceDao;

    @Autowired
    private IRepairInvoiceService repairInvoiceService;

    @Autowired
    private IRepairPackageService repairPackageService;

    @Override
    public void add(RepairDeviceDbo repairDeviceDbo) throws ServiceException {

        if (null == repairDeviceDbo.getRepairInvoiceId()) {
            throw new ServiceException(ServiceCode.system_param_error, "RepairInvoiceId is empty");
        }

        if (null == repairDeviceDbo.getRepairPackageId()) {
            throw new ServiceException(ServiceCode.system_param_error, "RepairPackageId is empty");
        }

        String sn = repairDeviceDbo.getSn();
        DeviceDbo deviceDbo = deviceService.get(sn);

        int repairTimes = 1;
        int deviceId = -1;
        if (null == deviceDbo) {
            deviceDbo = deviceService.add(repairDeviceDbo);
        }
        else {
            repairTimes = deviceDbo.getRepairedTimes() + 1;
        }

        deviceId = deviceDbo.getId();
        repairDeviceDbo.setDeviceId(deviceId);
        repairDeviceDbo.setRepairTimes(repairTimes);
        repairDeviceDbo.setLastRepairTime(deviceDbo.getLastRepairTime());

        Date now = new Date(System.currentTimeMillis());

        repairDeviceDbo.setCreateTime(now);
        repairDeviceDbo.setUpdateTime(now);
        repairDeviceDbo.setDeliveryTime(now);

        repairDeviceDbo.setStatus(RepairStatus.Received.getCode());

        try {
            repairDeviceDao.save(repairDeviceDbo);

            deviceDbo.setLastRepairTime(now);
            deviceDbo.setRepairedTimes(repairTimes);
            deviceService.update(deviceDbo);
        }
        catch (BaseDaoException e) {

            if (e.getCode().equals(BaseDaoCode.bean_duplicate_entry_exception)) {
                throw new ServiceException(ServiceCode.system_object_exist_error, e.getMessage());
            }
            else {
                throw new ServiceException(ServiceCode.system_db_exception, e.getMessage());
            }
        }
        finally {
            deviceDbo = null;
        }
    }

    @Override
    public void update(RepairDeviceDbo repairDeviceDbo) throws ServiceException {

        repairDeviceDbo.setUpdateTime(new Date(System.currentTimeMillis()));

        try {
            repairDeviceDao.update(repairDeviceDbo);
        }
        catch (BaseDaoException e) {
            LOGGER.error("[][update][repairDeviceDbo failed]", e);
            throw new ServiceException(ServiceCode.system_db_exception, e.getMessage());
        }
    }

    @Override
    public void delete(RepairDeviceDbo repairDeviceDbo) {
        try {
            repairDeviceDao.delete(repairDeviceDbo);
        }
        catch (BaseDaoException e) {
            LOGGER.error("[][delete][RepairDeviceDbo failed," + repairDeviceDbo + "]", e);
        }
    }

    @Override
    public List<RepairDeviceDbo> getList4SearchBySn(String sn) {

        RepairDeviceFo deviceFo = new RepairDeviceFo();
        deviceFo.setSn(sn);

        try {
            return repairDeviceDao.list4Search(deviceFo);
        }
        finally {
            deviceFo = null;
        }
    }

    @Override
    public List<RepairDeviceDbo> getList4SearchByPacakgeId(Integer pacakgeId) {
        RepairDeviceFo deviceFo = new RepairDeviceFo();
        deviceFo.setRepairPackageId(pacakgeId);

        try {
            return repairDeviceDao.list4Search(deviceFo);
        }
        finally {
            deviceFo = null;
        }
    }

    @Override
    public List<RepairDeviceDbo> list(PageInfo pageInfo, RepairDeviceDbo repairDeviceDbo) {

        return repairDeviceDao.list(pageInfo, repairDeviceDbo);

    }

    @Override
    public List<RepairDeviceDbo> list(PageInfo pageInfo, RepairDeviceFo repairDeviceFo) {

        return repairDeviceDao.list(pageInfo, repairDeviceFo);

    }

    @Override
    public RepairDeviceBaseDto getInvoiceData(String packageSerialNumber, String invoiceSerialNumber) {

        RepairDeviceBaseDto baseDto = new RepairDeviceBaseDto();

        RepairPackageDbo packageDbo = repairPackageService.get(packageSerialNumber);

        baseDto.setContactNumber(packageDbo.getContactNumber());
        baseDto.setContacts(packageDbo.getContacts());
        baseDto.setExpressName(packageDbo.getExpressName());
        baseDto.setExpressNumber(packageDbo.getExpressNumber());
        baseDto.setPackageSerialNumber(packageDbo.getSerialNumber());
        baseDto.setReceiptTime(packageDbo.getReceiptTime());
        baseDto.setStatus(packageDbo.getStatus());
        packageDbo = null;

        RepairInvoiceDbo invoiceDbo = repairInvoiceService.get(invoiceSerialNumber);
        baseDto.setInvoiceSerialNumber(invoiceDbo.getSerialNumber());
        baseDto.setCustomerRma(invoiceDbo.getCustomerRma());
        invoiceDbo = null;

        return baseDto;
    }

    @Override
    public DeviceDbo getBaseData(String sn) {
        return deviceService.get(sn);
    }

    @Override
    public RepairDeviceDbo get(Integer id) {
        return repairDeviceDao.findById(id);
    }

    @Autowired
    private RepairDeviceAsyncService repairDeviceAsyncService;

    @Override
    public void toWaitDetect(List<Integer> repairDeviceIds) {
        RepairDeviceDbo deviceDbo = new RepairDeviceDbo();
        deviceDbo.setStatus(RepairStatus.CheckWait.getCode());
        repairDeviceDao.updateRepairDeviceStatus(repairDeviceIds, deviceDbo);

        Collection<Integer> packageIds = repairDeviceDao.getRepairPackageIds(repairDeviceIds);
        if (CollectionUtils.isNotEmpty(packageIds)) {
            repairDeviceAsyncService.checkRepairPackageStatus(packageIds, RepairStatus.CheckWait);
        }

        deviceDbo = null;
    }

    @Override
    public void detectStart(Integer userId, Integer repairDeviceId, String repaiDeviceSn) throws ServiceException {

        if (isOperate(repairDeviceId) || !startOperate(repairDeviceId)) {
            throw new ServiceException(ServiceCode.system_object_busy_error, "");
        }

        RepairDeviceDbo deviceDbo = null;
        RepairDeviceDbo dbDeviceDbo = null;

        try {
            dbDeviceDbo = repairDeviceDao.findById(repairDeviceId);
            if (null == dbDeviceDbo) {
                throw new ServiceException(ServiceCode.system_param_error, "repairDeviceId is error");
            }
            if (null != dbDeviceDbo.getDetectUserId()
                    && RepairStatus.CheckWait.getCode() != dbDeviceDbo.getStatus().intValue()) {
                throw new ServiceException(ServiceCode.repair_device_status_error, "status:" + dbDeviceDbo.getStatus());
            }

            deviceDbo = new RepairDeviceDbo();
            deviceDbo.setStatus(RepairStatus.Checking.getCode());
            deviceDbo.setDetectUserId(userId);

            List<Integer> repairDeviceIds = new ArrayList<>();
            repairDeviceIds.add(repairDeviceId);
            repairDeviceDao.updateRepairDeviceStatus(repairDeviceIds, deviceDbo);
            repairDeviceIds.clear();
            repairDeviceIds = null;
        }
        finally {
            clearOperate(repairDeviceId);
            deviceDbo = null;
            dbDeviceDbo = null;
        }
    }

    @Override
    public void detectFinish(Integer userId, Integer repairDeviceId, String repaiDeviceSn) throws ServiceException {

        if (isOperate(repairDeviceId) || !startOperate(repairDeviceId)) {
            throw new ServiceException(ServiceCode.system_object_busy_error, "");
        }

        RepairDeviceDbo deviceDbo = null;
        RepairDeviceDbo dbDeviceDbo = null;

        try {
            dbDeviceDbo = repairDeviceDao.findById(repairDeviceId);
            if (!dbDeviceDbo.getDetectUserId().equals(userId)) {
                throw new ServiceException(ServiceCode.repair_invoice_param_error,
                        "detect user is difference,detectUserId:" + dbDeviceDbo.getDetectUserId());
            }

            deviceDbo = new RepairDeviceDbo();
            deviceDbo.setStatus(RepairStatus.Checked.getCode());

            List<Integer> repairDeviceIds = new ArrayList<>();
            repairDeviceIds.add(repairDeviceId);
            repairDeviceDao.updateRepairDeviceStatus(repairDeviceIds, deviceDbo);
            repairDeviceIds.clear();
            repairDeviceIds = null;
        }
        finally {
            clearOperate(repairDeviceId);
            deviceDbo = null;
            dbDeviceDbo = null;
        }
    }

    @Override
    public List<RepairDeviceDbo> getWaitDetectlist(Integer repairStationId) {

        RepairDeviceFo deviceFo = new RepairDeviceFo();
        deviceFo.setStatus(RepairStatus.CheckWait.getName());
        deviceFo.setRepairStationId(repairStationId);
        return repairDeviceDao.getList4Device(deviceFo);
    }

    @Override
    public List<RepairDeviceDbo> getDetectinglist(Integer userId) {
        RepairDeviceFo deviceFo = new RepairDeviceFo();
        deviceFo.setStatus(RepairStatus.Checking.getName() + "," + RepairStatus.CheckAgain.getName());
        deviceFo.setDetectUserId(userId);

        return repairDeviceDao.getList4Device(deviceFo);
    }

    @Override
    public List<RepairDeviceDbo> getWaitRepairlist(Integer userId) {
        RepairDeviceFo deviceFo = new RepairDeviceFo();
        deviceFo.setStatus(RepairStatus.RepairWait.getName());
        deviceFo.setDetectUserId(userId);
        return repairDeviceDao.getList4Device(deviceFo);
    }

    @Override
    public List<RepairDeviceDbo> getRepairinglist(Integer userId) {
        RepairDeviceFo deviceFo = new RepairDeviceFo();
        deviceFo.setStatus(RepairStatus.Repairing.getName() + "," + RepairStatus.RepairAgain.getName());
        deviceFo.setDetectUserId(userId);
        return repairDeviceDao.getList4Device(deviceFo);
    }

    @Autowired
    private RepairDeviceDetectInvoiceDao detectInvoiceDao;

    @Override
    public void detectReviewPass(Integer userId, List<Integer> repairDeviceIds) throws ServiceException {

        List<RepairDeviceDetectInvoiceDbo> invoiceDbos = detectInvoiceDao
                .getQuotedPriceListByRepairDevice(repairDeviceIds);

        Set<Integer> waitQuotationList = new HashSet<>();

        Set<Integer> waitRepairList = new HashSet<>(repairDeviceIds.size());
        for (Integer rdid : repairDeviceIds) {
            waitRepairList.add(rdid);
        }

        if (CollectionUtils.isNotEmpty(invoiceDbos)) {
            for (RepairDeviceDetectInvoiceDbo repairDeviceDetectInvoiceDbo : invoiceDbos) {
                Integer repairDeviceId = repairDeviceDetectInvoiceDbo.getRepairDeviceId();

                if (waitQuotationList.contains(repairDeviceId)) {
                    continue;
                }

                if (null == repairDeviceDetectInvoiceDbo.getQuotedPrice()) {
                    continue;
                }

                if (Device.QuotedPrice.getCode() == repairDeviceDetectInvoiceDbo.getQuotedPrice().intValue()) {
                    waitQuotationList.add(repairDeviceId);
                    waitRepairList.remove(repairDeviceId);
                }
            }
            invoiceDbos.clear();
        }

        invoiceDbos = null;

        RepairDeviceDbo repairDeviceDbo = new RepairDeviceDbo();

        try {
            if (CollectionUtils.isNotEmpty(waitQuotationList)) {
                repairDeviceDbo.setStatus(RepairStatus.QuotationWait.getCode());
                repairDeviceDbo.setCharge(Device.Charge.getCode());
                repairDeviceDao.updateRepairDeviceStatus(waitQuotationList, repairDeviceDbo);
            }

            if (CollectionUtils.isNotEmpty(waitRepairList)) {
                repairDeviceDbo.setStatus(RepairStatus.RepairWait.getCode());
                repairDeviceDbo.setCharge(Device.ChargeNot.getCode());
                repairDeviceDbo.setCostTotal("0");
                repairDeviceDbo.setLaborCosts("0");
                repairDeviceDao.updateRepairDeviceStatus(waitRepairList, repairDeviceDbo);
            }
        }
        finally {
            repairDeviceDbo = null;
            waitQuotationList.clear();
            waitQuotationList = null;

            waitRepairList.clear();
            waitRepairList = null;
        }

    }

    @Override
    public void detectReviewFail(Integer userId, List<Integer> repairDeviceIds) throws ServiceException {
        RepairDeviceDbo repairDeviceDbo = new RepairDeviceDbo();
        repairDeviceDbo.setStatus(RepairStatus.CheckAgain.getCode());
        repairDeviceDao.updateRepairDeviceStatus(repairDeviceIds, repairDeviceDbo);
        repairDeviceDbo = null;
    }

    @Override
    public void repairStart(Integer userId, Integer repairDeviceId, String repaiDeviceSn) throws ServiceException {

        validateDeviceSn(repairDeviceId, repaiDeviceSn);

        RepairDeviceDbo deviceDbo = new RepairDeviceDbo();
        deviceDbo.setStatus(RepairStatus.Repairing.getCode());
        deviceDbo.setRepairUserId(userId);
        repairDeviceDao.updateRepairDeviceStatus(repairDeviceId, deviceDbo);
        deviceDbo = null;
    }

    @Override
    public void repairFinish(Integer userId, Integer repairDeviceId, String repaiDeviceSn) throws ServiceException {

        validateDeviceSn(repairDeviceId, repaiDeviceSn);

        RepairDeviceDbo deviceDbo = new RepairDeviceDbo();
        deviceDbo.setStatus(RepairStatus.RepairFinish.getCode());
        repairDeviceDao.updateRepairDeviceStatus(repairDeviceId, deviceDbo);
        List<Integer> rpids = new ArrayList<>(1);
        rpids.add(repairDeviceId);
        Collection<Integer> packageIds = repairDeviceDao.getRepairPackageIds(rpids);
        if (CollectionUtils.isNotEmpty(packageIds)) {
            repairDeviceAsyncService.checkRepairPackageStatus(packageIds, RepairStatus.RepairFinish);
        }
        deviceDbo = null;
    }

    @Autowired
    private RepairDeviceQuotationInvoiceDao quotationInvoiceDao;

    @Override
    public void finishQutation(Integer userId, Integer repairDeviceId, String repaiDeviceSn) throws ServiceException {

        RepairDeviceDbo repairDeviceDbo = repairDeviceDao.findById(repairDeviceId);
        if (null == repairDeviceDbo) {
            throw new ServiceException(ServiceCode.system_object_not_exist_error,
                    "not found repairDeviceDbo,repairDeviceId:" + repairDeviceId);
        }

        int laborCost = 0;

        String laborCostStr = repairDeviceDbo.getLaborCosts();
        if (StringUtils.isNotEmpty(laborCostStr)) {
            laborCost = Integer.parseInt(laborCostStr);
        }

        List<RepairDeviceQuotationInvoiceDbo> quotationInvoiceDbos = quotationInvoiceDao
                .getListByRepairDevice(repairDeviceId);

        int costTotal = 0;

        List<Integer> confirmIds = new ArrayList<>();

        try {
            for (RepairDeviceQuotationInvoiceDbo quotationInvoiceDbo : quotationInvoiceDbos) {

                Integer confirmStatus = quotationInvoiceDbo.getConfirmStatus();
                if (null != confirmStatus
                        && RepairDeviceQuotationConfirmStatus.Refused.code == confirmStatus.intValue()) {
                    continue;
                }

                confirmIds.add(quotationInvoiceDbo.getId());

                int quantity = quotationInvoiceDbo.getQuantity();
                int priceUnit = Integer.parseInt(quotationInvoiceDbo.getPriceUnit());
                int priceTotal = Integer.parseInt(quotationInvoiceDbo.getPriceTotal());
                int total = quantity * priceUnit;
                if (total != priceTotal) {
                    throw new ServiceException(ServiceCode.system_param_error, "priceTotal not equal total");
                }
                costTotal = costTotal + priceTotal;
            }
        }
        catch (ServiceException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ServiceException(ServiceCode.system_param_error, e.getMessage());
        }
        finally {
            if (CollectionUtils.isNotEmpty(quotationInvoiceDbos)) {

                quotationInvoiceDbos.clear();

                quotationInvoiceDbos = null;
            }
        }

        costTotal += laborCost;

        RepairDeviceDbo repairDevice = repairDeviceDao.findById(repairDeviceId);

        if (null == repairDevice) {
            throw new ServiceException(ServiceCode.system_param_error, "not found object");
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(
                    "[][][costTotal:" + costTotal + ";repairDevice costTotal:" + (repairDevice.getCostTotal()) + "]");
        }

        if (!(String.valueOf(costTotal).equals(repairDevice.getCostTotal()))) {
            throw new ServiceException(ServiceCode.system_param_error, "cost total not equal");
        }

        // confirmIds

        if (CollectionUtils.isNotEmpty(confirmIds)) {
            quotationInvoiceDao.confirmQuotationInvoice(confirmIds);
            confirmIds.clear();
            confirmIds = null;
        }

        repairDevice = null;

        RepairDeviceDbo rd = new RepairDeviceDbo();
        rd.setStatus(RepairStatus.CustomerConfirmWait.getCode());
        rd.setPayStatus(Device.PayNot.getCode());

        repairDeviceDao.updateRepairDeviceStatus(repairDeviceId, rd);

        rd = null;

    }

    @Override
    public void confirmQutation(Integer userId, Integer repairDeviceId, String repaiDeviceSn) throws ServiceException {

        validateDeviceSn(repairDeviceId, repaiDeviceSn);

        int nextStatus = RepairStatus.PayWait.getCode();
        Integer payStatus = null;
        Date payFinishDate = null;

        RepairDeviceDbo dbRepairDeviceDbo = repairDeviceDao.findById(repairDeviceId);
        if (dbRepairDeviceDbo.getCostTotal().equals("0")) {
            nextStatus = RepairStatus.RepairWait.getCode();
            payStatus = Device.PayFinish.getCode();
            payFinishDate = new Date(System.currentTimeMillis());
        }
        else if (Device.PayTypeMonth.getCode() == dbRepairDeviceDbo.getPayType().intValue()) {
            nextStatus = RepairStatus.RepairWait.getCode();
        }
        dbRepairDeviceDbo = null;

        RepairDeviceDbo rdo = new RepairDeviceDbo();
        rdo.setStatus(nextStatus);
        rdo.setPayStatus(payStatus);
        rdo.setPayFinishTime(payFinishDate);

        repairDeviceDao.updateRepairDeviceStatus(repairDeviceId, rdo);

    }

    @Override
    public void refuseQutation(Integer userId, Integer repairDeviceId, String repaiDeviceSn) throws ServiceException {

        validateDeviceSn(repairDeviceId, repaiDeviceSn);

        int nextStatus = RepairStatus.BackWait.getCode();

        RepairDeviceDbo rdo = new RepairDeviceDbo();
        rdo.setStatus(nextStatus);
        rdo.setDeliveryTime(new Date(System.currentTimeMillis()));
        rdo.setPayStatus(Device.PayUserRefuse.getCode());
        rdo.setResult(RepairResult.FailUserRefusePay.code);

        repairDeviceDao.updateRepairDeviceStatus(repairDeviceId, rdo);
    }

    @Override
    public void finishPay(Integer userId, Integer repairDeviceId, String repaiDeviceSn, String payDesc)
            throws ServiceException {

        RepairDeviceDbo repairDeviceDbo = repairDeviceDao.findById(repairDeviceId);
        if (null == repairDeviceDbo) {
            throw new ServiceException(ServiceCode.system_object_not_exist_error, "repairDeviceId:" + repairDeviceId);
        }

        if (!repairDeviceDbo.getSn().equals(repaiDeviceSn)) {
            throw new ServiceException(ServiceCode.system_param_error, "sn is error");
        }

        RepairDeviceDbo rdo = new RepairDeviceDbo();

        Integer payType = repairDeviceDbo.getPayType();
        if (Device.PayTypeCash.getCode() == payType.intValue()) {
            rdo.setStatus(RepairStatus.RepairWait.getCode());
        }

        rdo.setPayDescription(payDesc);
        rdo.setPayStatus(Device.PayFinish.getCode());
        rdo.setPayFinishTime(new Date(System.currentTimeMillis()));

        repairDeviceDao.updateRepairDeviceStatus(repairDeviceId, rdo);
        rdo = null;
        LOGGER.info("[][finishPay][repairDeviceId:" + repairDeviceId + ";payType:" + payType + "]");
    }

    @Override
    public void addToBackPackage(Integer backPackageId, List<Integer> ids) throws ServiceException {
        repairDeviceDao.updateRepairBackPackage(backPackageId, ids);
        LOGGER.info("[][][add backpackage ,backPackageId:" + backPackageId + ",ids:" + ids + "]");
    }

    @Override
    public void removeFromBackPackage(Integer backPackageId, List<Integer> ids) throws ServiceException {
        repairDeviceDao.updateRepairBackPackage(null, ids);
        LOGGER.info("[][][remove backpackage ,backPackageId:" + backPackageId + ",ids:" + ids + "]");
    }

    private void validateDeviceSn(Integer repairDeviceId, String sn) throws ServiceException {

        RepairDeviceDbo repairDeviceDbo = repairDeviceDao.findById(repairDeviceId);

        try {
            if (null == repairDeviceDbo) {
                throw new ServiceException(ServiceCode.system_param_error, "not found repairDeviceInfo");
            }

            if (!repairDeviceDbo.getSn().equals(sn)) {
                throw new ServiceException(ServiceCode.system_param_error, "repairDeviceInfo is error");
            }
        }
        finally {
            repairDeviceDbo = null;
        }

    }

    private boolean startOperate(Integer deviceId) {
        synchronized (OperateRepairSet) {
            if (!OperateRepairSet.contains(deviceId)) {
                OperateRepairSet.add(deviceId);
                return true;
            }
        }
        return false;
    }

    private boolean isOperate(Integer deviceId) {
        synchronized (OperateRepairSet) {
            return OperateRepairSet.contains(deviceId);
        }
    }

    private void clearOperate(Integer deviceId) {
        synchronized (OperateRepairSet) {
            OperateRepairSet.remove(deviceId);
        }
    }

    @Override
    public void finishConfim(List<Integer> ids) throws ServiceException {

        RepairDeviceDbo repairDeviceDbo = new RepairDeviceDbo();
        repairDeviceDbo.setStatus(RepairStatus.BackWait.getCode());
        repairDeviceDbo.setResult(RepairResult.Success.code);

        repairDeviceDao.updateRepairDeviceStatus(ids, repairDeviceDbo);

    }

    @Autowired
    private IRepairDeviceQuotationInvoiceService quotationInvoiceService;

    @Autowired
    private IRepairDeviceDetectInvoiceService detectInvoiceService;

    @Override
    public void modifyStatus(Integer repairDeviceId, String sn, int status) throws ServiceException {

        RepairDeviceDbo deviceDbo = repairDeviceDao.findById(repairDeviceId);
        if (null == deviceDbo || !deviceDbo.getSn().equals(sn)) {
            throw new ServiceException(ServiceCode.system_object_not_exist_error, "sn:" + sn);
        }

        if (status == RepairStatus.CheckWait.getCode()) {
            LOGGER.info("[][modifyStatus][recheck,repairDeviceId:" + repairDeviceId + "]");
            recheck(repairDeviceId, sn);
        }
        else {
            LOGGER.error("[][modifyStatus][status error,repairDeviceId:" + repairDeviceId + ";status:" + status + "]");
        }

    }

    private void recheck(Integer repairDeviceId, String sn) throws ServiceException {

        RepairDeviceDbo updateDbo = new RepairDeviceDbo();
        updateDbo.setId(repairDeviceId);
        updateDbo.setStatus(RepairStatus.CheckWait.getCode());
        updateDbo.setLaborCosts("0");
        updateDbo.setCostTotal("0");
        updateDbo.setCharge(Device.ChargeNot.getCode());
        updateDbo.setPayType(Device.PayTypeCash.getCode());
        updateDbo.setPayStatus(Device.PayNot.getCode());
        updateDbo.setUpdateTime(TimeUtil.getNowDate());

        try {
            repairDeviceDao.update(updateDbo);
            LOGGER.info("[][recheck][update repairDevice success," + updateDbo + "]");
        }
        catch (BaseDaoException e) {
            throw new ServiceException(ServiceCode.system_db_exception, e.getMessage());
        }

        List<RepairDeviceDetectInvoiceDbo> detectInvoiceDbos = detectInvoiceService
                .getListByRepairDevice(repairDeviceId);
        if (CollectionUtils.isNotEmpty(detectInvoiceDbos)) {
            List<Integer> repairDeviceDetectInvoiceIds = new ArrayList<>(detectInvoiceDbos.size());
            for (RepairDeviceDetectInvoiceDbo repairDeviceDetectInvoiceDbo : detectInvoiceDbos) {
                repairDeviceDetectInvoiceIds.add(repairDeviceDetectInvoiceDbo.getId());
            }

            quotationInvoiceService.delete(repairDeviceDetectInvoiceIds);
            repairDeviceDetectInvoiceIds.clear();
            repairDeviceDetectInvoiceIds = null;

            LOGGER.info("[][recheck][delete detectInvoiceDbos," + Arrays.toString(detectInvoiceDbos.toArray()) + "]");
            detectInvoiceDbos.clear();
            detectInvoiceDbos = null;
        }

    }

    @EventListener
    public void dealRepairDeviceStatusEvent(RepairDeviceStatusEvent repairDeviceStatusEvent) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][dealRepairDeviceStatusEvent][" + repairDeviceStatusEvent + "]");
        }

        List<Integer> repairDeviceIds = repairDeviceStatusEvent.getRepairDeviceIds();
        RepairStatus status = repairDeviceStatusEvent.getStatus();

        RepairDeviceDbo repairDeviceDbo = new RepairDeviceDbo();
        repairDeviceDbo.setStatus(status.getCode());
        if (status.equals(RepairStatus.Backing)) {
            repairDeviceDbo.setDeliveryTime(new Date(System.currentTimeMillis()));
        }

        repairDeviceDao.updateRepairDeviceStatus(repairDeviceIds, repairDeviceDbo);
        repairDeviceDbo = null;

        Set<Integer> packageIds = repairDeviceDao.getRepairPackageIds(repairDeviceIds);

        if (CollectionUtils.isNotEmpty(packageIds)) {
            repairDeviceAsyncService.checkRepairPackageStatus(packageIds, status);
        }
    }

}
