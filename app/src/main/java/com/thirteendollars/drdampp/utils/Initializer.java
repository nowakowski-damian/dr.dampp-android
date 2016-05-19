package com.thirteendollars.drdampp.utils;

import android.app.Application;
import android.util.Log;

import com.thirteendollars.drdampp.connection.UDPManager;

import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by Damian Nowakowski on 27.04.16.
 */
public class Initializer extends Application {



    public static boolean init_success;
    private UDPManager mUDPManager;
    private SettingsPreferences mSettings;

    @Override
    public void onCreate() {
        super.onCreate();
        init_success=true;
        mSettings = new SettingsPreferences(this);
        initUDP();
    }

    private void testConnection() {

    }

    private void initUDP() {

        try {
            mUDPManager= new UDPManager( mSettings.getServerAddress(),mSettings.getServerPort() );
        } catch (UnknownHostException e) {
            init_success=false;
            Log.e(getClass().getName(),e.toString());
        } catch (SocketException e) {
            init_success=false;
            Log.e(getClass().getName(),e.toString());
        }
    }

    public UDPManager getConnection(){
        return mUDPManager;
    }
}
