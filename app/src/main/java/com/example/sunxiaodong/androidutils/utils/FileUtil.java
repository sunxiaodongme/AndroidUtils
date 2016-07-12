package com.example.sunxiaodong.androidutils.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by sunxiaodong on 16/6/24.
 */
public class FileUtil {

    /**
     * <br>功能简述:判断文件是否存在
     * @param fileName 文件名
     * @return
     */
    public static boolean isFileExist(String fileName) {
        boolean ret = false;
        try {
            File file = new File(fileName);
            ret = file.exists();
            file = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 创建文件夹
     * @param filePath
     */
    public static void createFilePath(String filePath){
        File destDir = new File(filePath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
    }

    /**
     * <br>功能简述:创建文件
     * @param fileName 文件名（包含路径）
     * @param replace 是否替换掉原有文件
     * @return 创建成功与否
     */
    public static boolean createFile(String fileName, boolean replace) {
        boolean ret = false;
        File newFile = new File(fileName);
        if (newFile.exists()) {
            if (replace) {
                if (fileName.endsWith(File.separator)) {
                    ret = true;
                } else {
                    if (newFile.delete()) {
                        //删除成功，再创建
                        try {
                            if (newFile.createNewFile()) {
                                ret = true;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                ret = true;
            }
        } else {
            if (fileName.endsWith(File.separator)) {
                newFile.mkdirs();
            } else {
                File parent = newFile.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
            }
            try {
                if (!newFile.exists()) {
                    if (newFile.createNewFile()) {
                        ret = true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        newFile = null;
        return ret;
    }

    /**
     * <br>功能简述:删除文件
     * @param imagePath
     */
    public static void deleteFile(String imagePath) {
        if (isFileExist(imagePath)) {
            File delFile = new File(imagePath);
            delFile.delete();
        }
    }

    /**
     * 获取文件
     * @param fileName
     * @return
     */
    public static File getFile(String fileName) {
        if (!isFileExist(fileName)) {
            return null;
        }
        File file = new File(fileName);
        return file;
    }

    /**
     * <br>功能简述:将图片写入文件
     * @param fileName 文件名
     * @param replace 是否执行替换
     * @param bitmap 图片
     * @return
     */
    public static File saveBitmapToFile(String fileName, boolean replace, Bitmap bitmap) {
        OutputStream os = null;
        File file = null;
        try {
            if (createFile(fileName, replace)) {
                file = new File(fileName);
            }
            if (file == null) {
                return file;
            }
            os = new FileOutputStream(file);
            if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)) {
                file = null;
            }
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
            file = null;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * <br>功能简述:从文件图片读到内存（并进行了防止爆内存处理）
     * @param fileName 文件名
     * @return 图片
     */
    public static Bitmap readBitmapFromFile(String fileName) {
        Bitmap bitmap = null;
        if (!isFileExist(fileName)) {
            return bitmap;
        }
        File file = new File(fileName);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = false;
        boolean outMemory = false;
        try {
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
            outMemory = false;
        } catch (OutOfMemoryError e) {
            outMemory = true;
            e.printStackTrace();
        }
        //如果由于爆内存而加载失败, 尝试进一步减小加载的图片大小
        while (null == bitmap && outMemory) {
            opts.inSampleSize = opts.inSampleSize + 1;
            try {
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
                outMemory = false;
            } catch (OutOfMemoryError e) {
                outMemory = true;
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    /**
     * <br>功能简述:保存图片带压缩
     * @param fileName
     * @param replace
     * @param quality
     * @param bitmap
     * @return
     */
    public static boolean saveBitmapToFileWithCps(String fileName, boolean replace, int quality,
                                                  Bitmap bitmap) {
        boolean ret = false;
        OutputStream os = null;
        try {
            File file = null;
            if (createFile(fileName, replace)) {
                file = new File(fileName);
            }
            if (file == null) {
                return ret;
            }
            os = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, quality, os)) {
                ret = true;
            }
            os.flush();
            file = null;
            ret = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

}
