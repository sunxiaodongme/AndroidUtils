package com.example.sunxiaodong.androidutils.utils;

import android.media.ExifInterface;

import java.io.IOException;

/**
 * 
 * <br>类描述:相机工具类
 * <br>功能详细描述:
 * 
 * @author  xiaodong
 * @date  [2015-4-13]
 */
public class CameraUtil {

    /**
     * <br>功能简述:获取照片角度的
     * @param path 照片文件路径
     * @return 角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90 :
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180 :
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270 :
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
    
}
