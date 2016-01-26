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


import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.barbedo.dwall.activities.EditActivity;
import com.barbedo.dwall.activities.ListActivity;
import com.barbedo.dwall.data.Wallpaper;
import com.barbedo.dwall.data.WallpaperData;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * This class defines the adapter of the RecycleView, which is preferred over the use of a ListView
 * for lists of programs following the Material Design guideline. The ItemTouchHelper implementation
 * offers an easy way to handle gestures and animations when changing the list.
 *
 * @author Ricardo Barbedo
 */
public class RecyclerViewAdapter
        extends RecyclerView.Adapter<RecyclerViewAdapter.WallpaperViewHolder>
        implements ItemTouchHelperAdapter {

    private static final String TAG = RecyclerViewAdapter.class.getSimpleName();

    private Context context;
    private List<Wallpaper> wallpaperList;
    private WallpaperData wallpaperData;

    /**
     * This class defines the ViewHolder containing all the information of the card that represents
     * the wallpaper
     */
    public static class WallpaperViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView name;
        TextView mode;
        TextView info;
        Button editButton;
        ImageView thumb;

        /**
         * Constructor of the view holder.
         *
         * @param itemView View of the item to be held.
         */
        public WallpaperViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            name = (TextView) itemView.findViewById(R.id.wallpaper_name);
            mode = (TextView) itemView.findViewById(R.id.wallpaper_mode);
            info = (TextView) itemView.findViewById(R.id.wallpaper_info);
            thumb = (ImageView) itemView.findViewById(R.id.wallpaper_thumb);
            editButton = (Button) itemView.findViewById(R.id.edit_button);

            // Tags the button to retrieve the WallpaperViewHolder reference later for the edit button.
            editButton.setTag(this);
        }
    }

    /**
     * Constructor of the adapter class.
     *
     * @param context       Application context.
     * @param wallpaperList Reference to used list.
     * @param wallpaperData Reference to used WallpaperData.
     */
    public RecyclerViewAdapter(Context context, List<Wallpaper> wallpaperList, WallpaperData wallpaperData) {
        this.context = context;
        this.wallpaperList = wallpaperList;
        this.wallpaperData = wallpaperData;
    }

    /**
     * @return Size of the adapted list.
     */
    @Override
    public int getItemCount() {
        return wallpaperList.size();
    }

    /**
     * Called when a ViewHolder is created.
     *
     * @param viewGroup ViewGroup containing the item views.
     * @param i         Number of the item view in the group.
     * @return          ViewHolder for the item view.
     */
    @Override
    public WallpaperViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return new WallpaperViewHolder(v);
    }

    /**
     * Called when the adapter is attached to the RecyclerView.
     *
     * @param recyclerView Attached RecyclerView.
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    /**
     * Called when the ViewHolder is bound to the RecyclerView.
     * Sets the cards fields and sets up the button listener.
     *
     * @param wallpaperViewHolder Bound ViewHolder.
     * @param i                   Position of the View in the adapted list.
     */
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
                Intent intent = new Intent(v.getContext(), EditActivity.class);
                intent.putExtra(ListActivity.EXTRA_POSITION, position);
                v.getContext().startActivity(intent);
                //Toast.makeText(v.getContext(), "edit " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Called when an item of the list is moved.
     *
     * @param fromPosition Previous position of the selected card.
     * @param toPosition   New position of the selected card.
     * @return             True the item was moved.
     *
     * @see ItemTouchHelperAdapter
     */
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

    /**
     * Called when an item of the list is dismissed with a swipe.
     *
     * @param position Position of the dismissed card.
     * @return         True if the item was dismissed.
     */
    @Override
    public boolean onItemDismiss(int position) {

        Log.d(TAG, "onItemDismiss");

        List<Wallpaper> activeWallpapers = wallpaperData.getActiveWallpaperList(context);

        // Sets default wallpaper if the current one is dismissed from the list
        // TODO: Move to an AsyncTask (too much work on the UI thread)
        if (activeWallpapers.size() > 0) {
            if (activeWallpapers.get(0).getFilename().
                    equals(wallpaperList.get(position).getFilename())) {

                File file = context.getFileStreamPath("default");
                Bitmap wallpaperImage = BitmapFactory.decodeFile(file.getPath());
                try {
                    WallpaperManager.getInstance(context.getApplicationContext()).
                            setBitmap(wallpaperImage);
                } catch (IOException e){
                    e.printStackTrace();
                }
                Log.d(TAG, "Default wallpaper set");
            }
        }

        wallpaperData.deleteWallpaper(context, wallpaperList.get(position));

        wallpaperList.remove(position);
        notifyItemRemoved(position);

        // Shifts the position of the entries below the removed entry
        for (int i = position; i < wallpaperList.size(); i++) {
            wallpaperList.get(i).decrementPosition();
        }

        // Writes to database
        wallpaperData.clearAndInsertWallpaperList(wallpaperList);

        return true;
    }
}
