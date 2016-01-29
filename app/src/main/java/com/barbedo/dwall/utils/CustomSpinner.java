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

import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.widget.Spinner;

/**
 * Extension of the Spinner class that calls the listener even when the same item is selected twice.
 *
 * Solution based on the user melquiades' answer on StackOverflow.
 *
 * @see <a href="How can I get an event in Android Spinner when the current selected item is selected again?">
 *     http://stackoverflow.com/questions/5335306/how-can-i-get-an-event-in-android-spinner-when-the-current-selected-item-is-sele
 *     </a>
 *
 * @author Ricardo Barbedo
 */
public class CustomSpinner extends AppCompatSpinner {

    /**
     * Construct a new custom spinner with the given context's theme.
     *
     * @param context The Context the view is running in, through which it can
     *        access the current theme, resources, etc.
     */
    public CustomSpinner(Context context) {
        super(context);
    }

    /**
     * Construct a new custom spinner with the given context's theme and the supplied attribute set.
     *
     * @param context The Context the view is running in, through which it can
     *        access the current theme, resources, etc.
     * @param attrs The attributes of the XML tag that is inflating the view.
     */
    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Construct a new custom spinner with the given context's theme, the supplied attribute set,
     * and default style attribute.
     *
     * @param context The Context the view is running in, through which it can
     *        access the current theme, resources, etc.
     * @param attrs The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a
     *        reference to a style resource that supplies default values for
     *        the view. Can be 0 to not look for defaults.
     */
    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Set the selected position of the list.
     *
     * In case the item selected is the same as the previous, it will explicitly call the listener.
     *
     * @see android.widget.AbsSpinner
     *
     * @param position List position to set as selected.
     * @param animate  Animation request.
     */
    @Override
    public void setSelection(int position, boolean animate) {
        super.setSelection(position);

        if (position == getSelectedItemPosition()) {
            getOnItemSelectedListener().onItemSelected(this,
                    getSelectedView(), position, getSelectedItemId());
        }
    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);

        if (position == getSelectedItemPosition()) {
            getOnItemSelectedListener().onItemSelected(this,
                    getSelectedView(), position, getSelectedItemId());
        }
    }


}
