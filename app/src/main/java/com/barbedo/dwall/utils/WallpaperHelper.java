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

package com.barbedo.dwall.utils;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.barbedo.dwall.R;
import com.barbedo.dwall.data.Wallpaper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


/**
 * A class containing the static helper methods that manipulates the system wallpaper.
 */
public class WallpaperHelper {

    private static final String TAG = WallpaperHelper.class.getSimpleName();

    // Size of the thumbnail in pixels
    private static final int THUMB_WIDTH = 108;
    private static final int THUMB_HEIGHT = 192;

    /**
     * Static method to copy the selected file to the internal storage.
     *
     * This method also creates a thumbnail of the image with "_th" appended to the end of the
     * name, so it can be set on the ImageView.
     *
     * @param context  The current context.
     * @param uri      The wallpaper containing the filename.
     * @param filename The name of the file to save.
     */
    public static void copyWallpaperToStorage(Context context, Uri uri, String filename) {
        // Copies to internal data
        try {
            InputStream input = context.getContentResolver().openInputStream(uri);
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int len = 0;
            while ((len = input.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            if (fos != null)
                fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        File filePath = context.getFileStreamPath(filename);
        Log.d(TAG, "Internal filepath: " + filePath.toString());

        // Create a thumbnail
        Bitmap thumbnail = ThumbnailUtils.extractThumbnail(BitmapFactory.
                decodeFile(filePath.getAbsolutePath()), THUMB_WIDTH, THUMB_HEIGHT);
        try {
            FileOutputStream fos =
                    context.openFileOutput(filename + "_th", Context.MODE_PRIVATE);
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @param wallpaper Deletes the wallpaper file and its thumbnail
     */
    public static void deleteWallpaper(Context context, Wallpaper wallpaper) {
        if (context.deleteFile(wallpaper.getFilename()) &&
                context.deleteFile(wallpaper.getFilename() + "_th")) {
            Log.d(TAG, "Files deleted");
        } else {
            Log.d(TAG, "No file found");
        }
    }


    /**
     * Sets wallpaper on the top of the priority lists or the default if the list is empty.
     *
     * This function is used whenever an action can cause the wallpaper to change, such as
     * setting a new wallpaper, dismissing one or reordering the priority list.
     */
    public static void setOrIgnoreWallpaper(Context context, List<Wallpaper> activeList) {

        String current = getCurrentWallpaperName(context);

        if (activeList.size() > 0) {
            if (!current.equals(activeList.get(0))) {
                setWallpaper(context, activeList.get(0));
            }
        } else if (!current.equals("default")) {
            setWallpaper(context, new Wallpaper("default"));
        }
    }

    /**
     * Static method to set the system wallpaper.
     *
     * @param context    The current context
     * @param wallpaper  The wallpaper containing the filename
     */
    public static void setWallpaper(Context context, Wallpaper wallpaper) {

        new SetWallpaper().execute(context, wallpaper);

        // Writes the filename to the shared preferences to keep track of the current wallpaper
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.current_wallpaper_key),
                wallpaper.getFilename());
        editor.apply();
    }

    /**
     * Static AsyncTask that sets the system wallpaper on the background without hogging the
     * UI thread.
     */
    private static class SetWallpaper extends AsyncTask<Object, Void, Void> {

        protected Void doInBackground(Object... params) {

            Context context = (Context) params[0];
            Wallpaper wallpaper = (Wallpaper) params[1];

            File file = context.getFileStreamPath(wallpaper.getFilename());

            Bitmap wallpaperImage = BitmapFactory.decodeFile(file.getPath());

            try {
                WallpaperManager.getInstance(context.getApplicationContext()).
                        setBitmap(wallpaperImage);
            } catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }
    }

    /**
     * @param context The current context
     * @return        The filename of the current wallpaper
     */
    public static String getCurrentWallpaperName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);
        return sharedPreferences.getString(context.getString(R.string.current_wallpaper_key),
                "no entry");
    }
}
