package com.thirteendollars.drdampp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thirteendollars.drdampp.R;
import com.thirteendollars.drdampp.connection.APIDecoder;
import com.thirteendollars.drdampp.connection.UDPManager;
import com.thirteendollars.drdampp.utils.Initializer;
import com.thirteendollars.drdampp.utils.JoystickView;
import com.thirteendollars.drdampp.utils.SettingsPreferences;
import com.thirteendollars.drdampp.utils.TwoJoysticksView;

/**
 * Created by Damian Nowakowski on 27.04.16.
 */
public class GUIControlFragment extends Fragment {

    public final static String FRAGMENT_TAG="GUIControlFragment";

    UDPManager mConnection;
    APIDecoder mApi;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SettingsPreferences settings= new SettingsPreferences( getActivity().getApplicationContext() );
        View view= initAppropriateViewFromSettings( settings,inflater,container );
        mApi=new APIDecoder();
        mConnection= ((Initializer)getActivity().getApplication()).getConnection();
        return view;
    }


    private View initAppropriateViewFromSettings(SettingsPreferences settings,LayoutInflater inflater,ViewGroup container) {

        View view=null;
        switch ( settings.getJoystickMode() ){

            case SettingsPreferences.ONE_JOYSTICK_MODE:
                view = inflater.inflate(R.layout.gui_control_v_layout, container,false);
                JoystickView joystick = (JoystickView)view.findViewById(R.id.gui_joystick);
                joystick.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener() {
                    @Override
                    public void onValueChanged(int speed, int turn) {
                        notifyJoystickChange(speed,turn);
                    }
                },JoystickView.DEFAULT_LOOP_INTERVAL);
                break;

            case SettingsPreferences.TWO_JOYSTICKS_MODE:
                view = inflater.inflate(R.layout.gui_control_h_layout,container,false);
                TwoJoysticksView joysticks = (TwoJoysticksView)view.findViewById(R.id.gui_joystick);
                joysticks.setOnJoystickMoveListener(new TwoJoysticksView.OnJoystickMoveListener() {
                    @Override
                    public void onValueChanged(int speed, int turn) {
                        notifyJoystickChange(speed,turn);
                    }
                },JoystickView.DEFAULT_LOOP_INTERVAL);
                break;
        }

        return view;
    }

    private void notifyJoystickChange(int speed, int turn) {
        int leftLevel=( speed+(turn/2) )%101;
        int rightLevel=( speed-(turn/2) )%101;
        mConnection.send( mApi.setMotors(leftLevel,rightLevel) );
    }

}
