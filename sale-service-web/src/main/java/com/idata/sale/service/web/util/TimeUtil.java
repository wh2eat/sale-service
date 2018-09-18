package com.idata.sale.service.web.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    public final static String yyyyMMdd = "yyyyMMdd";

    private final static DateFormat format_yyyyMMdd = new SimpleDateFormat(yyyyMMdd);

    public final static String _yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";

    private final static DateFormat format_yyyyMMddHHmmss = new SimpleDateFormat(_yyyyMMddHHmmss);

    public static String getNowTimeStr() {
        Date date = new Date(System.currentTimeMillis());
        try {
            synchronized (format_yyyyMMddHHmmss) {
                return format_yyyyMMddHHmmss.format(date);
            }
        }
        finally {
            date = null;
        }
    }

    public static String formatByYYYYmmDDhhMMSS(Date date) {
        synchronized (format_yyyyMMddHHmmss) {
            return format_yyyyMMddHHmmss.format(date);
        }
    }

    public static String formatByYYYYmmDD(Date date) {
        synchronized (format_yyyyMMdd) {
            return format_yyyyMMdd.format(date);
        }
    }

    public static String getNowByYmdFormat() {
        synchronized (format_yyyyMMdd) {
            return format_yyyyMMdd.format(new Date(System.currentTimeMillis()));
        }
    }

    public static Date getNowDate() {
        return new Date(System.currentTimeMillis());
    }

    public static Date parseDateByYYYYmmDDhhMMSS(String dateStr) {
        synchronized (format_yyyyMMddHHmmss) {
            try {
                return format_yyyyMMddHHmmss.parse(dateStr);
            }
            catch (ParseException e) {
                return null;
            }
        }

    }

}
