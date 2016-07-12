package com.example.sunxiaodong.androidutils.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <br>类描述: md5散列化文本
 * 
 * @author  xiaodong
 * @date  [2015-3-16]
 */
public class MD5Util {

	public static String md5(String string) {
		byte[] hash;
		try {
		    /*LogUtils.v(LogUtils.LOG_TAG, "MD5Util--md5++MessageDigest:" + MessageDigest.getInstance("MD5"));
		    LogUtils.v(LogUtils.LOG_TAG, "MD5Util--md5++string:" + string);*/
			hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("oh, MD5 not be supported?", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("oh, UTF-8 should be supported?", e);
		}
		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10) {
				hex.append("0");
			}
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}
	
	public static String md5(byte[] hash) {
		byte[] md5;
		try {
		    /*LogUtils.v(LogUtils.LOG_TAG, "MD5Util--md5++MessageDigest:" + MessageDigest.getInstance("MD5"));
		    LogUtils.v(LogUtils.LOG_TAG, "MD5Util--md5++string:" + string);*/
			md5 = MessageDigest.getInstance("MD5").digest(hash);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("oh, MD5 not be supported?", e);
		}
		StringBuilder hex = new StringBuilder(md5.length * 2);
		for (byte b : md5) {
			if ((b & 0xFF) < 0x10) {
				hex.append("0");
			}
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}
	
}
