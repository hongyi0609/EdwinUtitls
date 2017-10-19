package com.edwin.utils.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.edwin.utils.EdwinApplication;

/**
 * Created by Edwin on 2016/3/16 0016.
 */
public class NetWorkUtil {

    private NetWorkUtil() {}

    /**
     * 判断网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetWorkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * 检测wifi是否连接
     *
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 检测3G是否连接
     *
     * @return
     */
    public static boolean is3gConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        TelephonyManager telephonyManager = (TelephonyManager) EdwinApplication.Companion
                .getContext().getSystemService(
                        Context.TELEPHONY_SERVICE);
        int networkType = telephonyManager.getNetworkType(); // 判断网络类型：2g、3g、4g

        return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }

}
