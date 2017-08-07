package com.zjhz.teacher.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-08-09
 * Time: 15:57
 * Description:
 */
public class NetWorkUtils {
	
	private Context mContext;
	private ConnectivityManager connectivityManager;
	private NetworkInfo networkInfo;
	
	public NetWorkUtils(Context context) {
		this.mContext = context;
		connectivityManager = (ConnectivityManager) this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		networkInfo = connectivityManager.getActiveNetworkInfo();
	}
	
	public boolean isConnected(){
		return networkInfo != null && networkInfo.isConnected();				
	}

	/**
	 * 
	 * @return 0: 无网络， 1:WIFI， 2:其他（流量）
	 */
	public int getNetType(){
		if (!isConnected()) {
			return 0;
		}
		int type = networkInfo.getType();
		if (type == ConnectivityManager.TYPE_WIFI) {
			return 1;
		}else {
			return 2;
		} 					
	}

}