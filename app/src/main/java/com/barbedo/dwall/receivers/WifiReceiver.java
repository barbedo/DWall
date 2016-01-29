/**
 * Copyright 2016 Ricardo Barbedo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.barbedo.dwall.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.barbedo.dwall.services.WifiService;

/**
 * Receiver that listens to the wifi state activity and launches the WifiService.
 * This receiver filters the CONNECTIVITY_CHANGE and the WIFI_STATE_CHANGED broadcasts to see
 * if the network is down or if it was changed and launches the WifiService to change the wallpaper.
 *
 * @author Ricardo Barbedo
 */
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

            Log.d(TAG, "NetworkInfo null: " + String.valueOf(connectivityManager.
                    getActiveNetworkInfo() == null));

            if (connectivityManager.getActiveNetworkInfo() != null) {
                if (connectivityManager.getActiveNetworkInfo().getType() ==
                        ConnectivityManager.TYPE_WIFI) {
                    if (connectivityManager.getActiveNetworkInfo().isConnected() ||
                            connectivityManager.getActiveNetworkInfo().isFailover()) {
                        Log.d(TAG, "isConnected");
                        Intent intent = new Intent(context, WifiService.class);
                        context.startService(intent);
                    }
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
    }
}
