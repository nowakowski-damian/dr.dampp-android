package com.thirteendollars.drdampp.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.service.voice.VoiceInteractionService;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.thirteendollars.drdampp.R;
import com.thirteendollars.drdampp.activities.MainActivity;
import com.thirteendollars.drdampp.connection.APIDecoder;
import com.thirteendollars.drdampp.connection.UDPManager;
import com.thirteendollars.drdampp.utils.SettingsPreferences;

import java.net.UnknownHostException;

/**
 * Created by Damian Nowakowski on 27.04.16.
 */
public class SettingsFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {

    public final static String FRAGMENT_TAG="SettingsFragment";

    private RadioGroup mJoystickSettings;
    private SettingsPreferences mSettings;
    private EditText mIpEditText;
    private EditText mPortEditText;
    private Button mUpdateConnectionButton;

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

        mIpEditText= (EditText) view.findViewById(R.id.settings_ip_address);
        mIpEditText.setText( mSettings.getServerAddress() );
        mPortEditText= (EditText) view.findViewById(R.id.settings_port);
        mPortEditText.setText( mSettings.getServerPort()+"" );
        mUpdateConnectionButton= (Button) view.findViewById(R.id.settings_save_conn_settings);
        mUpdateConnectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateConnection();
            }
        });

    }

    private void updateConnection() {
        String ipAdress = mIpEditText.getText().toString();
        int portNum = Integer.parseInt( mPortEditText.getText().toString() );
        // check data correctness
        int dotNum=0;
        for( char letter : ipAdress.toCharArray() ){
            if( letter=='.' ) {
                dotNum++;
            }
        }
        if( dotNum!=3 ) {
            Toast.makeText(getActivity(), "Your ip address is incorrect (ex. 192.168.1.1)",Toast.LENGTH_LONG).show();
            return;
        }
        if( portNum<0 || portNum>65535 ) {
            Toast.makeText(getActivity(), "Your port number is incorrect (min.0,max.65535)",Toast.LENGTH_LONG).show();
            return;
        }
        //try to change UDPManager settings
        UDPManager udp= ((MainActivity)getActivity()).getConnection();
        try {
            udp.setNewSettings(ipAdress,portNum);
        } catch (UnknownHostException e) {
            Toast.makeText(getActivity(), "Your ip address is incorrect (ex. 192.168.1.1)",Toast.LENGTH_LONG).show();
            Log.e(getClass().getName(),e.toString() );
            return;
        }
        // if everything is ok save to SharedPreferences
        mSettings.setServerAddress(ipAdress);
        mSettings.setServerPort(portNum);
        //and test connection
        udp.send(APIDecoder.testConnection() );
        Toast.makeText(getActivity(), "Updated",Toast.LENGTH_SHORT).show();
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
