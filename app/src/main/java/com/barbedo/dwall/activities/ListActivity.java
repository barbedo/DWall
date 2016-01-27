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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.barbedo.dwall.R;
import com.barbedo.dwall.adapters.ItemTouchHelperCallback;
import com.barbedo.dwall.adapters.RecyclerViewAdapter;
import com.barbedo.dwall.data.DWallApplication;
import com.barbedo.dwall.data.Wallpaper;
import com.barbedo.dwall.data.WallpaperData;

import java.util.Arrays;
import java.util.List;


/**
 * Main activity containing the wallpaper list.
 *
 * The items are represented as a list of wallpaper objects in the activity and as rows a SQLite
 * database. When an item is edited or deleted, the database will be properly updated.
 *
 * The list is displayed with the help of the RecyclerView, which is the standard way to create
 * lists according to the Material Design guidelines. The drag and swipe interactions and its
 * animations are managed by the ItemTouchHelper class.
 *
 * @author Ricardo Barbedo
 */
public class ListActivity extends AppCompatActivity {

    // Constants
    private static final String TAG = "ListActivity";
    public static final String EXTRA_POSITION = "com.barbedo.dwall.EXTRA_POSITION";

    private DWallApplication dWallApplication;
    private WallpaperData wallpaperData;
    private List<Wallpaper> wallpaperList;
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get data from the application object
        dWallApplication = (DWallApplication) getApplication();
        wallpaperData = dWallApplication.getWallpaperData();
        wallpaperList = wallpaperData.getWallpaperList();

        // RecyclerView initialization
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        adapter = new RecyclerViewAdapter(getApplicationContext(), wallpaperList, wallpaperData);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);

        // ItemTouchHelper initialization
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rv);

        // Floating button initialization
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!Arrays.asList(fileList()).contains("default")) {
                    Snackbar.make(view, "Please, set a default wallpaper.",
                            Snackbar.LENGTH_LONG).show();
                } else if (wallpaperList.size() >= 5) {
                    Snackbar.make(view, "Maximum number of wallpapers reached.",
                            Snackbar.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(ListActivity.this, EditActivity.class);
                    intent.putExtra(EXTRA_POSITION, wallpaperList.size());
                    startActivity(intent);
                }

            }
        });

        Log.d(TAG, "onCreate");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_default) {
            Log.d(TAG, "Set default selected");

            Intent intent = new Intent(this, DefaultActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
