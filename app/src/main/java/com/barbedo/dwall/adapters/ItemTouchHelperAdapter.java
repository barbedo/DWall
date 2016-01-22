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

/**
 * Listens for the callback of the ItemTouchHelper.
 *
 * @author Ricardo Barbedo
 */
public interface ItemTouchHelperAdapter {

    /**
     * Called every time the item is shifted.
     *
     * @param fromPosition previous position of the selected card
     * @param toPosition   new position of the selected card
     * @return             true if item was moved
     */
    boolean onItemMove(int fromPosition, int toPosition);

    /**
     * Called every time the item is dismissed with a swipe.
     *
     * @param position position of the dismissed card
     * @return         true if item was dismissed
     */
    boolean onItemDismiss(int position);
}
