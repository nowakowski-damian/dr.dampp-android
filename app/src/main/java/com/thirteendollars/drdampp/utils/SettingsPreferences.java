package com.thirteendollars.drdampp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Damian Nowakowski on 18.05.16.
 */
public class SettingsPreferences {

    private final String PREFERENCES_TAG = "SharedPreferencesTag";

    //GUI controller settings
    private final String JOYSTICK_SETTINGS = "GUIControllerSettings";
    public static final int TWO_JOYSTICKS_MODE = 1;
    public static final int ONE_JOYSTICK_MODE = 2;

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    public SettingsPreferences(Context context) {
        mPreferences= context.getSharedPreferences(PREFERENCES_TAG,Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
    }



    public int getJoystickMode(){
        return mPreferences.getInt(JOYSTICK_SETTINGS,TWO_JOYSTICKS_MODE);
    }

    public void setJoystickMode(int MODE){
        mEditor.putInt(JOYSTICK_SETTINGS,MODE).commit();
    }





}
