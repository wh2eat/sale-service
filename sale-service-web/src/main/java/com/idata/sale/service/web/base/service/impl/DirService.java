package com.idata.sale.service.web.base.service.impl;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.idata.sale.service.web.base.service.IDirService;
import com.idata.sale.service.web.util.TimeUtil;

@Service
public class DirService implements IDirService {

    private final static String linux_splilter = "/";

    private final static String windows_splilter = "\\";

    private final static Logger LOGGER = LoggerFactory.getLogger(DirService.class);

    public DirService() {
    }

    @Value("${com.idt.ss.dir.export.repairDevice}")
    private String repairDeviceDirPath;

    private String repairDeviceLastInitDate = null;

    @Override
    public String getRepairDeviceRecordDir() {

        String nowDate = TimeUtil.getNowByYmdFormat();
        String dirPath = connectDir(repairDeviceDirPath, nowDate);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][][dirPath:" + dirPath + "]");
        }
        if (null == repairDeviceLastInitDate || !repairDeviceLastInitDate.equals(nowDate)) {
            File dirFile = new File(dirPath);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            repairDeviceLastInitDate = nowDate;
            return dirPath;
        }
        return dirPath;
    }

    public String connectDir(String baseDir, String connectName) {

        if (baseDir.endsWith(linux_splilter)) {
            return baseDir + connectName + linux_splilter;
        }
        else if (baseDir.endsWith(windows_splilter)) {
            return baseDir + connectName + windows_splilter;
        }
        else if (baseDir.lastIndexOf(linux_splilter) > -1) {
            return baseDir + linux_splilter + connectName + linux_splilter;
        }
        else if (baseDir.lastIndexOf(windows_splilter) > -1) {
            return baseDir + windows_splilter + connectName + windows_splilter;
        }

        return baseDir + linux_splilter + connectName + linux_splilter;
    }

    public String connectFileName(String baseDir, String fileName) {

        if (baseDir.endsWith(linux_splilter)) {
            return baseDir + fileName;
        }
        else if (baseDir.endsWith(windows_splilter)) {
            return baseDir + fileName;
        }
        else if (baseDir.lastIndexOf(linux_splilter) > -1) {
            return baseDir + linux_splilter + fileName;
        }
        else if (baseDir.lastIndexOf(windows_splilter) > -1) {
            return baseDir + windows_splilter + fileName;
        }

        return baseDir + linux_splilter + fileName;
    }

}
