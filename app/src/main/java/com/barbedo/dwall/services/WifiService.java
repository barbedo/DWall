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

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class WifiService extends IntentService {

    private static final String TAG = "WifiService";

    public WifiService() {
        super("WifiService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        // Wait 500ms for the SSID to resolve
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DWallApplication application = (DWallApplication) getApplication();
        WallpaperData wallpaperData = application.getWallpaperData();
        List<Wallpaper> activeList = wallpaperData.getActiveWallpaperList(this);
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        String currentName = wifiManager.getConnectionInfo().getSSID().replace("\"", "");

        Log.d(TAG, "Wi-Fi name: " + currentName);

        WallpaperManager wallpaperManager
                = WallpaperManager.getInstance(getApplicationContext());

        if (activeList.size() > 0) {

            Log.d(TAG, "Priority: " + activeList.get(0).toString());

            if (activeList.get(0).getMode().equals("Wi-Fi") &&
                    activeList.get(0).getInfo().equals(currentName)) {

                // Sets the wallpaper
                File file = getFileStreamPath(activeList.get(0).getFilename());

                Bitmap wallpaperImage = BitmapFactory.decodeFile(file.getPath());

                try {
                    wallpaperManager.setBitmap(wallpaperImage);
                } catch (IOException e){
                    e.printStackTrace();
                }

                Log.d(TAG, activeList.get(0).toString() + " set");


            } else {
                // sets the first one on the list
            }
        } else {

            // Sets the default wallpaper
            File file = getFileStreamPath("default");
            Bitmap wallpaperImage = BitmapFactory.decodeFile(file.getPath());
            try {
                wallpaperManager.setBitmap(wallpaperImage);
            } catch (IOException e){
                e.printStackTrace();
            }
            Log.d(TAG, "Default wallpaper set");
        }
    }
}
