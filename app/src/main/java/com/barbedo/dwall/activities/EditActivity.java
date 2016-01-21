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
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.barbedo.dwall.R;
import com.barbedo.dwall.data.Wallpaper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class EditActivity extends AppCompatActivity {

    private final String TAG = "EditActivity";

    private final int DEFAULT_POSITION = 0;
    private static final int THUMB_WIDTH = 108;
    private static final int THUMB_HEIGHT = 192;

    // Action code to be certain that the response is for us
    private static final int SELECT_PICTURE = 100;
    private String selectedImagePath;

    private Wallpaper wallpaper;
    private ImageView preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        preview = (ImageView) findViewById(R.id.preview);

        wallpaper = new Wallpaper();

        Intent intent = getIntent();
        wallpaper.setPosition(intent.getIntExtra(ListActivity.EXTRA_POSITION, DEFAULT_POSITION));

        // Populate spinner
        Spinner spinner = (Spinner) findViewById(R.id.mode_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_text, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Log.d(TAG, "onCreate");
    }

    public void registerWallpaper(View v) {

        // TODO: Disable the button while the wallpaper isn't good
        // button.setEnabled(false);
        // TODO: Save the wallpaper to the

        // TMP: Delete the saved image and created thumbnail
        deleteFiles();

        Intent intent = new Intent(this, ListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void launchGallery(View v) {

        // Delete files from last selection
        deleteFiles();

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == SELECT_PICTURE) {

            // Retrieves URI
            Uri uri = data.getData();

            // Filename is a (unique) timestamp
            Long tsLong = System.currentTimeMillis() / 1000;
            String filename = tsLong.toString();
            String filenameThumb = filename + "_th";

            // Copy to internal data
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

            // Create thumbnail
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

            wallpaper.setFilename(filename);

            Log.d(TAG, "Wallpaper filename: " + filename);
        }
    }

    private void deleteFiles() {
        if (deleteFile(wallpaper.getFilename()) &&
                deleteFile(wallpaper.getFilename() + "_th" )) {
            Log.d(TAG, "Files deleted");
        } else {
            Log.d(TAG, "No file found");
        }
    }
}
