package com.example.sunxiaodong.androidutils.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * SimpleDateFormat, Date, Calendar
 * Created by sunxiaodong on 16/7/12.
 */
public class TimeUtil {

    private static final String UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";//T代表后面跟着时间
    private static final String YYYY_MM_DD_FORMAT = "yyyy-MM-dd";

    public static String getYYYY_MM_DD(String utcStr) {
        String yyyy_mm_dd = "";
        SimpleDateFormat df = new SimpleDateFormat(UTC_FORMAT);
        Date date = null;
        try {
            date = df.parse(utcStr);
            long timeMillisecond = date.getTime();
            yyyy_mm_dd = new SimpleDateFormat(YYYY_MM_DD_FORMAT).format(timeMillisecond);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return yyyy_mm_dd;
    }

}
