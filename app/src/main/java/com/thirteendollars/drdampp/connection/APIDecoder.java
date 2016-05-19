package com.thirteendollars.drdampp.connection;

/**
 * Created by Damian Nowakowski on 27.04.16.
 */
public class APIDecoder {

    private final byte SET_MOTORS_ID=0;
    private final byte SET_SWITCH_ID=1;
    private final byte SET_ALL_SWITCHES_ID=1;
    private final byte LEFT_ID=2;
    private final byte RIGHT_ID=2;



    public byte[] setMotors(int leftLevel, int rightLevel ){

        byte[] pack = new byte[4];
        pack[0]=SET_MOTORS_ID;
        pack[1]=(byte)leftLevel;
        pack[2]=(byte)rightLevel;
        return pack;
    }

    public byte[] setSwitch(int switchIndex, boolean turnOn  ){

        byte[] pack = new byte[4];
        pack[0]=SET_SWITCH_ID;
        pack[1]=(byte)switchIndex;
        pack[2]= (byte)( turnOn ? 1:0 );
        return pack;
    }


    public byte[] setAllSwitches( boolean turnOn  ){

        byte[] pack = new byte[4];
        pack[0]=SET_ALL_SWITCHES_ID;
        pack[1]=-1;
        pack[2]= (byte)( turnOn ? 1:0 );
        return pack;
    }


    public byte[] turnLeft( int angle ){

        byte[] pack = new byte[4];
        pack[0]=LEFT_ID;
        pack[1]=0;
        pack[2]= (byte) angle;
        return pack;
    }

    public byte[] turnRight( int angle  ){

        byte[] pack = new byte[4];
        pack[0]=RIGHT_ID;
        pack[1]=1;
        pack[2]= (byte) angle;
        return pack;
    }



}
