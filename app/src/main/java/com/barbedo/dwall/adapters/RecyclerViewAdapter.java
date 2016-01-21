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

package com.barbedo.dwall.adapters;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.barbedo.dwall.R;
import com.barbedo.dwall.activities.ListActivity;
import com.barbedo.dwall.data.Wallpaper;
import com.barbedo.dwall.data.WallpaperData;

import java.util.Collections;
import java.util.List;

public class RecyclerViewAdapter
        extends RecyclerView.Adapter<RecyclerViewAdapter.WallpaperViewHolder>
        implements ItemTouchHelperAdapter {

    private static final String TAG = RecyclerViewAdapter.class.getSimpleName();

    private Context context;
    private List<Wallpaper> wallpaperList;
    private WallpaperData wallpaperData;

    public static class WallpaperViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView name;
        TextView mode;
        TextView info;
        Button editButton;
        ImageView thumb;

        public WallpaperViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            name = (TextView) itemView.findViewById(R.id.wallpaper_name);
            mode = (TextView) itemView.findViewById(R.id.wallpaper_mode);
            info = (TextView) itemView.findViewById(R.id.wallpaper_info);
            thumb = (ImageView) itemView.findViewById(R.id.wallpaper_thumb);
            editButton = (Button) itemView.findViewById(R.id.edit_button);

            // Tags the button to retrieve the WallpaperViewHolder reference later
            editButton.setTag(this);
        }
    }

    public RecyclerViewAdapter(Context context, List<Wallpaper> wallpaperList, WallpaperData wallpaperData) {
        this.context = context;
        this.wallpaperList = wallpaperList;
        this.wallpaperData = wallpaperData;
    }

    @Override
    public int getItemCount() {
        return wallpaperList.size();
    }

    @Override
    public WallpaperViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return new WallpaperViewHolder(v);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(WallpaperViewHolder wallpaperViewHolder, int i) {
        wallpaperViewHolder.name.setText(wallpaperList.get(i).getName());
        wallpaperViewHolder.mode.setText(wallpaperList.get(i).getMode());
        wallpaperViewHolder.info.setText(wallpaperList.get(i).getInfo());

        Context context = wallpaperViewHolder.thumb.getContext();
        String filename = wallpaperList.get(i).getFilename();

        wallpaperViewHolder.thumb.setImageDrawable(Drawable.createFromPath(context.
                getFileStreamPath(filename + "_th").getAbsolutePath()));


        wallpaperViewHolder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Retrieves the View Holder reference using the button tag
                WallpaperViewHolder viewHolder = (WallpaperViewHolder) v.getTag();
                int position = viewHolder.getAdapterPosition();
                Toast.makeText(v.getContext(), "edit " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(wallpaperList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        Log.d(TAG, "onItemMove");

        // Update positions
        wallpaperList.get(fromPosition).setPosition(fromPosition);
        wallpaperList.get(toPosition).setPosition(toPosition);

        // Write to database
        wallpaperData.clearAndInsertWallpaperList(wallpaperList);

        return true;
    }

    @Override
    public boolean onItemDismiss(int position) {

        Log.d(TAG, "onItemDismiss");

        wallpaperData.deleteWallpaper(context, wallpaperList.get(position));

        wallpaperList.remove(position);
        notifyItemRemoved(position);

        // Shifts the position of the entries below the removed entry
        for (int i = position; i < wallpaperList.size(); i++) {
            wallpaperList.get(i).decrementPosition();
        }

        // Write to database
        wallpaperData.clearAndInsertWallpaperList(wallpaperList);

        return true;
    }
}
