package com.example.sunxiaodong.androidutils.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * <br>类描述:应用程序 工具
 * <br>功能详细描述:
 * 
 * @author  xiaodong
 * @date  [2015-10-17]
 */
public class AppUtil {



    public static boolean isValidContext(Context c) {
        Activity a = (Activity) c;
        return !a.isFinishing();
    }

    /**
     * 安装apk
     * @param filePath 文件路径
     */
    public static void installApkFile(Context context, String filePath) {
        if (null == context || null == filePath) {
            return;
        }
        File file = new File(filePath);
        if (file.exists()) {
            Uri uri = Uri.fromFile(file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
        }
    }

    public static boolean isShowingActivity(Context context, String activityName) {
        try {
            ActivityManager activityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
            if (tasksInfo.size() > 0) {
                // Activity位于堆栈的顶层,如果Activity的类为空则判断的是当前应用是否在前台
                ComponentName cn = tasksInfo.get(0).topActivity;
                String topActivityClsName = cn.getClassName();
                if (activityName.equals(topActivityClsName)) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }
    
    private static boolean isTopActivity(Context context, String packageName) {
        try {
            ActivityManager activityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
            if (tasksInfo.size() > 0) {
                // Activity位于堆栈的顶层,如果Activity的类为空则判断的是当前应用是否在前台
                if (packageName.equals(tasksInfo.get(0).topActivity.getPackageName())) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }
    
    private static boolean isForgroundApp(Context context, String pkgName) {
        // 获取当前正在运行进程列表
        try {
            ActivityManager activityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses(); 
            if (appProcesses == null) {
                return false; 
            }
            
            for (RunningAppProcessInfo appProcess : appProcesses) { 
                // 通过进程名及进程所用到的包名来进行查找
                if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    if (appProcess.processName.equals(pkgName)
                            || Arrays.asList(appProcess.pkgList).contains(pkgName)) {
                        return true; 
                    }
                } 
            } 
        } catch (Exception e) {
        }
        return false; 
    }
    
    /**
     * 判断当前程序是否运行于前台 
     */
    public static boolean isAppRunningInForground(Context context, String pkgName) {
        if (Build.VERSION.SDK_INT >= 21) {
            return isForgroundApp(context, pkgName);
        } else {
            return isTopActivity(context, pkgName);
        }
    }
    
    /**
     * 判断是否处于锁屏状态 
     */
    public static boolean isScreenLocked(Context context) {
        KeyguardManager mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        return mKeyguardManager.inKeyguardRestrictedInputMode();
    }
    
    /**
     * <br>功能简述:获取当前应用包名
     */
    public static String getPackageName(Context context) {
        return context.getPackageName();
    }
    
    /**
     * 获取元数据值
     * @param metaKey
     */
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return apiKey;
    }
    
    /**
     * <br>功能简述:获得当前进程号
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
    
    public static Bitmap getAppIcon(Context context) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable)context.getApplicationInfo().loadIcon(context.getPackageManager());
        Bitmap appIcon = bitmapDrawable.getBitmap();
        return appIcon;
    }
    
    public static String getAppName(Context context) {
        String title = (String)context.getPackageManager().getApplicationLabel(context.getApplicationInfo());
        return title;
    }
}
