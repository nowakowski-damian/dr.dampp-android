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
    //Connection settings
    private final String IP_ADDRESS_SETTINGS = "IpAddressSettings";
    private final String PORT_NUMBER_SETTINGS = "PortNumberSettings";
    private final String DEFAULT_SERVER_IP_ADDRESS = "192.168.0.110";
    private final int  DEFAULT_SERVER_PORT = 9876;


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


    public int getServerPort(){
        return mPreferences.getInt(PORT_NUMBER_SETTINGS,DEFAULT_SERVER_PORT);
    }

    public void setServerPort(int port){
        mEditor.putInt(PORT_NUMBER_SETTINGS,port).commit();
    }

    public String  getServerAddress(){
        return mPreferences.getString(IP_ADDRESS_SETTINGS,DEFAULT_SERVER_IP_ADDRESS);
    }

    public void setServerAddress(String ip){
        mEditor.putString(IP_ADDRESS_SETTINGS,ip).commit();
    }





}
