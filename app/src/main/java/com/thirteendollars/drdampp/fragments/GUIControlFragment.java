package com.thirteendollars.drdampp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;
import com.thirteendollars.drdampp.R;
import com.thirteendollars.drdampp.activities.MainActivity;
import com.thirteendollars.drdampp.connection.APIDecoder;
import com.thirteendollars.drdampp.connection.UDPManager;
import com.thirteendollars.drdampp.utils.OneJoystickView;
import com.thirteendollars.drdampp.utils.SettingsPreferences;
import com.thirteendollars.drdampp.utils.TwoJoysticksView;

/**
 * Created by Damian Nowakowski on 27.04.16.
 */
public class GUIControlFragment extends Fragment implements View.OnClickListener {

    public final static String FRAGMENT_TAG="GUIControlFragment";

    private UDPManager mConnection;
    private APIDecoder mApi;

    private ToggleButton mButton0;
    private ToggleButton mButton1;
    private ToggleButton mButton2;
    private ToggleButton mButton3;
    private ToggleButton mButton4;
    private ToggleButton mButton5;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SettingsPreferences settings= new SettingsPreferences( getActivity().getApplicationContext() );
        View view= initAppropriateJoystickFromSettings( settings,inflater,container );
        initButtonsPanel(view);
        mApi=new APIDecoder();
        mConnection= ((MainActivity)getActivity()).getConnection();
        return view;
    }

    private void initButtonsPanel(View view) {
        mButton0=(ToggleButton)view.findViewById(R.id.control_button_0);
        mButton1=(ToggleButton)view.findViewById(R.id.control_button_1);
        mButton2=(ToggleButton)view.findViewById(R.id.control_button_2);
        mButton3=(ToggleButton)view.findViewById(R.id.control_button_3);
        mButton4=(ToggleButton)view.findViewById(R.id.control_button_4);
        mButton5=(ToggleButton)view.findViewById(R.id.control_button_5);
        mButton0.setOnClickListener(this);
        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);
        mButton4.setOnClickListener(this);
        mButton5.setOnClickListener(this);
    }


    private View initAppropriateJoystickFromSettings(SettingsPreferences settings,LayoutInflater inflater,ViewGroup container) {

        View view=null;
        switch ( settings.getJoystickMode() ){

            case SettingsPreferences.ONE_JOYSTICK_MODE:
                view = inflater.inflate(R.layout.gui_one_control_layout, container,false);
                OneJoystickView joystick = (OneJoystickView)view.findViewById(R.id.gui_joystick);
                joystick.setOnJoystickMoveListener(new OneJoystickView.OnJoystickMoveListener() {
                    @Override
                    public void onValueChanged(int speed, int turn) {
                        notifyJoystickChange(speed,turn);
                    }
                }, OneJoystickView.DEFAULT_LOOP_INTERVAL);
                break;

            case SettingsPreferences.TWO_JOYSTICKS_MODE:
                view = inflater.inflate(R.layout.gui_two_controls_layout,container,false);
                TwoJoysticksView joysticks = (TwoJoysticksView)view.findViewById(R.id.gui_joystick);
                joysticks.setOnJoystickMoveListener(new TwoJoysticksView.OnJoystickMoveListener() {
                    @Override
                    public void onValueChanged(int speed, int turn) {
                        notifyJoystickChange(speed,turn);
                    }
                }, OneJoystickView.DEFAULT_LOOP_INTERVAL);
                break;
        }

        return view;
    }

    private void notifyJoystickChange(int speed, int turn) {
        int leftLevel=( speed+(turn/2) )%101;
        int rightLevel=( speed-(turn/2) )%101;
        mConnection.send( mApi.setMotors(leftLevel,rightLevel) );
    }

    @Override
    public void onClick(View v) {
        byte[] dataToSend=null;
        switch ( v.getId() ){
            case R.id.control_button_0: dataToSend=mApi.setSwitch( 0, mButton0.isChecked() ); break;
            case R.id.control_button_1: dataToSend=mApi.setSwitch( 1, mButton1.isChecked() ); break;
            case R.id.control_button_2: dataToSend=mApi.setSwitch( 2, mButton2.isChecked() ); break;
            case R.id.control_button_3: dataToSend=mApi.setSwitch( 3, mButton3.isChecked() ); break;
            case R.id.control_button_4: dataToSend=mApi.setSwitch( 4, mButton4.isChecked() ); break;
            case R.id.control_button_5: dataToSend=mApi.setSwitch( 5, mButton5.isChecked() ); break;
        }
        mConnection.send(dataToSend);
    }
}
