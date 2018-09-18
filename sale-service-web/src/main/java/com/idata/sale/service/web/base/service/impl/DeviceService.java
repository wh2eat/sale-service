package com.idata.sale.service.web.base.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idata.sale.service.web.base.dao.BaseDaoException;
import com.idata.sale.service.web.base.dao.dbo.DeviceDbo;
import com.idata.sale.service.web.base.dao.dbo.RepairDeviceDbo;
import com.idata.sale.service.web.base.dao.impl.DeviceDao;
import com.idata.sale.service.web.base.service.IDeviceService;

@Service
public class DeviceService implements IDeviceService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);

    public DeviceService() {
    }

    @Autowired
    private DeviceDao deviceDao;

    @Override
    public int add(DeviceDbo deviceDbo) {

        Date now = new Date(System.currentTimeMillis());

        deviceDbo.setCreateTime(now);
        deviceDbo.setUpdateTime(now);
        deviceDbo.setRepairedTimes(0);

        try {
            int id = (int) deviceDao.save(deviceDbo);
            LOGGER.info("[DeviceService][add][success,id:" + id + ";" + deviceDbo + "]");
            return id;
        }
        catch (BaseDaoException e) {
            LOGGER.error("[DeviceService][add][failed," + deviceDbo + "]", e);
        }
        finally {
            now = null;
        }
        return -1;
    }

    @Override
    public DeviceDbo add(RepairDeviceDbo repairDeviceDbo) {

        DeviceDbo deviceDbo = new DeviceDbo();
        deviceDbo.setSn(repairDeviceDbo.getSn());
        deviceDbo.setAgentName(repairDeviceDbo.getAgentName());
        deviceDbo.setEndCustomerName(repairDeviceDbo.getEndCustomerName());
        deviceDbo.setImei(repairDeviceDbo.getImei());
        deviceDbo.setImeiTwo(repairDeviceDbo.getImeiTwo());
        deviceDbo.setManufactureTime(repairDeviceDbo.getManufactureTime());
        deviceDbo.setMeid(repairDeviceDbo.getMeid());
        deviceDbo.setMeidTwo(repairDeviceDbo.getMeidTwo());
        deviceDbo.setModel(repairDeviceDbo.getModel());
        deviceDbo.setMachineType(repairDeviceDbo.getMachineType());

        int id = add(deviceDbo);

        deviceDbo.setId(id);

        LOGGER.info("[][add][device success," + deviceDbo + "]");

        return deviceDbo;

    }

    @Override
    public DeviceDbo get(String sn) {

        return deviceDao.getBySn(sn);
    }

    @Override
    public void update(DeviceDbo deviceDbo) {

        try {
            deviceDbo.setUpdateTime(new Date(System.currentTimeMillis()));
            deviceDao.update(deviceDbo);
        }
        catch (BaseDaoException e) {
            LOGGER.error("[][update][failed]", e);
        }
    }

}
