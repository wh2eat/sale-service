package com.idata.sale.service.web.base.service;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.RandomStringUtils;

import com.idata.sale.service.web.util.TimeUtil;

public class SerialNumberUtil {

    private final static String type_repair_invoice = "I";

    private final static String type_repair_package = "P";

    private final static String type_repair_back_package = "B";

    public static String generateRepairBackPackageSerialNumber() {

        return generateSerialNumber(type_repair_back_package);
    }

    public static String generateRepairInvoiceSerialNumber() {

        return generateSerialNumber(type_repair_invoice);
    }

    public static String generateRepairPackageSerialNumber() {

        return generateSerialNumber(type_repair_package);

    }

    private static String generateSerialNumber(String typeStr) {

        return TimeUtil.formatByYYYYmmDD(new Date(System.currentTimeMillis())) + typeStr + getTimeSection()
                + RandomStringUtils.randomNumeric(4);

    }

    private static Calendar calendar = Calendar.getInstance();

    private static String getTimeSection() {
        int hours = -1;
        synchronized (calendar) {
            calendar.setTime(new Date(System.currentTimeMillis()));
            hours = calendar.get(Calendar.HOUR_OF_DAY);
        }

        if (hours >= 0 && hours <= 5) {
            return "M";
        }
        else if (hours >= 6 && hours <= 11) {
            return "A";
        }
        else if (hours >= 12 && hours <= 17) {
            return "P";
        }
        else if (hours >= 18 && hours <= 23) {
            return "N";
        }

        return "U";
    }

}
