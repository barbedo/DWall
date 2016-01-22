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

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * This class configures the ItemTouchHelper that enables gestures on the RecyclerView list.
 *
 * @author  Ricardo Barbedo
 */
public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter adapter;

    /**
     * Constructor.
     * Recovers the RecyclerViewAdapter that it is going to listen.
     *
     * @param adapter The RecyclerViewAdapter used.
     */
    public ItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
        this.adapter = adapter;
    }

    /**
     * Sets the configuration for the long press drag.
     *
     * @return True if long press drag is enabled and false otherwise
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    /**
     * Sets the configuration for the swipe gesture.
     *
     * @return True if the swipe gesture is enabled and false otherwise.
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    /**
     * Configures the allowed movements.
     *
     * @param recyclerView RecyclerView that contains the view that was moved.
     * @param viewHolder   Holder of the view that was moved.
     * @return             Flags that configure the helper.
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    /**
     * Called when the item is moved with a long press drag.
     * Called each time the position changes not only on the finger release. Calls the
     * adapter method.
     *
     * @param recyclerView RecyclerView that contains the view that was moved.
     * @param viewHolder   Holder of the view that was moved.
     * @param target       Holder of the new position of the view.
     * @return             Flags that configure the helper.
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    /**
     * Called when the item is swiped. Calls the adapter method.
     *
     * @param viewHolder Holder of the view that was swiped.
     * @param direction  Direction of the swipe.
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        adapter.onItemDismiss(viewHolder.getAdapterPosition());
    }
}
