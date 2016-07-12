package com.example.sunxiaodong.androidutils.utils;

import android.os.SystemClock;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

/**
 * 
 * <br>类描述:这个类用来记录点击事件的时间,用于判断是否产生了过快的点击
 * <br>功能详细描述:
 * 
 * @author  xiaodong
 * @date  [2015-6-18]
 */
public class QuickClickGuard {

    private final static String LOG_TAG = "QuickClickGuard";

    /**
     * 默认的快速点击定义的时间间隔值1000毫秒
     */
    public final static long DEFAULT_LIMIT_TIME = 400;
    public final static long LIMIT_TIME_1000 = 1000;
    
    private long mLastClickTime;//上次点击时间
    
    /**
     * 被定义为快速点击的时间间隔.<br>
     * 若两次点击之间时间小于这这值则认为是快速点击<br>
     * (单位毫秒)默认值 {@link #DEFAULT_LIMIT_TIME}
     */
    private long mLimitTime;

    /**
     * 被点击对象的点击时间
     */
    private SparseArray<Long> mClickTimes;

    public QuickClickGuard() {
        mClickTimes = new SparseArray<Long>();
        mLimitTime = DEFAULT_LIMIT_TIME;
    }

    /**
     * 被定义为快速点击的时间间隔.<br>
     * 若两次点击之间时间小于这这值则认为是快速点击<br>
     * (单位毫秒)默认值 {@link #DEFAULT_LIMIT_TIME}
     */
    public long getLimitTime() {
        return mLimitTime;
    }

    /**
     * 被定义为快速点击的时间间隔.<br>
     * 若两次点击之间时间小于这这值则认为是快速点击<br>
     * (单位毫秒)默认值 {@link #DEFAULT_LIMIT_TIME}
     */
    public void setLimitTime(long limitTime) {
        mLimitTime = limitTime;
    }

    /**
     * 判断一个对象是否被快速点击了.<br>
     * 这个方法必须在点击触发时调用,因为其中会记录当前点击的时间<br>
     * @param clickObjectId 在一定范围内一个对象的唯一id,通常对于view,使用view id作为clickObjectId是个不错的选择,其他情况hashCode是个不错的选择
     */
    public boolean isQuickClick(int clickObjectId) {
        boolean isQuickClick = false;
        final long current = SystemClock.elapsedRealtime();
        long last = mClickTimes.get(clickObjectId, Long.valueOf(0));
        if (last != 0 && current - last < mLimitTime) {
            isQuickClick = true;
            Log.d(LOG_TAG, "clickObjectId[" + clickObjectId + "]" + " is isQuickClick!");
        }
        mClickTimes.put(clickObjectId, current);
        return isQuickClick;
    }

    /**
     * 判断一个view是否被快速点击<br>
     * @param view
     * @return
     */
    public boolean isQuickClick(View view) {
        return isQuickClick(view.getId());
    }

    /**
     * <br>功能简述:针对整个页面双击的判断
     * @return
     */
    public boolean isQuickClick() {
        boolean ret = false;
        long currClickTime = System.currentTimeMillis();
        if (currClickTime - mLastClickTime < getLimitTime()) {
            ret = true;
        }
        mLastClickTime = currClickTime;
        return ret;
    }
    
    private static long lastClickTime;
    public static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 350) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
    
}
