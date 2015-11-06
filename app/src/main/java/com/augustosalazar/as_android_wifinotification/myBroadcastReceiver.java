package com.augustosalazar.as_android_wifinotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class myBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "WifiNotification";

    public static String getNetworkTypeName(int type) {
        switch (type) {
            case ConnectivityManager.TYPE_MOBILE:
                return "MOBILE";
            case ConnectivityManager.TYPE_WIFI:
                return "WIFI";
            case ConnectivityManager.TYPE_MOBILE_MMS:
                return "MOBILE_MMS";
            case ConnectivityManager.TYPE_MOBILE_SUPL:
                return "MOBILE_SUPL";
            case ConnectivityManager.TYPE_MOBILE_DUN:
                return "MOBILE_DUN";
            case ConnectivityManager.TYPE_MOBILE_HIPRI:
                return "MOBILE_HIPRI";
            case ConnectivityManager.TYPE_WIMAX:
                return "WIMAX";
            case ConnectivityManager.TYPE_BLUETOOTH:
                return "BLUETOOTH";
            case ConnectivityManager.TYPE_DUMMY:
                return "DUMMY";
            case ConnectivityManager.TYPE_ETHERNET:
                return "ETHERNET";
            case ConnectivityManager.TYPE_VPN:
                return "VPN";
            default:
                return Integer.toString(type);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);

        NetworkInfo networkInfo = intent
                .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if (networkInfo != null) {
            Log.d(TAG, "Type : " + getNetworkTypeName(networkInfo.getType())
                    + " State : " + networkInfo.getState());


            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                Log.d(TAG, "Type is wifi ");
                //get the different network states
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    Log.d(TAG, "Type is wifi amd conneced");
                    context.stopService(new Intent(context, UploadService.class));
                    Intent serviceIntent = new Intent(context, UploadService.class);
                    context.startService(serviceIntent);
                }
            }
        }
    }
}