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
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.barbedo.dwall.activities.EditActivity;

import java.util.Calendar;

/**
 * Created by ricardo on 1/22/16.
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private OnTimeSetListener callback;

    public static TimePickerFragment newInstance(int id) {
        TimePickerFragment tpf = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putInt("id", id);
        tpf.setArguments(args);
        return tpf;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        callback = (OnTimeSetListener) context;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it

        final TimePickerDialog tpd = new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));

        TextView title = new TextView(getActivity());

        title.setTextColor(Color.parseColor("#FFFFFF"));
        title.setTextSize(30);
        title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        title.setBackgroundColor(Color.parseColor("#009688"));
        title.setPadding(5, 3, 5, 3);
        title.setGravity(Gravity.CENTER_HORIZONTAL);

        tpd.setCustomTitle(title);

        tpd.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                tpd.getButton(Dialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
                tpd.getButton(Dialog.BUTTON_POSITIVE).setText("Next");
            }
        });

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

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user

        int id = getArguments().getInt("id");

        callback.onTimeSelected(id, hourOfDay, minute);

    }

    public interface OnTimeSetListener {
        public void onTimeSelected(int id, int hourOfDay, int minute);
    }
}