package com.zjhz.teacher.utils;

import android.util.Log;

import com.zjhz.teacher.BuildConfig;

import pro.log.XLog;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-2
 * Time: 15:57
 * Description:
 */
public class LogUtil {
    private LogUtil() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	public static boolean isDebug = BuildConfig.DEBUG;
	private static final String TAG = LogUtil.class.getSimpleName();

	public static void i(String msg) {
		if (isDebug)
			Log.i(TAG, msg);
	}

	public static void d(String msg) {
		if (isDebug)
			XLog.d(TAG, msg);
	}

	public static void e(String msg) {
		if (isDebug)
			XLog.e(TAG, msg);
	}

	public static void v(String msg) {
		if (isDebug)
			XLog.json(3, TAG,msg);
	}

	public static void i(String tag, String msg) {
		if (isDebug)
			Log.i(tag, msg);
	}

	public static void d(String tag, String msg) {
		if (isDebug)
			XLog.d(tag, msg);
	}

	public static void e(String tag, String msg) {
		if (isDebug)
			XLog.json(3, tag,msg);
	}

	public static void v(String tag, String msg) {
		if (isDebug)
			Log.v(tag, msg);
	}

	public static void json(String msg) {
		if (isDebug)
			XLog.json(msg);
	}
	public static void json(String TAG ,String msg) {
		if (isDebug)
			XLog.json(Log.ERROR,TAG,msg);
	}
}
