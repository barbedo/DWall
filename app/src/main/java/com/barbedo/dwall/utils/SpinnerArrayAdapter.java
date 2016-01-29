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
import android.support.annotation.ArrayRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

/**
 * An extension of the ArrayAdapter that makes the spinner hides its first entry and disable its
 * selection.
 *
 * This way, it is possible to have a first default message that is displayed only when the spinner
 * is first loaded.
 *
 * Based on a question of the StackOverflow's user Georgie.
 *
 * @see <a href="How to hide one item in an Android Spinner">
 *     http://stackoverflow.com/questions/9863378/how-to-hide-one-item-in-an-android-spinner
 *      </a>
 *
 * @author Ricardo Barbedo
 */
public class SpinnerArrayAdapter<CharSequence> extends ArrayAdapter<CharSequence> {


    /**
     * Constructor
     *
     * @param context The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects The objects to represent in the ListView.
     */
    public SpinnerArrayAdapter(Context context,
                               @LayoutRes int resource, @NonNull CharSequence[] objects) {
        this(context, resource, 0, Arrays.asList(objects));
    }

    /**
     * Constructor
     *
     * @param context The current context.
     * @param resource The resource ID for a layout file containing a layout to use when
     *                 instantiating views.
     * @param textViewResourceId The id of the TextView within the layout resource to be populated
     * @param objects The objects to represent in the ListView.
     */
    public SpinnerArrayAdapter(Context context, @LayoutRes int resource,
                               @IdRes int textViewResourceId, @NonNull List<CharSequence> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        View v = null;

        if (position == 0) {
            TextView tv = new TextView(getContext());
            tv.setHeight(0);
            tv.setVisibility(View.GONE);
            v = tv;
        } else {
            v = super.getDropDownView(position, null, parent);
        }

        parent.setVerticalScrollBarEnabled(false);
        return v;
    }

    /**
     * Creates a new SpinnerArrayAdapter from external resources. The content of the array is
     * obtained through {@link android.content.res.Resources#getTextArray(int)}.
     *
     * @param context The application's environment.
     * @param textArrayResId The identifier of the array to use as the data source.
     * @param textViewResId The identifier of the layout used to create views.
     *
     * @return An SpinnerArrayAdapter<CharSequence>.
     */
    public static SpinnerArrayAdapter<java.lang.CharSequence> createFromResource(
            Context context, @ArrayRes int textArrayResId, @LayoutRes int textViewResId) {

        java.lang.CharSequence[] strings = context.getResources().getTextArray(textArrayResId);
        return new SpinnerArrayAdapter<java.lang.CharSequence>(context, textViewResId, strings);
    }
}