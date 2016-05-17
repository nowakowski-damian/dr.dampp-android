package com.thirteendollars.drdampp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thirteendollars.drdampp.R;
import com.thirteendollars.drdampp.utils.JoystickView;

/**
 * Created by Damian Nowakowski on 27.04.16.
 */
public class GUIControlFragment extends Fragment {

    public final static String FRAGMENT_TAG="GUIControlFragment";

    private JoystickView mJoystick;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.gui_control_layout,container,false);
        mJoystick=(JoystickView) view.findViewById(R.id.gui_joystick);
        mJoystick.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener() {
            @Override
            public void onValueChanged(int speed, int turn) {
                Log.e("JOYSTICK", "speed:"+speed + "  turn:" + turn);
            }
        },JoystickView.DEFAULT_LOOP_INTERVAL);

        return view;
    }

}
