package com.zjhz.teacher.utils;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-2
 * Time: 15:57
 * Description: Base64加密解密工具类
 */
public class Base64Util {


	public static String base64Encode(String strBase64,String code) throws UnsupportedEncodingException{
		String enToStr = Base64.encodeToString(strBase64.getBytes(), Base64.DEFAULT);
//		String string = new String(Base64.encode(strBase64.getBytes(code), Base64.DEFAULT));
		return enToStr;
	}
	

	public static String base64Decode(String str,String code) throws UnsupportedEncodingException{
		String string = new String(Base64.decode(str.getBytes(code), Base64.DEFAULT));
		return string;
	}


	public static byte[] bitmapToBytes(Bitmap bitmap){
		if (bitmap == null) return null;
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
		return os.toByteArray();
	}
}
