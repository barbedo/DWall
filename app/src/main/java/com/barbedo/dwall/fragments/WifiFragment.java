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

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.barbedo.dwall.R;

/**
 * Dialog fragment to specify the Wi-Fi name.
 *
 * The input name is returned to the parent activity with the OnWifiSetListener interface.
 *
 * @author Ricardo Barbedo
 */
public class WifiFragment extends DialogFragment {

    private EditText editText;
    private Button currentButton;
    private Button okButton;

    public static WifiFragment newInstance(String title) {
        WifiFragment frag = new WifiFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    public interface OnWifiSetListener {
        public void onWifiSelected(String wifiName);
    }

    private OnWifiSetListener callback;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wifi, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        callback = (OnWifiSetListener) getContext();

        getDialog().setCanceledOnTouchOutside(false);

        editText = (EditText) view.findViewById(R.id.wifi_name);
        currentButton = (Button) view.findViewById(R.id.current_button);
        okButton = (Button) view.findViewById(R.id.ok_button_fragment);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String wifiName = editText.getText().toString();
                callback.onWifiSelected(wifiName);
                dismiss();
            }
        });

        currentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeCurrentWifi();
            }
        });

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);

        // Show soft keyboard automatically and request focus to field
        editText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public void writeCurrentWifi() {
        WifiManager wifiManager = (WifiManager)
                getActivity().getSystemService(Context.WIFI_SERVICE);

        String currentName = wifiManager.getConnectionInfo().getSSID();

        // Remove double quotes
        editText.setText(currentName.replace("\"", ""));
    }
}
