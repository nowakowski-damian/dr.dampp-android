package com.thirteendollars.drdampp.activities;

import android.content.Context;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.thirteendollars.drdampp.R;
import com.thirteendollars.drdampp.connection.APIDecoder;
import com.thirteendollars.drdampp.connection.UDPManager;
import com.thirteendollars.drdampp.fragments.AboutFragment;
import com.thirteendollars.drdampp.fragments.GUIControlFragment;
import com.thirteendollars.drdampp.fragments.SettingsFragment;
import com.thirteendollars.drdampp.fragments.StatusFragment;
import com.thirteendollars.drdampp.utils.SettingsPreferences;

import java.net.SocketException;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private final int SIGNAL_CHECK_THREAD_DELAY_MS = 2000;

    private UpdateSignalStatus mSignalStatusThread;
    private UDPManager mUDPManager;
    private SettingsPreferences mSettings;

    private DrawerLayout mDrawer;
    private Toolbar mToolbar;
    private ImageView mToolbarConnectionIcon;
    private TextView mToolbarMainText;
    private TextView mToolbarRightText;

    private int mSignalStrength; // 0-100%

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSettings = new SettingsPreferences(this);
        mSignalStrength=0;
        initViews();
        replaceFragment(R.id.content,new StatusFragment(),StatusFragment.FRAGMENT_TAG,null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initUDP();
        //test connection
        if(mUDPManager!=null) {
            mUDPManager.send(APIDecoder.testConnection());
        }
        mSignalStatusThread=new UpdateSignalStatus();
        mSignalStatusThread.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mUDPManager.cancel();
        mSignalStatusThread.cancel(true);
        mUDPManager=null;
        mSignalStatusThread=null;
    }

    private void initUDP() {

        mToolbarRightText.setText(R.string.toolbar_connecting_mssg);
        try {
            mUDPManager = new UDPManager(mSettings.getServerAddress(), mSettings.getServerPort()){
                @Override
                public void onResponseSkipped(int skippedResponses) {
                    }


                @Override
                public void onDataReceived(byte[] data) {

                }
            };
        } catch (UnknownHostException exception) {
            Log.e(getClass().getName(), exception.toString());
        } catch (SocketException exception) {
            Log.e(getClass().getName(), exception.toString());
        }
    }






    private void initViews() {

        //init variables
        mToolbar=(Toolbar) findViewById(R.id.toolbar);
        mDrawer=(DrawerLayout) findViewById(R.id.drawer_layout);
        mToolbarConnectionIcon=(ImageView) findViewById(R.id.toolbar_connection_icon);
        mToolbarMainText=(TextView)findViewById(R.id.toolbar_maintext);
        mToolbarRightText=(TextView)findViewById(R.id.toolbar_righttext);

        //init views
        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_status) {
            mToolbarMainText.setText(R.string.toolbar_status);
            replaceFragment(R.id.content,new StatusFragment(),StatusFragment.FRAGMENT_TAG,null);

        } else if (id == R.id.nav_joystickcontrol) {
            mToolbarMainText.setText(R.string.toolbar_joystick);
            replaceFragment(R.id.content,new GUIControlFragment(),GUIControlFragment.FRAGMENT_TAG,null);

        } else if (id == R.id.nav_voicecontrol) {
            mToolbarMainText.setText(R.string.toolbar_voice_control);

        } else if (id == R.id.nav_settings) {
            mToolbarMainText.setText(R.string.toolbar_settings);
            replaceFragment(R.id.content,new SettingsFragment(),SettingsFragment.FRAGMENT_TAG,null);

        } else if (id == R.id.nav_about) {
            mToolbarMainText.setText(R.string.toolbar_about);
            replaceFragment(R.id.content,new AboutFragment(),AboutFragment.FRAGMENT_TAG,null);
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void replaceFragment(int contaitnerId, Fragment fragment,String fragmentTag,String backStackTag) {

        if (backStackTag == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(contaitnerId, fragment, fragmentTag)
                    .disallowAddToBackStack()
                    .commit();
        } else{

            if (getSupportFragmentManager().getBackStackEntryCount()>4){
                getSupportFragmentManager().popBackStack(
                        getSupportFragmentManager().getBackStackEntryAt(0).getId(),
                        FragmentManager.POP_BACK_STACK_INCLUSIVE );
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(contaitnerId, fragment, fragmentTag)
                    .addToBackStack(backStackTag)
                    .commit();
        }
    }

    public Toolbar getToolbar(){
        return mToolbar;
    }

    public UDPManager getConnection(){
        return mUDPManager;
    }

    public SettingsPreferences getSettings(){
        return mSettings;
    }

    public int getSignalStrength() {
        return mSignalStrength;
    }



    private class UpdateSignalStatus extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            mSignalStrength = manager.calculateSignalLevel( manager.getConnectionInfo().getRssi(),101 );
            try {
                Thread.sleep(SIGNAL_CHECK_THREAD_DELAY_MS);
            } catch (InterruptedException e) {
                Log.e(getClass().getName(),e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            setConnectionQuality( (int)Math.round( (mSignalStrength)/25. ) );
            if( !isCancelled() ) {
                mSignalStatusThread = new UpdateSignalStatus();
                mSignalStatusThread.execute();
            }
        }

        private void setConnectionQuality(int signal) {

            switch (signal) {

                case 4:
                    mToolbarRightText.setText(R.string.toolbar_connected_mssg);
                    mToolbarRightText.setTextColor(Color.GREEN);
                    mToolbarConnectionIcon.setImageResource(R.drawable.ok_connection);
                    break;
                case 3:
                    mToolbarRightText.setText(R.string.toolbar_connected_mssg);
                    mToolbarRightText.setTextColor(Color.GREEN);
                    mToolbarConnectionIcon.setImageResource(R.drawable.one_s_connection);
                    break;
                case 2:
                    mToolbarRightText.setText(R.string.toolbar_connected_mssg);
                    mToolbarRightText.setTextColor(Color.GREEN);
                    mToolbarConnectionIcon.setImageResource(R.drawable.two_s_connection);
                    break;
                case 1:
                    mToolbarRightText.setText(R.string.toolbar_connected_mssg);
                    mToolbarRightText.setTextColor(Color.GREEN);
                    mToolbarConnectionIcon.setImageResource(R.drawable.three_s_connection);
                    break;
                default:
                    mToolbarRightText.setText(R.string.toolbar_disconnected_mssg);
                    mToolbarRightText.setTextColor(Color.parseColor("#990000"));
                    mToolbarConnectionIcon.setImageResource(R.drawable.no_connection);
            }
        }

    }


}
