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

package com.barbedo.dwall.services;

import android.app.IntentService;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.barbedo.dwall.data.DWallApplication;
import com.barbedo.dwall.data.Wallpaper;
import com.barbedo.dwall.data.WallpaperData;
import com.barbedo.dwall.utils.WallpaperHelper;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Service responsible for setting the system wallpaper when the connection state is changed.
 * This intent service is launched by the WifiReceiver when the wifi state is changed and decides
 * what is the correct wallpaper to set.
 *
 * @author Ricardo Barbedo
 */
public class WifiService extends IntentService {

    private static final String TAG = "WifiService";

    public WifiService() {
        super("WifiService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        DWallApplication application = (DWallApplication) getApplication();
        WallpaperData wallpaperData = application.getWallpaperData();
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        String currentName = wifiManager.getConnectionInfo().getSSID().replace("\"", "");

        Log.d(TAG, "Wi-Fi name: " + currentName);

        // Sets default wallpaper if the current one is dismissed from the list
        List<Wallpaper> activeList = wallpaperData.getActiveWallpaperList(this);
        WallpaperHelper.setOrIgnoreWallpaper(this, activeList);
    }
}
