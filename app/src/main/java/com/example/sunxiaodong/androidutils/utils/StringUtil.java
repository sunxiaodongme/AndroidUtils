package com.example.sunxiaodong.androidutils.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * <br>类描述:字符串处理工具
 * 
 * @author  xiaodong
 * @date  [2015-3-24]
 */
public class StringUtil {

    /**
     * 将字符串转换为大写
     * @param obj 要转换的字符串
     * @return 转换后的字符串
     */
    public static String toUpperCase(Object obj) {
        if (obj == null) {
            obj = "";
        }
        return obj.toString().trim().toUpperCase();
    }
    /**
     * 将字符串转换为小写
     * @param obj 要转换的字符串
     * @return 转换后的字符串
     */
    public static String toLowerCase(Object obj) {
        if (obj == null) {
            obj = "";
        }
        return obj.toString().trim().toLowerCase();
    }
    /**
     * 将Object类型转换为Integer
     * @param srcStr
     * @param defaultValue
     * @return
     */
    public static Integer toInteger(Object srcStr, Integer defaultValue) {
        try {
            if (srcStr != null && StringUtil.isInt(srcStr)) {
                String s = srcStr.toString().replaceAll("(\\s)", "");
                return s.length() > 0 ? Integer.valueOf(s) : defaultValue;
            }
        } catch (Exception e) {

        }
        return defaultValue;
    }
    /**
     * 将Object类型转换为Long
     * @param srcStr
     * @param defaultValue
     * @return
     */
    public static Long toLong(Object srcStr, Long defaultValue) {
        try {
            if (srcStr != null && StringUtil.isInt(srcStr)) {
                String s = srcStr.toString().replaceAll("(\\s)", "");
                return s.length() > 0 ? Long.parseLong(s) : defaultValue;
            }
        } catch (Exception e) {

        }
        return defaultValue;
    }
    /**
     * 判断是否为数字
     * @param srcStr
     * @return
     */
    public static boolean isInt(Object srcStr) {
        if (srcStr == null) {
            return false;
        }
        String s = srcStr.toString().replaceAll("(\\s)", "");
        Pattern p = Pattern.compile("([-]?[\\d]+)");
        Matcher m = p.matcher(s);
        return m.matches();
    }
    /**
     * 将Object类型转换为String,且去除空格
     * @param obj
     * @return
     */
    public static String toString(Object obj) {
        if (obj == null) {
            obj = "";
        }
        return obj.toString().trim();
    }
    
    /**
     * <br>功能简述:整型转为字符串
     * @param i
     * @return
     */
    public static String toString(Integer i) {
        return String.valueOf(i);
    }
    
    public static double toDouble(String doubleStr) {
        double ret = 0;
        if (!TextUtils.isEmpty(doubleStr)) {
            try {
                ret = Double.parseDouble(doubleStr);
            } catch (Exception e) {
                ret = 0;
            }
        }
        return ret;
    }
    
    public static float toFloat(String floatStr) {
        float ret = 0;
        if (!TextUtils.isEmpty(floatStr)) {
            try {
                ret = Float.parseFloat(floatStr);
            } catch (Exception e) {
                ret = 0;
            }
        }
        return ret;
    }
    
    /**
     * <br>功能简述:获取两个字符串之间字符串的方法
     * @param string 待处理字符串
     * @param strStart 起始字符串
     * @param strEnd 结束字符串
     * @return
     */
    public static String getTwoStrBtwStr(String string, String strStart, String strEnd) {
        String regex = strStart + "(.*)" + strEnd;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        while (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }
    
}
