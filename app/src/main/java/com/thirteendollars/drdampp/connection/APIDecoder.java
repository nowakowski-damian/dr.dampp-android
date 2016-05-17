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



    public byte[] getSetMotorsPack(int turnLeftLevel, int turnRightLevel ){

        byte[] pack = new byte[4];
        pack[0]=SET_MOTORS_ID;
        pack[1]=(byte)turnLeftLevel;
        pack[2]=(byte)turnRightLevel;
        return pack;
    }

    public byte[] getSetSwitchPack(byte switchIndex, boolean turnOn  ){

        byte[] pack = new byte[4];
        pack[0]=SET_SWITCH_ID;
        pack[1]=switchIndex;
        pack[2]= (byte)( turnOn ? 1:0 );
        return pack;
    }


    public byte[] getSetAllSwitchesPack( boolean turnOn  ){

        byte[] pack = new byte[4];
        pack[0]=SET_ALL_SWITCHES_ID;
        pack[1]=-1;
        pack[2]= (byte)( turnOn ? 1:0 );
        return pack;
    }


    public byte[] getTurnLeftPack( int angle ){

        byte[] pack = new byte[4];
        pack[0]=LEFT_ID;
        pack[1]=0;
        pack[2]= (byte) angle;
        return pack;
    }

    public byte[] getTurnRightPack( int angle  ){

        byte[] pack = new byte[4];
        pack[0]=RIGHT_ID;
        pack[1]=1;
        pack[2]= (byte) angle;
        return pack;
    }



}
