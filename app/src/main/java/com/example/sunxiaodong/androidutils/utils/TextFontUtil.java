package com.example.sunxiaodong.androidutils.utils;

import android.graphics.Paint;

/**
 * 文本字体处理工具
 * Created by sunxiaodong on 16/6/8.
 */
public class TextFontUtil {

    /**
     * 获取文本符号绘制高度
     * @param textSize px
     * @return
     */
    public static float getTextFontSymbolHeight(int textSize) {
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return Math.abs(fontMetrics.ascent - fontMetrics.top);
    }

}
