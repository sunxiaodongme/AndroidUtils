package com.example.sunxiaodong.androidutils.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * 
 * <br>类描述:网络相关工具类
 * 
 * @author  xiaodong
 * @date  [2015-3-23]
 */
public class NetworkUtil {

    public static final int NETWORK_TYPE_UNKOWN = 0x00; // 未知网络
    public static final int NETWORK_TYPE_WIFI = 0x01; // wifi网络
    public static final int NETWORK_TYPE_2G = 0x02; // 2G网络
    public static final int NETWORK_TYPE_3G = 0x03; // 3G网络
    public static final int NETWORK_TYPE_4G = 0x04; // 4G网络
    public static final int NETWORK_TYPE_OTHER = 0x05; // 其他网络，如热点、代理、以太网等
    
    /**
     * 判断当前网络是否可以使用
     */
    public static boolean isNetworkOK(Context context) {
        boolean result = false;
        if (context != null) {
            try {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (cm != null) {
                    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isAvailable()) {
                        result = true;
                    }
                }
            } catch (NoSuchFieldError e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    
    public static int getNetworkType(Context context) {
        int type = NETWORK_TYPE_UNKOWN;
        ConnectivityManager connectMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if (info != null) {
            switch (info.getType()) {
            case ConnectivityManager.TYPE_WIFI: // 1
                type = NETWORK_TYPE_WIFI;
                break;
            case ConnectivityManager.TYPE_MOBILE: // 0
                switch (info.getSubtype()) {
                // 2G网络
                case TelephonyManager.NETWORK_TYPE_GPRS: // 1，移动和联通2G
                case TelephonyManager.NETWORK_TYPE_EDGE: // 2，移动和联通2G
                case TelephonyManager.NETWORK_TYPE_CDMA: // 4，电信2G
                case TelephonyManager.NETWORK_TYPE_1xRTT: // 7，电信2G
                    type = NETWORK_TYPE_2G;
                    break;
                // 3G网络
                case TelephonyManager.NETWORK_TYPE_UMTS: // 3，联通3G
                case TelephonyManager.NETWORK_TYPE_EVDO_0: // 5，电信3G
                case TelephonyManager.NETWORK_TYPE_EVDO_A: // 6，电信3G
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // 12，电信3G
                case TelephonyManager.NETWORK_TYPE_HSDPA: // 8，联通3G
                case TelephonyManager.NETWORK_TYPE_HSUPA: // 9，HSUPA
                case TelephonyManager.NETWORK_TYPE_HSPA: // 10，HSPA
                case TelephonyManager.NETWORK_TYPE_IDEN: // 11，IDEN，集成数字增强型网络
                case TelephonyManager.NETWORK_TYPE_EHRPD: // 14，3G网络
                case TelephonyManager.NETWORK_TYPE_HSPAP: // 15，HSPAP，联通3.5G
                    type = NETWORK_TYPE_3G;
                    break;
                // 4G网络
                case TelephonyManager.NETWORK_TYPE_LTE: // 13，4G网络
                    type = NETWORK_TYPE_4G;
                    break;
                case TelephonyManager.NETWORK_TYPE_UNKNOWN: // 0，未知网络
                    type = NETWORK_TYPE_UNKOWN;
                    break;
                default:
                    type = NETWORK_TYPE_UNKOWN;
                    break;
                }
                break;
            case ConnectivityManager.TYPE_MOBILE_MMS: // 2，运营商的多媒体消息服务
            case ConnectivityManager.TYPE_MOBILE_SUPL: // 3，平面定位特定移动数据连接
            case ConnectivityManager.TYPE_MOBILE_DUN: // 4，运营商热点网络
            case ConnectivityManager.TYPE_MOBILE_HIPRI: // 5，高优先级的移动数据连接。
            case ConnectivityManager.TYPE_WIMAX: // 6，WIMAX网络
            case ConnectivityManager.TYPE_BLUETOOTH: // 7，蓝牙连接
            case ConnectivityManager.TYPE_DUMMY: // 8，虚拟连接
            case ConnectivityManager.TYPE_ETHERNET: // 9，以太网
                type = NETWORK_TYPE_OTHER;
                break;
            default:
                type = NETWORK_TYPE_UNKOWN;
                break;
            }
        }
        return type;
    }
    
    public static boolean isWifi(Context context) {
        boolean isWifi = false;
        int mobileNetworkType = NetworkUtil.getNetworkType(context);
        if (mobileNetworkType == NetworkUtil.NETWORK_TYPE_WIFI) {
            isWifi = true;
        }
        return isWifi;
    }
    
}
