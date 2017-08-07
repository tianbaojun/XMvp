package com.zjhz.teacher.utils;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-08-09
 * Time: 15:57
 * Description:
 */
public class URLUtils {

	public static boolean isHttpUrl(String url) {
		return (null != url) && (url.length() > 6)
				&& url.substring(0, 7).equalsIgnoreCase("http://");
	}

	public static boolean isHttpsUrl(String url) {
		return (null != url) && (url.length() > 7)
				&& url.substring(0, 8).equalsIgnoreCase("https://");
	}

	public static boolean isNetworkUrl(String url) {
		if (url == null || url.length() == 0) {
			return false;
		}
		return isHttpUrl(url) || isHttpsUrl(url);
	}

}