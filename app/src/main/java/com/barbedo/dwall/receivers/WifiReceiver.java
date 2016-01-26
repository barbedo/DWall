package com.barbedo.dwall.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.barbedo.dwall.services.WifiService;

public class WifiReceiver extends BroadcastReceiver {

    private final static String TAG = "WifiReceiver";

    public WifiReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent callingIntent) {

        Log.d(TAG, "onReceive");


        if (callingIntent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connectivityManager.getActiveNetworkInfo() != null) {
                if (connectivityManager.getActiveNetworkInfo().isConnected()) {
                    Log.d(TAG, "isConnected");
                    Intent intent = new Intent(context, WifiService.class);
                    context.startService(intent);
                }
            }
        }

        if (callingIntent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
            int wifiState = callingIntent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);

            Log.d(TAG, "State changed: " + wifiState);

            if (wifiState == WifiManager.WIFI_STATE_DISABLED) {
                Log.d(TAG, "Wi-Fi disabled");
                Intent intent = new Intent(context, WifiService.class);
                context.startService(intent);
            }
        }

        //String bssid = callingIntent.getStringExtra(WifiManager.EXTRA_BSSID);

        //if (bssid != null) {
         //   if (!bssid.equals("bssid")) {
//                Log.d(TAG, "startService");
//                Intent intent = new Intent(context, WifiService.class);
//                context.startService(intent);
        //    }
        //}



//        ConnectivityManager connectivityManager =
//                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//
//        if (connectivityManager.getActiveNetworkInfo() != null) {
//            int type = connectivityManager.getActiveNetworkInfo().getType();
//
//            Log.d(TAG, "Intent received. Type: " + type);
//
//            if (type == ConnectivityManager.TYPE_WIFI) {
//                context.startService(intent);
//            }
//        } else {
//            if (firstConnect) {
//                Log.d(TAG, "No connectivity");
//                context.startService(intent);
//                firstConnect = false;
//            } else {
//                firstConnect = true;
//            }
//        }

    }
}
