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


package com.barbedo.dwall.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.barbedo.dwall.activities.EditActivity;

import java.util.Calendar;

/**
 * Time picker for the time mode of the app.
 *
 * This fragment is used to display two instances of the system default time picker, one for
 * the selection of the starting time of the period in which the corresponding wallpaper must
 * be set and another for the ending time.
 *
 * The two pickers are instantiated using a static factory, so that we can pass it a parameter
 * to change its behavior at running time.
 *
 * @author Ricardo Barbedo
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    /**
     * Definition of the listener that passes the information from the picker
     * to the parent activity.
     */
    public interface OnTimeSetListener {
        public void onTimeSelected(int id, int hourOfDay, int minute);
    }

    private OnTimeSetListener callback;

    /**
     * Static factory that takes the identifier parameter and returns a new fragment.
     * This instantiation helps us reuse the code to get our two instances of the picker,
     * on for the start time and other for the end.
     *
     * @param id Identifier passed to define what picker do we want.
     * @return   A TimePickerFragment.
     */
    public static TimePickerFragment newInstance(int id) {
        TimePickerFragment tpf = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putInt("id", id);
        tpf.setArguments(args);
        return tpf;
    }

    /**
     * Recovers the reference to the listener activity.
     * Called when the fragment is created and attached to its parent activity.
     *
     * @param context Current application context.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        callback = (OnTimeSetListener) context;

    }

    /**
     * Called when the dialog is created.
     * Configures the format and appearance of the picker based on the retrieved identifier.
     *
     * @param savedInstanceState Bundle with the last information of the view.
     * @return                   A new TimePickerDialog with the specified configuration.
     */
    @Override
    public @NonNull Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        final TimePickerDialog tpd = new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));

        // Configures the title text
        TextView title = new TextView(getActivity());
        title.setTextColor(Color.parseColor("#FFFFFF"));
        title.setTextSize(30);
        title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        title.setBackgroundColor(Color.parseColor("#009688"));
        title.setPadding(5, 3, 5, 3);
        title.setGravity(Gravity.CENTER_HORIZONTAL);
        tpd.setCustomTitle(title);

        // Registers the on show listener to change the dialog appearance
        tpd.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                tpd.getButton(Dialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
                tpd.getButton(Dialog.BUTTON_POSITIVE).setText("Next");
            }
        });

        // Recovers the identifier to know what is the desired picker
        int id = getArguments().getInt("id");

        switch (id) {
            case EditActivity.START_TIME_PICKER:
                title.setText("Start");
                break;
            case EditActivity.END_TIME_PICKER:
                title.setText("End");
                break;
            default:
                break;
        }

        return tpd;
    }

    /**
     * Called when a time is selected.
     * Passes the time and the identifier to the parent activity via the interface.
     *
     * @param view      View of the TimePicker.
     * @param hourOfDay Selected hour with a 24h system.
     * @param minute    Selected minute.
     */
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        int id = getArguments().getInt("id");
        callback.onTimeSelected(id, hourOfDay, minute);
    }
}