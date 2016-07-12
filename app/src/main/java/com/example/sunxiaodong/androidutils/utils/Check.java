package com.example.sunxiaodong.androidutils.utils;

import android.text.TextUtils;

import java.util.Collection;

/**
 * Created by sunxiaodong on 16/5/26.
 */
public class Check {

    public static void notNull(Object obj) {
        if (ConfigUtil.DEBUG) {
            if(obj == null) {
                throw new RuntimeException("notNull assert fail");
            }
        }
    }

    public static void notNull(Object obj, String msg) {
        if (ConfigUtil.DEBUG) {
            if(obj == null) {
                throw new RuntimeException(msg);
            }
        }
    }

    public static void notEmpty(CharSequence str) {
        if (ConfigUtil.DEBUG) {
            if(TextUtils.isEmpty(str)) {
                throw new RuntimeException("notEmpty assert fail");
            }
        }
    }

    public static void notEmpty(Collection c) {
        if (ConfigUtil.DEBUG) {
            if(c == null || c.isEmpty()) {
                throw new RuntimeException("notEmpty assert fail");
            }
        }

    }

    public static void beTrue(boolean b) {
        if (ConfigUtil.DEBUG) {
            if(!b) {
                throw new RuntimeException("beTrue assert fail");
            }
        }
    }

    public static void beFalse(boolean b) {
        if (ConfigUtil.DEBUG) {
            if(b) {
                throw new RuntimeException("beFalse assert fail");
            }
        }
    }

    public static void crash(Exception e) {
        throw new RuntimeException(e);
    }

    public static void equal(int origin, int expect) {
        if (ConfigUtil.DEBUG) {
            if(origin != expect) {
                throw new RuntimeException("" + origin + " not equal to " + expect);
            }
        }
    }

}
