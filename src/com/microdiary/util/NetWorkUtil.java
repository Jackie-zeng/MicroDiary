package com.microdiary.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

public class NetWorkUtil {

	/**
	 * 判断是否联网
	 * 若有，返回true
	 * 否则, 返回false
	 * @param context
	 * @return
	 */
	public static boolean isConnect(Context context) {
		ConnectivityManager cwjManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo info = cwjManager.getActiveNetworkInfo(); 
		if (info != null && info.isAvailable()){ 
	        return true;
	    } 
	    else {
	        return false;
	    }
	}
	
	/**
	 * 判断是否连接wifi
	 * @param context
	 * @return
	 */
	public static boolean isConnectWifi(Context context) {
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if(wifi == State.CONNECTED||wifi==State.CONNECTING)
        	return true;
        return false;
	}
	
	/**
	 * 判断是否连接数据流量
	 * @param context
	 * @return
	 */
	public static boolean isConnectData(Context context) {
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        State mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if(mobile == State.CONNECTED||mobile==State.CONNECTING)
        	return true;
        return false;
	}
	
}
