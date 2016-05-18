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

    private final String SERVER_IP_ADDRESS = "192.168.0.110";
    private final int  SERVER_PORT = 9876;

    public static boolean init_success;
    private UDPManager mUDPManager;

    @Override
    public void onCreate() {
        super.onCreate();
        init_success=true;
        initUDP();
    }

    private void testConnection() {

    }

    private void initUDP() {

        try {
            mUDPManager= new UDPManager(SERVER_IP_ADDRESS,SERVER_PORT);
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
