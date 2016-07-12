package com.example.sunxiaodong.androidutils.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

/**
 * 
 * <br>类描述: i/o工具类
 * <br>功能详细描述:
 * 
 * @author  xiaodong
 * @date  [2015-3-16]
 */
public class IOUtil {

    //输入流转换成字节数组
    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }
    
    public static int copy(InputStream input, OutputStream output)
            throws IOException {
        byte[] buffer = new byte[1024 * 4];
        int count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
    
    public static byte[] ungzip(byte[] bs) throws Exception {
        GZIPInputStream gzin = null;
        ByteArrayInputStream bin = null;
        try {
            bin = new ByteArrayInputStream(bs);
            gzin = new GZIPInputStream(bin);
            return toByteArray(gzin);
        } catch (Exception e) {
            throw e;
        } finally {
            if (bin != null) {
                bin.close();
            }
            if (gzin != null) {
                gzin.close();
            }
        }
    }
    
    /**
     * <br>功能简述:将输入流转换为字符串
     * @param is
     * @param charset
     */
    public static String getStringFromInputStream(InputStream is, String charset) {
        StringBuilder builder = new StringBuilder();
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        try {
            if (charset == null) {
                inputStreamReader = new InputStreamReader(is);
            } else {
                inputStreamReader = new InputStreamReader(is, charset);
            }
            reader = new BufferedReader(inputStreamReader);
            //一次只申请少量内存，避免一次性加载完，导致OOM
            int bufferLength;
            char[] tmpBuffer = new char[1024 * 4];
            while ((bufferLength = reader.read(tmpBuffer)) != -1) {
                builder.append(tmpBuffer, 0, bufferLength);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();
    }
    
}
