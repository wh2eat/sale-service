package com.idata.sale.service.web.util;

import java.util.UUID;

public class FileUtils {

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

    public static void main(String[] args) {
        String path = "d:\\sss.xls";
        System.out.println(getFileSuffix(path));
    }

}
