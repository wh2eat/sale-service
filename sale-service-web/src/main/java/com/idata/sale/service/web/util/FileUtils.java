package com.idata.sale.service.web.util;

import java.io.File;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    public final static String getRandomFileName() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("\\{", "").replaceAll("\\}", "").replaceAll("-", "");
    }

    public final static String getFileSuffix(String path) {

        int idx = path.lastIndexOf(".");
        if (idx == -1) {
            return "";
        }
        return path.substring(idx, path.length()).trim();
    }

    public final static void rm(String path) {

        if (StringUtils.isEmpty(path)) {
            return;
        }

        File file = new File(path);
        if (file.exists()) {
            file.delete();
            LOGGER.info("[][rm][success,file:" + path + "]");
        }
    }

    public static void main(String[] args) {
        String path = "d:\\sss.xls";
        System.out.println(getFileSuffix(path));
    }

}
