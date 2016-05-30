package com.thirteendollars.drdampp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.thirteendollars.drdampp.R;
import com.thirteendollars.drdampp.activities.MainActivity;
import com.thirteendollars.drdampp.connection.APIDecoder;
import com.thirteendollars.drdampp.connection.UDPManager;
import com.thirteendollars.drdampp.utils.VoiceController;

/**
 * Created by Damian Nowakowski on 27.04.16.
 */
public class VoiceControlFragment extends Fragment implements View.OnClickListener {

    public final static String FRAGMENT_TAG="VoiceControlFragmentTag";
    private final int MOTOR_SPEED_CONST_VALUE = 50; // 0-100%
    private final int MOTOR_TURN_ANGLE_CONST_VALUE = 90; // 0-100%


    private UDPManager mUDPManager;
    private APIDecoder mApi;
    private VoiceController mVoiceController;

    private TextView mHeardText;
    private Button mStopButton;
    private ToggleButton mButton0;
    private ToggleButton mButton1;
    private ToggleButton mButton2;
    private ToggleButton mButton3;
    private ToggleButton mButton4;
    private ToggleButton mButton5;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.voicecontrol_layout,container,false);
        initViews(view);
        mUDPManager=((MainActivity)getActivity()).getConnection();
        mApi= new APIDecoder();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mVoiceController=initVoiceController();
        mVoiceController.execute();
    }

    @Override
    public void onPause() {
        super.onPause();
        if( mVoiceController!=null ){
            mVoiceController.cancel(true);
            mVoiceController=null;
        }
    }

    @Override
    public void onClick(View v) {
        byte[] dataToSend=null;
        switch ( v.getId() ){
            case R.id.voicecontrol_stop_button:
                dataToSend=mApi.setMotors(0,0);
                mHeardText.setText(R.string.command_stop);
                break;
            case R.id.control_button_0: dataToSend=mApi.setSwitch( 0, mButton0.isChecked() ); break;
            case R.id.control_button_1: dataToSend=mApi.setSwitch( 1, mButton1.isChecked() ); break;
            case R.id.control_button_2: dataToSend=mApi.setSwitch( 2, mButton2.isChecked() ); break;
            case R.id.control_button_3: dataToSend=mApi.setSwitch( 3, mButton3.isChecked() ); break;
            case R.id.control_button_4: dataToSend=mApi.setSwitch( 4, mButton4.isChecked() ); break;
            case R.id.control_button_5: dataToSend=mApi.setSwitch( 5, mButton5.isChecked() ); break;
        }
        mUDPManager.send(dataToSend);
    }

    private void initViews(View view){
        //init
        mHeardText = (TextView) view.findViewById(R.id.voicecontrol_heard_text);
        mStopButton = (Button) view.findViewById(R.id.voicecontrol_stop_button);
        mButton0=(ToggleButton)view.findViewById(R.id.control_button_0);
        mButton1=(ToggleButton)view.findViewById(R.id.control_button_1);
        mButton2=(ToggleButton)view.findViewById(R.id.control_button_2);
        mButton3=(ToggleButton)view.findViewById(R.id.control_button_3);
        mButton4=(ToggleButton)view.findViewById(R.id.control_button_4);
        mButton5=(ToggleButton)view.findViewById(R.id.control_button_5);
        // set OnClickListeners
        mStopButton.setOnClickListener(this);
        mButton0.setOnClickListener(this);
        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);
        mButton4.setOnClickListener(this);
        mButton5.setOnClickListener(this);
    }

    private VoiceController initVoiceController() {

        mHeardText.setText(R.string.voicecontrol_initializing);
        if(mVoiceController!=null){
            mVoiceController.cancel(true);
        }

        VoiceController controller = new VoiceController( getActivity().getApplicationContext() ) {
            @Override
            public void initSuccess() {
                mHeardText.setText(R.string.voicecontroller_init_success);
            }

            @Override
            public void initFailed(Exception result) {
                mHeardText.setText(R.string.voicecontrol_failed);
                Log.e(getClass().getName(),result.toString());
            }

            @Override
            public void onWordSaid(String text) {
                parseCommand(text);
            }

            @Override
            public void onError(Exception e) {
                mHeardText.setText(R.string.voicecontrol_error);
                Log.e(getClass().getName(),e.toString());
            }
        };

        return controller;
    }

    private void parseCommand(String text) {
        switch (text){
            case VoiceController.GO_AHEAD:
                mUDPManager.send( mApi.setMotors(MOTOR_SPEED_CONST_VALUE,MOTOR_SPEED_CONST_VALUE));
                mHeardText.setText(R.string.command_goahead);
                break;
            case VoiceController.GO_BACK:
                mUDPManager.send( mApi.setMotors(-MOTOR_SPEED_CONST_VALUE,-MOTOR_SPEED_CONST_VALUE));
                mHeardText.setText(R.string.command_back);
                break;
            case VoiceController.TURN_LEFT:
                mUDPManager.send( mApi.turnLeft(MOTOR_TURN_ANGLE_CONST_VALUE));
                mHeardText.setText(R.string.command_left);
                break;
            case VoiceController.TURN_RIGHT:
                mUDPManager.send( mApi.turnRight(MOTOR_TURN_ANGLE_CONST_VALUE));
                mHeardText.setText(R.string.command_right);
                break;
            default:
                mUDPManager.send( mApi.setMotors(0,0) );
                mHeardText.setText(R.string.command_stop);
        }
    }


}
