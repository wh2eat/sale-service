package com.idata.sale.service.web.base.service;

import com.idata.sale.service.web.base.dao.dbo.DeviceDbo;
import com.idata.sale.service.web.base.dao.dbo.RepairDeviceDbo;

public interface IDeviceService {

    public int add(DeviceDbo deviceDbo);

    public DeviceDbo add(RepairDeviceDbo repairDeviceDbo);

    public void update(DeviceDbo deviceDbo);

    public DeviceDbo get(String sn);

}
