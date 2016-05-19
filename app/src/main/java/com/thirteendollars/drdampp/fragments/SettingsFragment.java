package com.thirteendollars.drdampp.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.thirteendollars.drdampp.R;
import com.thirteendollars.drdampp.utils.SettingsPreferences;

/**
 * Created by Damian Nowakowski on 27.04.16.
 */
public class SettingsFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {

    public final static String FRAGMENT_TAG="SettingsFragment";

    private RadioGroup mJoystickSettings;
    private SettingsPreferences mSettings;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mSettings=new SettingsPreferences( getActivity() );
        View view = inflater.inflate(R.layout.settings_layout,container,false);
        initViews(view);

        return view;

    }

    private void initViews(View view) {
        mJoystickSettings=(RadioGroup)view.findViewById(R.id.settings_joystick);
        switch ( mSettings.getJoystickMode() ){
            case SettingsPreferences.ONE_JOYSTICK_MODE: mJoystickSettings.check(R.id.one_joystick); break;
            case SettingsPreferences.TWO_JOYSTICKS_MODE: mJoystickSettings.check(R.id.two_joysticks); break;
        }
        mJoystickSettings.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if(group.equals(mJoystickSettings)){
            switch (checkedId){
                case R.id.one_joystick: mSettings.setJoystickMode(SettingsPreferences.ONE_JOYSTICK_MODE); break;
                case R.id.two_joysticks: mSettings.setJoystickMode(SettingsPreferences.TWO_JOYSTICKS_MODE); break;
            }
        }
    }
}
