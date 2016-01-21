package com.barbedo.dwall.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.barbedo.dwall.R;
import com.barbedo.dwall.data.Wallpaper;

public class EditActivity extends AppCompatActivity {

    private final String TAG = "EditActivity";
    private final int DEFAULT_POSITION = 0;

    private Wallpaper wallpaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        wallpaper = new Wallpaper();

        Intent intent = getIntent();
        wallpaper.setPosition(intent.getIntExtra(ListActivity.EXTRA_POSITION, DEFAULT_POSITION));
    }

    public void registerWallpaper(View v) {

        // TODO: Disable the button while the wallpaper isn't good
        // button.setEnabled(false);
        // TODO: Save the wallpaper to the

        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }
}
