package com.why.week7demo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by my on 2016/11/15.
 */

public class NetWorkUtils {
    public static boolean isConnected(Context context){

        ConnectivityManager connectivityManager = (ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE);

        //获取当前活跃网络
        NetworkInfo activityNetWorkInfo =
                connectivityManager.getActiveNetworkInfo();

        if (activityNetWorkInfo == null) {
            return false;
        }
        switch (activityNetWorkInfo.getType()){
            case ConnectivityManager.TYPE_MOBILE:
                Toast.makeText(context, "当前网络是移动网络", Toast.LENGTH_LONG).show();
                return true;
            case ConnectivityManager.TYPE_WIFI:
                Toast.makeText(context, "当前网络是WIFI", Toast.LENGTH_LONG).show();
                return true;
        }

        return false;
    }
}
