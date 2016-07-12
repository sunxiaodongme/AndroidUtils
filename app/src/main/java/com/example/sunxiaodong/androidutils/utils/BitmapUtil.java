package com.example.sunxiaodong.androidutils.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <br>类描述:Bitmap操作处理类
 * <br>功能详细描述:
 *
 * @author xiaodong
 * @date [2014-11-13]
 */
public class BitmapUtil {

    /**
     * resource - > Bitmap
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap resourceToBitmap(Context context, int resId) {
        Resources res = context.getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(res, resId);
        return bitmap;
    }

    /**
     * 将drawable转成bitmap
     */
    public static Bitmap drawableToBitmap(Context context, Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * <br>功能简述: 旋转图片
     *
     * @param bitmap
     * @param degrees
     * @return 旋转好的图片
     */
    public static Bitmap rotate(Bitmap bitmap, int degrees) {
        if (degrees == 0) {
            return bitmap;
        }
        if (degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth(), (float) bitmap.getHeight());
            try {
                Bitmap b = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                        bitmap.getHeight(), m, true);
                if (bitmap != b) {
                    bitmap.recycle(); // Android开发网再次提示Bitmap操作完应该显示的释放
                    bitmap = b;
                }
            } catch (OutOfMemoryError ex) {
                // Android123建议大家如何出现了内存不足异常，最好return 原始的bitmap对象。.
            }
        }
        return bitmap;
    }

    public static Bitmap rotate(Bitmap b, int degreesX, int degreesY, int degressZ) {
        // 开始处理图像
        // 1.获取处理矩阵
        // 记录一下初始状态。save()和restore()可以将图像过渡得柔和一些。
        // Each save should be balanced with a call to restore().
        Camera camera = new Camera();
        camera.save();
        Matrix matrix = new Matrix();
        // rotate
        camera.rotateY(degreesY);
        camera.getMatrix(matrix);
        // 恢复到之前的初始状态。
        camera.restore();
        // 设置图像处理的中心点
//        matrix.preTranslate(b.getWidth() >> 1, b.getHeight() >> 1);
//        matrix.preSkew(skewX, skewY);
        // matrix.postSkew(skewX, skewY);
        // 直接setSkew()，则前面处理的rotate()、translate()等等都将无效。
        // matrix.setSkew(skewX, skewY);
        // 2.通过矩阵生成新图像(或直接作用于Canvas)
        Bitmap newBit = null;
        try {
            // 经过矩阵转换后的图像宽高有可能不大于0，此时会抛出IllegalArgumentException
            newBit = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        }
        if (newBit != null) {
            return newBit;
        }
        return null;
    }

    /**
     * <br>功能简述:生成圆角图
     *
     * @param bitmap
     * @param pixels
     * @return
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_4444);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * <br>功能简述:bitmap转换成字节数组
     *
     * @param bmp
     * @param needRecycle
     */
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.PNG, 100, output);
//        if (needRecycle) {
//            bmp.recycle();
//        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    //================================================生成图片缩略图=================================start

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * <br>功能简述:按比例计算缩放图片参数
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @param isExceed
     * @return
     */
    private static int calculateInScaleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight, boolean isExceed) {
        final float outHeight = options.outHeight;
        final float outWidth = options.outWidth;
        final float heightSampleSize = outHeight / reqHeight;
        final float widthSampleSize = outWidth / reqWidth;
        int inSampleSize = 1;
        if (outHeight > reqHeight || outWidth > reqWidth) {
            if (isExceed) {
//              inSampleSize = Math.round(Math.min(heightSampleSize, widthSampleSize));
                inSampleSize = (int) (Math.min(heightSampleSize, widthSampleSize) + 0.5f);
            } else {
//              inSampleSize = Math.round(Math.max(heightSampleSize, widthSampleSize));
                inSampleSize = (int) (Math.max(heightSampleSize, widthSampleSize) + 0.5f);
            }
        }
        return inSampleSize;
    }

    // 如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影响
    private static Bitmap createScaleBitmap(Bitmap src, int dstWidth, int dstHeight) {
        Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
        if (src != dst) { // 如果没有缩放，那么不回收
            src.recycle(); // 释放Bitmap的native像素数组
        }
        return dst;
    }

    // 从Resources中加载图片
    public static Bitmap decodeSampledBitmapFromResource(Resources res,
                                                         int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options); // 读取图片长款
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight); // 计算inSampleSize
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeResource(res, resId, options); // 载入一个稍大的缩略图
        return createScaleBitmap(src, reqWidth, reqHeight); // 进一步得到目标大小的缩略图
    }

    /**
     * <br>功能简述:从sd卡上加载缩略图，获得输入尺寸大小图片
     */
    public static Bitmap getThumbImg(String pathName, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        boolean outMemory = false;
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(pathName, options);
            outMemory = false;
        } catch (OutOfMemoryError e) {
            outMemory = true;
            e.printStackTrace();
        }
        // XXX 如果由于爆内存而加载失败, 尝试进一步减小加载的图片大小
        while (null == bitmap && outMemory) {
            options.inSampleSize = options.inSampleSize + 1;
            try {
                bitmap = BitmapFactory.decodeFile(pathName, options);
                outMemory = false;
            } catch (OutOfMemoryError e) {
                outMemory = true;
                e.printStackTrace();
            }
        }
        return createScaleBitmap(bitmap, reqWidth, reqHeight);
    }

    /**
     * <br>功能简述::从sd卡上加载缩略图,按原图尺寸进行缩放
     *
     * @param isExceed 加载的图片的某一边是否可以大于视图区域的某一边.
     *                 如果为true, 那么对于高宽比例比较极端的图片加载时很大可能是以原图大小加载了,在当前应用程序环境下，填写false
     * @return
     */
    public static Bitmap getThumbImg(String pathName, int reqWidth, int reqHeight, boolean isExceed) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options.inSampleSize = calculateInScaleSize(options, reqWidth, reqHeight, isExceed);
        options.inJustDecodeBounds = false;
        boolean outMemory = false;
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(pathName, options);
            outMemory = false;
        } catch (OutOfMemoryError e) {
            outMemory = true;
            e.printStackTrace();
        }
        // XXX 如果由于爆内存而加载失败, 尝试进一步减小加载的图片大小
        while (null == bitmap && outMemory) {
            options.inSampleSize = options.inSampleSize + 1;
            try {
                bitmap = BitmapFactory.decodeFile(pathName, options);
                outMemory = false;
            } catch (OutOfMemoryError e) {
                outMemory = true;
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    //================================================生成图片缩略图=================================end
    
   /* public static Bitmap mergeBitmap(Bitmap bg, Bitmap secondBitmap, float secondX, float secondY) {
        Bitmap bitmap = Bitmap.createBitmap(firstBitmap.getWidth(), firstBitmap.getHeight(),
                firstBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(secondBitmap, secondX, secondY, null);
        canvas.drawBitmap(firstBitmap, new Matrix(), null);
        Paint p = new Paint();
        int color = Color.parseColor("#AARRGGBB");
        p.setColor(color);
//        p.setTypeface(font);  
        p.setAntiAlias(true);//去除锯齿  
        p.setFilterBitmap(true);//对位图进行滤波处理  
        p.setTextSize(scalaFonts(size));  
        float tX = (x - getFontlength(p, mString))/2;  
        float tY = (y - getFontHeight(p))/2+getFontLeading(p);    
        canvas.drawText(mString,tX,tY,p);
        return bitmap;
    }
    
    *//**
     * @return 返回指定笔和指定字符串的长度
     *//*  
    public static float getFontlength(Paint paint, String str) {  
        return paint.measureText(str);  
    }  
    *//**
     * @return 返回指定笔的文字高度
     *//*  
    public static float getFontHeight(Paint paint)  {    
        FontMetrics fm = paint.getFontMetrics();   
        return fm.descent - fm.ascent;    
    }   
    */

    /**
     * @return 返回指定笔离文字顶部的基准距离
     *//*  
    public static float getFontLeading(Paint paint)  {    
        FontMetrics fm = paint.getFontMetrics();   
        return fm.leading- fm.ascent;    
    }   
    
    public static Bitmap mergeBitmap(Bitmap firstBitmap, Bitmap secondBitmap, float secondX, float secondY, String text, float textX, float textY, float textSize) {
        Bitmap bitmap = Bitmap.createBitmap(firstBitmap.getWidth(), firstBitmap.getHeight(),
                firstBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(secondBitmap, secondX, secondY, null);
        canvas.drawBitmap(firstBitmap, new Matrix(), null);
        return bitmap;
    }*/
    public static Bitmap getScaleBitmap(Bitmap bitmap, int reqWidth, int reqHeight) {
        //获取这个图片的宽和高 
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        //计算缩放率，新尺寸除原始尺寸 
        float scaleWidth = ((float) reqWidth) / width;
        float scaleHeight = ((float) reqHeight) / height;

        // 创建操作图片用的matrix对象 
        Matrix matrix = new Matrix();
        // 缩放图片动作 
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = null;
        try {
            // 创建新的图片 
            resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                    matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return resizedBitmap != null ? resizedBitmap : bitmap;
    }

    public static Bitmap getBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Config.ARGB_4444);
        //利用bitmap生成画布    
        Canvas canvas = new Canvas(bitmap);
        //把view中的内容绘制在画布上    
        view.draw(canvas);
        return bitmap;
    }

    public static Bitmap getBitmapNoOutMem(String pathName) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        boolean outMemory = false;
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(pathName, options);
            outMemory = false;
        } catch (OutOfMemoryError e) {
            outMemory = true;
            e.printStackTrace();
        }
        // XXX 如果由于爆内存而加载失败, 尝试进一步减小加载的图片大小
        while (null == bitmap && outMemory) {
            options.inSampleSize = options.inSampleSize + 1;
            try {
                bitmap = BitmapFactory.decodeFile(pathName, options);
                outMemory = false;
            } catch (OutOfMemoryError e) {
                outMemory = true;
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    /**
     * <br>功能简述:压缩bitmap防止outofmemory
     *
     * @param image
     * @return
     */
    public static Bitmap getBitmapNoOutMem(Bitmap bitmap) {
        Bitmap retBmp = null;
        if (bitmap == null) {
            return retBmp;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        boolean outMemory = false;
        try {
            retBmp = BitmapFactory.decodeStream(isBm, null, options);//把ByteArrayInputStream数据生成图片  
            outMemory = false;
        } catch (OutOfMemoryError e) {
            outMemory = true;
            e.printStackTrace();
        }
        while (outMemory) {
            options.inSampleSize = options.inSampleSize + 1;
            try {
                retBmp = BitmapFactory.decodeStream(isBm, null, options);//把ByteArrayInputStream数据生成图片  
                outMemory = false;
            } catch (OutOfMemoryError e) {
                outMemory = true;
                e.printStackTrace();
            }
        }
        return retBmp;
    }

    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /**
     * <br>功能简述:获取未经旋转的图片
     * <br>功能详细描述:
     *
     * @param imgPath
     * @throws IOException
     */
    public static Bitmap getNoRotateBitmap(String imgPath) throws IOException {
        Bitmap bitmap = null;
        if (TextUtils.isEmpty(imgPath)) {
            return bitmap;
        }
        int degrees = 0;
        if (FileUtil.isFileExist(imgPath)) {
            degrees = CameraUtil.readPictureDegree(imgPath);
        }
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(imgPath)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        while (true) {
            if ((options.outWidth >> i <= 1000) && (options.outHeight >> i <= 1000)) {
                in = new BufferedInputStream(new FileInputStream(new File(imgPath)));
                options.inSampleSize = (int) Math.pow(2.5D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        if (degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth(), (float) bitmap.getHeight());
            try {
                Bitmap b = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                        bitmap.getHeight(), m, true);
                if (bitmap != b) {
                    bitmap.recycle(); // Android开发网再次提示Bitmap操作完应该显示的释放
                    bitmap = b;
                }
            } catch (OutOfMemoryError ex) {
                // Android123建议大家如何出现了内存不足异常，最好return 原始的bitmap对象。.
            }
        }
        return bitmap;
    }

    // 圆角图片
    public static Bitmap ToCircularCorner(Bitmap bitmap, int i) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff336699;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = i;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    // 圆角图片
    public static Bitmap ToCircular(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff000000;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getWidth());
        final RectF rectF = new RectF(rect);

        final float roundPx = (bitmap.getWidth()) / 10;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setColor(color);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        // paint.setStyle(Style.STROKE);
        // paint.setStrokeWidth(2);
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    // 圆形图片
    public static Bitmap ToCircularBig(Bitmap bitmap) {
        int bitmap_width = bitmap.getWidth();
        Bitmap output = Bitmap.createBitmap(bitmap_width, bitmap_width, Config.ARGB_4444);
        Canvas canvas = new Canvas(output);
        final int color = 0xff000000;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap_width, bitmap_width);
        final RectF rectF = new RectF(rect);

        final float roundPx = bitmap_width / 2;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setColor(color);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        // paint.setStyle(Style.STROKE);
        // paint.setStrokeWidth(2);
        canvas.drawBitmap(bitmap, rect, rect, paint);

        paint.setColor(0xffffffff);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawCircle(bitmap_width / 2, bitmap_width / 2, bitmap_width / 2 - 5, paint);// 描白边
        return output;
    }

    public static Drawable toCircularDrawable(Bitmap bitmap) {
        BitmapDrawable drawable = new BitmapDrawable(null, ToCircular(bitmap));
        return drawable;
    }

    public static Drawable toCircularBigDrawable(Bitmap bitmap) {
        BitmapDrawable drawable = new BitmapDrawable(null, ToCircularBig(bitmap));
        return drawable;
    }

    // drawable-->bitmap
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888 : Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    // 加倒影
    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        final int reflectionGap = 0;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2, width, height / 2, matrix, false);

        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 2), Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0, bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);

        return bitmapWithReflection;
    }

    public static Bitmap myImg(Bitmap bitmap, int i) {
        return createReflectionImageWithOrigin(ToCircularCorner(bitmap, i));
    }

    private static Bitmap doBlur(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {
        if (radius < 1) {
            return (null);
        }

        int w = sentBitmap.getWidth();
        int h = sentBitmap.getHeight();

        int[] pix = new int[w * h];
        sentBitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        sentBitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (sentBitmap);
    }

    public static Bitmap getBlurBitmap(Bitmap loadedBitmap, int blurFactor) {
        Bitmap bitmap = null;
        try {
            loadedBitmap = loadedBitmap.copy(loadedBitmap.getConfig(), true);
            bitmap = doBlur(loadedBitmap, blurFactor, true);
        } catch (OutOfMemoryError ex) {
            bitmap = loadedBitmap;
        }

        return bitmap;
    }

}
