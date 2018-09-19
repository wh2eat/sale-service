package com.idata.sale.service.web.base.service;

import java.util.List;

import com.idata.sale.service.web.base.dao.PageInfo;
import com.idata.sale.service.web.base.dao.dbo.DeviceDbo;
import com.idata.sale.service.web.base.dao.dbo.RepairDeviceDbo;
import com.idata.sale.service.web.base.dao.fo.RepairDeviceFo;
import com.idata.sale.service.web.base.service.dto.RepairDeviceBaseDto;

public interface IRepairDeviceService {

    public void add(RepairDeviceDbo repairDeviceDbo) throws ServiceException;

    public void update(RepairDeviceDbo repairDeviceDbo) throws ServiceException;

    public void delete(RepairDeviceDbo repairDeviceDbo);

    public List<RepairDeviceDbo> getList4SearchBySn(String sn);

    public List<RepairDeviceDbo> getList4SearchByPacakgeId(Integer pacakgeId);

    public List<RepairDeviceDbo> list(PageInfo pageInfo, RepairDeviceDbo repairDeviceDbo);

    public List<RepairDeviceDbo> list(PageInfo pageInfo, RepairDeviceFo repairDeviceFo);

    public List<RepairDeviceDbo> getWaitDetectlist(Integer repairStationId);

    public List<RepairDeviceDbo> getDetectinglist(Integer userId);

    public List<RepairDeviceDbo> getWaitRepairlist(Integer userId);

    public List<RepairDeviceDbo> getRepairinglist(Integer userId);

    public RepairDeviceBaseDto getInvoiceData(String packageSerialNumber, String invoiceSerialNumber);

    public DeviceDbo getBaseData(String sn);

    public RepairDeviceDbo get(Integer id);

    public void toWaitDetect(List<Integer> repairDeviceIds);

    public void detectStart(Integer userId, Integer repairDeviceId, String repaiDeviceSn) throws ServiceException;

    public void detectFinish(Integer userId, Integer repairDeviceId, String repaiDeviceSn) throws ServiceException;

    public void detectReviewPass(Integer userId, List<Integer> repairDeviceIds) throws ServiceException;

    public void detectReviewFail(Integer userId, List<Integer> repairDeviceIds) throws ServiceException;

    public void repairStart(Integer userId, Integer repairDeviceId, String repaiDeviceSn) throws ServiceException;

    public void repairFinish(Integer userId, Integer repairDeviceId, String repaiDeviceSn) throws ServiceException;

    public void finishQutation(Integer userId, Integer repairDeviceId, String repaiDeviceSn) throws ServiceException;

    public void confirmQutation(Integer userId, Integer repairDeviceId, String repaiDeviceSn) throws ServiceException;

    public void refuseQutation(Integer userId, Integer repairDeviceId, String repaiDeviceSn) throws ServiceException;

    public void finishPay(Integer userId, Integer repairDeviceId, String repaiDeviceSn, String payDesc)
            throws ServiceException;

    public void addToBackPackage(Integer backPackageId, List<Integer> ids) throws ServiceException;

    public void removeFromBackPackage(Integer backPackageId, List<Integer> ids) throws ServiceException;

    public void finishConfim(List<Integer> ids) throws ServiceException;

    public void modifyStatus(Integer repairDeviceId, String sn, int status) throws ServiceException;

}
