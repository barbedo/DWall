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

package com.barbedo.dwall.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.barbedo.dwall.data.DWallApplication;
import com.barbedo.dwall.data.Wallpaper;
import com.barbedo.dwall.data.WallpaperData;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 */
public class TimeService extends IntentService {

    private static final String TAG = "TimeService";

    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_SET_ALARM = "com.barbedo.dwall.services.action.SET_ALARM";
    public static final String ACTION_UNSET_ALARM = "com.barbedo.dwall.services.action.UNSET_ALARM";
    public static final String ACTION_ALARM = "com.barbedo.dwall.services.action.ALARM";

    public static final String EXTRA_INFO = "com.barbedo.dwall.services.extra.INFO";
    public static final String EXTRA_FILENAME = "com.barbedo.dwall.services.extra.FILENAME";
    public static final String EXTRA_STATE = "com.barbedo.dwall.services.extra.STATE";

    private static final int ID_SET_START = 131;
    private static final int ID_SET_END = 132;

    public TimeService() {
        super("TimeService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "TimeService called");
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SET_ALARM.equals(action)) {
                Log.d(TAG, "action set alarm");
                final String info = intent.getStringExtra(EXTRA_INFO);
                final String filename = intent.getStringExtra(EXTRA_FILENAME);
                Log.d(TAG, info);
                handleActionSet(info, filename);
            } else if (ACTION_UNSET_ALARM.equals(action)) {
                final String info = intent.getStringExtra(EXTRA_INFO);
                final String filename = intent.getStringExtra(EXTRA_FILENAME);
                handleActionUnset(info, filename);
            } else if (ACTION_ALARM.equals(action)) {
                Log.d(TAG, "alarm called");
                final String state = intent.getStringExtra(EXTRA_STATE);
                final String filename = intent.getStringExtra(EXTRA_FILENAME);
                handleActionAlarm(state, filename);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionSet(String info, String filename) {

        // Set alarm with supplied information

//        String[] times = info.split("\\s+");
//        Date startTime = new Date();
//        Date endTime = new Date();
//
//        try {
//            startTime = new SimpleDateFormat("HH:mm").parse(times[0]);
//            endTime = new SimpleDateFormat("HH:mm").parse(times[1]);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//
//        Calendar calendarStart = Calendar.getInstance();
//        Calendar calendarEnd = Calendar.getInstance();
//        calendarStart.setTime(startTime);
//        calendarEnd.setTime(endTime);

//        int startHour = Integer.parseInt(info.substring(0,2));
//        int startMinute = Integer.parseInt(info.substring(3,5));
//        int endHour = Integer.parseInt(info.substring(6,8));
//        int endMinute = Integer.parseInt(info.substring(9,11));
//
//        Calendar calendarStart = Calendar.getInstance();
//        Calendar calendarEnd = Calendar.getInstance();
//
//        calendarStart.set(Calendar.HOUR_OF_DAY, startHour);
//        calendarStart.set(Calendar.MINUTE, startMinute);
//        calendarStart.set(Calendar.SECOND, 0);
//
//        calendarEnd.set(Calendar.HOUR_OF_DAY, endHour);
//        calendarEnd.set(Calendar.MINUTE, endMinute);
//        calendarEnd.set(Calendar.SECOND, 0);
//
//        Intent intentStart = new Intent(this, TimeService.class);
//        intentStart.setAction(ACTION_ALARM);
//        intentStart.putExtra(EXTRA_STATE, "start");
//        intentStart.putExtra(EXTRA_FILENAME, filename);
//
//        Intent intentEnd = new Intent(this, TimeService.class);
//        intentEnd.setAction(ACTION_ALARM);
//        intentEnd.putExtra(EXTRA_STATE, "end");
//        intentEnd.putExtra(EXTRA_FILENAME, filename);
//
//        PendingIntent pendingIntentStart = PendingIntent.getService(this, ID_SET_START,
//                intentStart, PendingIntent.FLAG_UPDATE_CURRENT);
//        PendingIntent pendingIntentEnd = PendingIntent.getService(this, ID_SET_END,
//                intentEnd, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendarStart = getCalendarFromInfo(ID_SET_START, info);
        Calendar calendarEnd = getCalendarFromInfo(ID_SET_END, info);
        PendingIntent pendingIntentStart = getPendingIntentFromFilename(ID_SET_START, filename);
        PendingIntent pendingIntentEnd = getPendingIntentFromFilename(ID_SET_END, filename);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                calendarStart.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentStart);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                calendarEnd.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentEnd);
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionUnset(String info, String filename) {

        PendingIntent pendingIntentStart = getPendingIntentFromFilename(ID_SET_START, filename);
        PendingIntent pendingIntentEnd = getPendingIntentFromFilename(ID_SET_END, filename);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntentStart);
        alarmManager.cancel(pendingIntentEnd);
        pendingIntentStart.cancel();
        pendingIntentEnd.cancel();
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionAlarm(String state, String filename) {

        DWallApplication application = (DWallApplication) getApplication();
        WallpaperData wallpaperData = application.getWallpaperData();
        List<Wallpaper> activeList = wallpaperData.getActiveWallpaperList(this);

        Log.d(TAG, "Action alarm: " + state);

        if (state.equals("start")) {

            if (activeList.size() > 0) {
                if (activeList.get(0).getMode().equals("Time") &&
                        activeList.get(0).getFilename().equals(filename)) {

                    WallpaperManager wallpaperManager
                            = WallpaperManager.getInstance(getApplicationContext());

                    File file = getFileStreamPath(activeList.get(0).getFilename());

                    Bitmap wallpaperImage = BitmapFactory.decodeFile(file.getPath());

                    try {
                        wallpaperManager.setBitmap(wallpaperImage);
                    } catch (IOException e){
                        e.printStackTrace();
                    }

                    Log.d(TAG, activeList.get(0).toString() + " set");
                }
            }


        } else if (state.equals("end")) {

            if (activeList.size() > 0) {

                // Sets the wallpaper
                WallpaperManager wallpaperManager
                        = WallpaperManager.getInstance(getApplicationContext());

                File file = getFileStreamPath(activeList.get(0).getFilename());

                Bitmap wallpaperImage = BitmapFactory.decodeFile(file.getPath());

                try {
                    wallpaperManager.setBitmap(wallpaperImage);
                } catch (IOException e){
                    e.printStackTrace();
                }

                Log.d(TAG, activeList.get(0).toString() + " set");
            } else {

                WallpaperManager wallpaperManager
                        = WallpaperManager.getInstance(getApplicationContext());
                // sets default
                File file = getFileStreamPath("default");
                Bitmap wallpaperImage = BitmapFactory.decodeFile(file.getPath());
                try {
                    wallpaperManager.setBitmap(wallpaperImage);
                } catch (IOException e){
                    e.printStackTrace();
                }
                Log.d(TAG, "Default wallpaper set");
            }
        }
    }

    private PendingIntent getPendingIntentFromFilename(int id, String filename) {

        String extraState;

        switch (id) {
            case ID_SET_START:
                extraState = "start";
                break;
            case ID_SET_END:
                extraState = "end";
                break;
            default:
                extraState = "";
                break;
        }


        Intent intent = new Intent(this, TimeService.class);
        intent.setAction(ACTION_ALARM);
        intent.putExtra(EXTRA_STATE, extraState);
        intent.putExtra(EXTRA_FILENAME, filename);

        return PendingIntent.getService(this, id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private Calendar getCalendarFromInfo(int id, String info) {

        int hour, minute;
        switch (id) {
            case ID_SET_START:
                hour = Integer.parseInt(info.substring(0,2));
                minute = Integer.parseInt(info.substring(3,5));
                break;
            case ID_SET_END:
                hour = Integer.parseInt(info.substring(6,8));
                minute = Integer.parseInt(info.substring(9,11));
                break;
            default:
                hour = 0;
                minute = 0;
                break;
        }

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        return calendar;
    }
}
