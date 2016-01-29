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

package com.barbedo.dwall.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.barbedo.dwall.R;
import com.barbedo.dwall.data.DWallApplication;
import com.barbedo.dwall.data.Wallpaper;
import com.barbedo.dwall.data.WallpaperData;
import com.barbedo.dwall.utils.WallpaperHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Selects the default wallpaper.
 *
 * This activity launches the default gallery activity to select the default wallpaper to be used.
 *
 * The selected image is saved to the internal storage with the name "default" and a thumbnail is
 * generated.
 *
 * @author Ricardo Barbedo
 */
public class DefaultActivity extends AppCompatActivity {

    private final String TAG = "DefaultActivity";

    private static final int SELECT_PICTURE = 100;
    private static final int THUMB_WIDTH = 216;
    private static final int THUMB_HEIGHT = 384;

    private ImageView preview;
    private Button okButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);

        preview = (ImageView) findViewById(R.id.preview_default);
        okButton = (Button) findViewById(R.id.ok_default_button);

        if (Arrays.asList(fileList()).contains("default")) {
            preview.setImageDrawable(Drawable.
                    createFromPath(getFileStreamPath("default_th").getAbsolutePath()));
        }
    }

    /**
     * Launches the gallery activity to select the desired image.
     *
     * @param v Select image button
     */
    public void launchGallery(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }

    /**
     * Returns to the main list.
     *
     * @param v Ok button.
     */
    public void registerWallpaper(View v) {

        // Sets the default wallpaper if there is no active wallpaper
        DWallApplication application = (DWallApplication) getApplication();
        WallpaperData wallpaperData = application.getWallpaperData();
        List<Wallpaper> activeList = wallpaperData.getActiveWallpaperList(this);

        WallpaperHelper.setOrIgnoreWallpaper(this, activeList);

        Intent intent = new Intent(this, ListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  // Clears stack
        startActivity(intent);
    }

    /**
     * Receives and treats the data returned by the gallery activity.
     *
     * @param requestCode Our specified constant, to be sure that we were the ones that requested.
     * @param resultCode  Result code passed by the gallery activity.
     * @param data        Data containing the path of the selected image.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == SELECT_PICTURE) {

            String filename = "default";

            // Delete files from the previous default
            if (deleteFile(filename) &&
                    deleteFile(filename + "_th")) {
                Log.d(TAG, "Files deleted");
            } else {
                Log.d(TAG, "No file found");
            }

            // Retrieves URI
            Uri uri = data.getData();

            // Copies to internal data
            try {
                InputStream input = getContentResolver().openInputStream(uri);
                FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);
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

            File filePath = getFileStreamPath(filename);
            Log.d(TAG, "Internal filepath: " + filePath.toString());

            // Create a thumbnail
            Bitmap thumbnail = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(filePath.getAbsolutePath()),
                    THUMB_WIDTH, THUMB_HEIGHT);
            try {
                FileOutputStream fos = openFileOutput(filename + "_th", Context.MODE_PRIVATE);
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Displays thumbnail
            preview.setImageDrawable(Drawable.
                    createFromPath(getFileStreamPath(filename + "_th").getAbsolutePath()));

            // Enable the button
            okButton.setEnabled(true);
        }
    }
}
