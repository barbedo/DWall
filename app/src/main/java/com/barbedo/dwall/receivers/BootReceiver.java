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

import com.barbedo.dwall.data.DWallApplication;
import com.barbedo.dwall.data.Wallpaper;
import com.barbedo.dwall.data.WallpaperData;
import com.barbedo.dwall.services.TimeService;

import java.util.List;

/**
 * Receiver that rearms all the alarms on boot.
 */
public class BootReceiver extends BroadcastReceiver {

    private final static String TAG = "BootReceiver";

    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        DWallApplication application = (DWallApplication) context.getApplicationContext();
        WallpaperData wallpaperData = application.getWallpaperData();
        List<Wallpaper> wallpaperList = wallpaperData.getWallpaperList();

        for (Wallpaper wallpaper : wallpaperList) {
            if (wallpaper.getInfo().equals("Time")) {
                Intent serviceIntent = new Intent(context, TimeService.class);
                serviceIntent.setAction(TimeService.ACTION_SET_ALARM);
                serviceIntent.putExtra(TimeService.EXTRA_INFO, wallpaper.getInfo());
                serviceIntent.putExtra(TimeService.EXTRA_FILENAME, wallpaper.getFilename());
                context.startService(serviceIntent);
            }
        }

    }
}
