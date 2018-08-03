package com.idata.sale.service.web.base.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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

import com.idata.sale.service.web.base.dao.dbo.RepairDeviceDbo;
import com.idata.sale.service.web.base.dao.dbo.RepairPackageDbo;
import com.idata.sale.service.web.base.service.IRepairBackPackageService;
import com.idata.sale.service.web.base.service.IRepairDeviceService;
import com.idata.sale.service.web.base.service.IRepairPackageService;
import com.idata.sale.service.web.base.service.IRepairSearchService;

@Service
public class RepairSearchService implements IRepairSearchService {

    private final static Logger LOGGER = LoggerFactory.getLogger(RepairSearchService.class);

    public RepairSearchService() {
    }

    @Autowired
    private IRepairDeviceService repairDeviceService;

    @Autowired
    private IRepairPackageService repairPackageService;

    @Autowired
    private IRepairBackPackageService repairBackPackageService;

    @Override
    public List<RepairPackageDbo> searchBySn(String sn) {

        List<RepairDeviceDbo> repairDevices = repairDeviceService.getList4SearchBySn(sn);

        if (CollectionUtils.isEmpty(repairDevices)) {
            return null;
        }

        repairBackPackageService.setBackPackage(repairDevices);

        Set<Integer> repairPackageIdSet = new HashSet<>();
        for (RepairDeviceDbo repairDeviceDbo : repairDevices) {
            Integer repairPackageId = repairDeviceDbo.getRepairPackageId();
            if (null != repairPackageId && !repairPackageIdSet.contains(repairPackageId)) {
                repairPackageIdSet.add(repairPackageId);
            }
        }

        List<RepairPackageDbo> repairPackageDbos = repairPackageService.getList(repairPackageIdSet);

        if (repairPackageDbos.size() > 1) {
            Collections.sort(repairPackageDbos, new Comparator<RepairPackageDbo>() {

                @Override
                public int compare(RepairPackageDbo o1, RepairPackageDbo o2) {

                    int i1 = o1.getId();
                    int i2 = o2.getId();

                    if (i1 > i2) {
                        return -1;
                    }
                    else if (i1 < i2) {
                        return 1;
                    }
                    return 0;
                }
            });

        }

        Map<Integer, RepairPackageDbo> map = new HashMap<>(repairPackageDbos.size());

        for (RepairPackageDbo repairPackageDbo : repairPackageDbos) {
            map.put(repairPackageDbo.getId(), repairPackageDbo);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][][" + repairPackageDbo + "]");
            }
        }

        for (RepairDeviceDbo repairDeviceDbo : repairDevices) {
            Integer pid = repairDeviceDbo.getRepairPackageId();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][][pid:" + pid + "]");
            }
            RepairPackageDbo packageDbo = map.get(pid);
            if (null != packageDbo) {
                List<RepairDeviceDbo> repairDeviceDbos = packageDbo.getRepairDevices();
                if (null == repairDeviceDbos) {
                    repairDeviceDbos = new ArrayList<>();
                }
                repairDeviceDbos.add(repairDeviceDbo);
                packageDbo.setRepairDevices(repairDeviceDbos);
            }
        }

        map.clear();
        map = null;

        repairDevices.clear();
        repairDevices = null;

        return repairPackageDbos;
    }

    @Override
    public List<RepairPackageDbo> searchByExpressNumber(String expressNumber) {

        RepairPackageDbo repairPackageDbo = repairPackageService.getByExpressName(expressNumber);
        if (null == repairPackageDbo) {
            return null;
        }

        List<RepairDeviceDbo> repairDeviceDbos = repairDeviceService
                .getList4SearchByPacakgeId(repairPackageDbo.getId());
        repairPackageDbo.setRepairDevices(repairDeviceDbos);
        repairBackPackageService.setBackPackage(repairDeviceDbos);

        return Arrays.asList(repairPackageDbo);
    }

}
