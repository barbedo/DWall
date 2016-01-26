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

package com.barbedo.dwall.data;

import android.app.Application;
import android.util.Log;

/**
 * Application object to allow all the components to share the same data
 */
public class DWallApplication extends Application {

    private static String TAG = DWallApplication.class.getSimpleName();

    private WallpaperData wallpaperData;

    @Override
    public void onCreate() {
        super.onCreate();
        wallpaperData = new WallpaperData(getApplicationContext());
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d(TAG, "onTerminate");
    }

    public WallpaperData getWallpaperData() {
        return wallpaperData;
    }

}