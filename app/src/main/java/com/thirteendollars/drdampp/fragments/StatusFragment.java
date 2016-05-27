package com.thirteendollars.drdampp.fragments;

import android.content.Context;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thirteendollars.drdampp.R;
import com.thirteendollars.drdampp.activities.MainActivity;

import org.w3c.dom.Text;

/**
 * Created by Damian Nowakowski on 27.04.16.
 */
public class StatusFragment extends Fragment{

    public static final String FRAGMENT_TAG= "StatusFragmentTag";

    private TextView mWifiStatus;
    private TextView mSSID;
    private TextView mSignal;
    private TextView mPhoneIP;
    private TextView mServerIP;
    private TextView mBattery;
    private TextView mTemperature;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.status_layout,container,false);
        mWifiStatus=(TextView)view.findViewById(R.id.status_wifi_text);
        mSSID=(TextView)view.findViewById(R.id.status_ssid_text);
        mSignal=(TextView)view.findViewById(R.id.status_signal_text);
        mPhoneIP=(TextView)view.findViewById(R.id.status_phoneip_text);
        mServerIP=(TextView)view.findViewById(R.id.status_serverip_text);
        mBattery=(TextView)view.findViewById(R.id.status_battery_text);
        mTemperature=(TextView)view.findViewById(R.id.status_temperature_text);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fillViewsWithData();
    }

    @SuppressWarnings("deprecation")
    private void fillViewsWithData() {
        WifiManager manager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
        if( manager.getWifiState()==WifiManager.WIFI_STATE_ENABLED){
            mWifiStatus.setText(R.string.wifi_enabled);
        }
        else{
            mWifiStatus.setText(R.string.wifi_disabled);
        }
        mSSID.setText( manager.getConnectionInfo().getSSID() );
        mSignal.setText( ((MainActivity)getActivity()).getSignalStrength()+"");
        mPhoneIP.setText( Formatter.formatIpAddress( manager.getConnectionInfo().getIpAddress() ) );
        mServerIP.setText( ((MainActivity)getActivity()).getSettings().getServerAddress() );
    }




}
